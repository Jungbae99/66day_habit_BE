package day.dayBackend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final String fromEmail;

    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String fromEmail) {
        this.emailSender = mailSender;
        this.fromEmail = fromEmail;

    }

    private MimeMessage createEmailForm(String toEmail, String certCode) throws MessagingException {
        String title = "66일의 걸음 회원가입 인증입니다.";
        String content = String.format("""
                    습관 만들기 플랫폼 "66 일의 걸음" 회원가입 인증 메일입니다.
                    본인이 요청하지 않았을 경우 링크를 누르지 마시고 고객센터에 연락해 주세요.
                    회원가입을 완료하려면 아래 인증코드를 입력해 주세요.
                    위 인증코드가 만료되었다면 다시 인증을 요청하세요.
                    
                    %s
                """, certCode);

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject(title);
        message.setFrom(fromEmail);
        message.setText(content);
        return message;
    }

    @Async("threadPoolTaskExecutor")
    public void sendEmail(String toEmail, String authNum) throws MessagingException {
        MimeMessage emailForm = createEmailForm(toEmail, authNum);
        emailSender.send(emailForm);
    }

}
