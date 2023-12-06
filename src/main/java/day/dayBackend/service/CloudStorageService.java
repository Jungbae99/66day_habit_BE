package day.dayBackend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class CloudStorageService {

    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public CloudStorageService(AmazonS3 amazonS3, @Value("${spring.cloud.aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public void uploadFile(MultipartFile file, String filePath) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata);

            amazonS3.putObject(putObjectRequest);
        } catch (IOException e) {
            //:TODO 예외처리..?
            e.printStackTrace();
        }
    }


    /**
     * 파일 url 생성
     */
    public String getUrl(String filePath) {
        return "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + filePath;
    }

    /**
     * 파일 url 로부터 버킷 객체명 추출
     */
    public String getObjectName(String url) {
        try {
            URL parsedUrl = new URL(url);
            String path = parsedUrl.getPath();
            String objectKey = path.substring(1); // / 다음의 문자열을 가져옴
            return objectKey;
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
