package day.dayBackend.service.recommend;

import day.dayBackend.domain.recommend.RecommendedHabit;
import day.dayBackend.dto.recommend.RecommendedHabitDto;
import day.dayBackend.dto.recommend.HabitRecommendResponseDto;
import day.dayBackend.repository.recommend.HabitRecommendRepository;
import day.dayBackend.service.AuthService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HabitRecommendService {

    private final HabitRecommendRepository habitRecommendRepository;
    private boolean isFirstExecution = true;

    /**
     * 랜덤 습관 조회
     */
    public List<RecommendedHabitDto> getRandomList() {
        return habitRecommendRepository.findRandomHabit().orElse(Collections.emptyList())
                .stream().map(RecommendedHabitDto::fromEntity).collect(Collectors.toList());

    }


    @Transactional
    @PostConstruct
    public void executeHabitCrawlerOnStartup() {
        if (isFirstExecution) {
            executeHabitCrawler();
            isFirstExecution = false;
        }
    }

    @Transactional
    public void executeHabitCrawlerScheduled() {
        executeHabitCrawler();
    }


    private void executeHabitCrawler() {
        Logger logger = LoggerFactory.getLogger(AuthService.class);

        try {
            habitRecommendRepository.deleteAll();
            String pythonCommand;
            String pythonScriptPath;

            if (System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("window")) {
                // 윈도우
                pythonCommand = "python";
                pythonScriptPath = "/python/HabitCrawling.py";
            } else {
                // 다른 운영체제
                pythonCommand = "python3";
                pythonScriptPath = "/python/HabitCrawling.py";
            }

            InputStream pythonScriptStream = getClass().getResourceAsStream(pythonScriptPath);

            File tempScriptFile = File.createTempFile("HabitCrawling", ".py");
            tempScriptFile.deleteOnExit();

            try (OutputStream out = new FileOutputStream(tempScriptFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = pythonScriptStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

            } catch (IOException e) {
                logger.error("Failed to create a temporary script file. ", e);
            } finally {
                pythonScriptStream.close();
            }


            ProcessBuilder processBuilder = new ProcessBuilder(pythonCommand, tempScriptFile.getAbsolutePath());
            Process process = processBuilder.start();
            InputStream errorStream = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            StringBuilder errorOutput = new StringBuilder();
            String line;

            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\\n");
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info("HabitCrawling.py execution succeeded.");
            } else {
                logger.error("HabitCrawling.py execution failed. Exit code: " + exitCode);
                logger.error("Error output:\\n" + errorOutput);

            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public HabitRecommendResponseDto getHabitList(Pageable pageable) {
        // Python 스크립트 실행
        executeHabitCrawler();

        // 데이터베이스에서 습관 데이터를 조회
        Page<RecommendedHabit> habits = habitRecommendRepository.findAll(pageable);

        return HabitRecommendResponseDto.of(
                habits.getTotalElements(),
                habits.getTotalPages(),
                habits.getContent()
                        .stream().map(RecommendedHabitDto::fromEntity)
                        .collect(Collectors.toList()));
    }

}
