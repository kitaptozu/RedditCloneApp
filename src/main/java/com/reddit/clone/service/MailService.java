package com.reddit.clone.service;

import com.reddit.clone.exceptions.SpringRedditException;
import com.reddit.clone.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    // Mail gönderilene süreci zaman alacağı için asenkron olaraak belirttik.
    @Async
    void sendMail(NotificationEmail notificationEmail) {

        // Lambda ifadesi MimeMessagePreparator class'ına ait bir instance oluşturur.
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            // mailtrap.io = örnek olması için bir fake smtp server.
            // Buraya yazılan mail göstermelik(?)
            mimeMessageHelper.setFrom("mail@email.com");

            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            //mailContentBuilder.build() bu method mesajı html formata dönüştürüyor.Bizim yazdığımız class.
            mimeMessageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));

        };
        try {
            mailSender.send(mimeMessagePreparator);
            log.info("Activation email sent!");
        } catch (MailException e) {
            log.error("Exception occrured when sending mail", e);
            throw new SpringRedditException("Exception occurred when sending mail to " + notificationEmail.getRecipient(), e);
        }

    }
}
