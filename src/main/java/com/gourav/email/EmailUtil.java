package com.gourav.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Created by gouravsoni on 23/01/18.
 */
@Component
public class EmailUtil {

    @Value("${email.from}")
    private String user;

    @Value("${email.password}")
    private String password;

    @Value("${email.body}")
    private String emailBody;

    @Value("${email.fileNotFoundMsg}")
    private String fileNotFoundMsg;

    @Value("${email.smtpHost}")
    private String smtpHost;

    @Value("${email.smtpPort}")
    private String smtpPort;

    Properties properties;

    Session session;

    @PostConstruct
    public void postConstructData() {
        properties = System.getProperties();
        properties.setProperty("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.starttls.enable", "true");


        session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });
    }

    private MimeBodyPart addAttachment(MimeBodyPart messageBodyPart2, String filePath) throws MessagingException {
        DataSource source = new FileDataSource(filePath);
        messageBodyPart2.setDataHandler(new DataHandler(source));
        messageBodyPart2.setFileName(filePath);
        return messageBodyPart2;
    }


    public void sendEmail(String to, String filePath) throws Exception {

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Order-system mail");

        Multipart multipart = new MimeMultipart();


        BodyPart messageBodyPart1 = new MimeBodyPart();
        messageBodyPart1.setText(emailBody);

        MimeBodyPart messageBodyPart2 = new MimeBodyPart();

        if (null != filePath) {
            messageBodyPart2 = addAttachment(messageBodyPart2, filePath);
            multipart.addBodyPart(messageBodyPart2);
        } else {
            messageBodyPart1.setText(emailBody + "\n\n\n" + fileNotFoundMsg);
        }

        multipart.addBodyPart(messageBodyPart1);

        message.setContent(multipart);

        Transport.send(message);

    }
}