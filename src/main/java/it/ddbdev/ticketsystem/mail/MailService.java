package it.ddbdev.ticketsystem.mail;

import it.ddbdev.ticketsystem.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class MailService {



    @Autowired
    JavaMailSender javaMailSender;

    @Value("${app.mail.sender}")
    private String from;

    @Async
    public void sendMail(Mail mail) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false);
            mimeMessageHelper.setFrom(mail.getMailFrom());
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setText(mail.getMailContent());

            javaMailSender.send(mimeMessage);
            log.info(mail.toString());

        } catch (MessagingException e) {
            log.error("ERROR SENDING EMAIL:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public Mail createMail(User u, String subject, String object, String s) { // String s = confirm link + confirm code

        Mail m = new Mail();
        m.setMailFrom(from);
        m.setMailTo(u.getEmail());
        m.setMailSubject(subject);
        m.setMailContent(object+s);
        return m;
    }

}
