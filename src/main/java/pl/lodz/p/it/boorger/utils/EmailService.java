package pl.lodz.p.it.boorger.utils;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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
}
