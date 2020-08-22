package pl.lodz.p.it.boorger.utils;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class EmailService {

    private JavaMailSender javaMailSender;

    @Async
    public void sendConfirmationEmail(String mail, String language, String token, String url, String path) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(mail);
        helper.setSubject(MessageProvider.getTranslatedText("email.confirmation.subject", language));
        String link = url.substring(0, url.length() - path.length()).concat("/confirm?token=");
        helper.setText("<a href=\"" + link + token + "\">" + MessageProvider.getTranslatedText("email.confirmation.body", language)
                + "</a></br></br>" + MessageProvider.getTranslatedText("email.footer", language), true);
        javaMailSender.send(message);
    }

    @Async
    public void sendAccountBlockedEmail(String mail, String language) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail);
        message.setSubject(MessageProvider.getTranslatedText("email.accountblocked.subject", language));
        message.setText(MessageProvider.getTranslatedText("email.accountblocked.body", language) + "\n\n"
                + MessageProvider.getTranslatedText("email.footer", language));
        javaMailSender.send(message);
    }
    
    @Async
    public void sendAdminAuthEmail(String mail, String language, String ip) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail);
        message.setSubject(MessageProvider.getTranslatedText("email.adminauth.subject", language));
        message.setText(MessageProvider.getTranslatedText("email.adminauth.body", language) + ", "
                + MessageProvider.getTranslatedText("email.adminauth.date", language) + " "
                + DateFormatter.dateToString(LocalDateTime.now()) + ", "
                + MessageProvider.getTranslatedText("email.adminauth.ip", language) + " "
                + ip + "\n\n"
                + MessageProvider.getTranslatedText("email.footer", language));
        javaMailSender.send(message);
    }

    @Async
    public void sendPasswordResetEmail(String mail, String language, String token, String url, String  path) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(mail);
        helper.setSubject(MessageProvider.getTranslatedText("email.resetpassword.subject", language));
        String link = url.substring(0, url.length() - path.length()).concat("/changePassword?token=");
        helper.setText("<a href=\"" + link + token + "\">" + MessageProvider.getTranslatedText("email.resetpassword.body", language)
                + "</a></br></br>" + MessageProvider.getTranslatedText("email.footer", language), true);
        javaMailSender.send(message);
    }
}
