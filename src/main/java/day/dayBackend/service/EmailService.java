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
    private final String certUrl;
    private final String resendUrl;

    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String fromEmail, @Value("${deploy.client.url}") String serverUrl) {
        this.emailSender = mailSender;
        this.fromEmail = fromEmail;
        this.certUrl = String.format("%s/certification", serverUrl);
        this.resendUrl = String.format("%s/certification", serverUrl);
    }

    private MimeMessage createEmailForm(String toEmail, String certCode) throws MessagingException {
        String title = "66일의 걸음 회원가입 인증입니다.";
//        String content = " 습관 만들기 플랫폼 < 66 일의 걸음>의 회원가입 인증 메일입니다. 본인이 요청하지 않았을 경우 고객센터에 연락해 주세요." + certCode;

        String content = String.format("""
습관 만들기 플랫폼 < 66 일의 걸음>의 회원가입 인증 메일입니다.
본인이 요청하지 않았을 경우 링크를 누르지 마시고 고객센터에 연락해 주세요.

회원가입을 완료하려면 아래 링크를 클릭해 주세요.
%s?email=%s&certCode=%s

위 링크가 만료되었다면 아래 링크를 클릭해 주세요.
%s?email=%s
""", this.certUrl, toEmail, certCode, this.resendUrl, toEmail);


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
