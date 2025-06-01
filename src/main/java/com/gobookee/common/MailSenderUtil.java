package com.gobookee.common;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MailSenderUtil {
    private static final Properties mailProps = new Properties();

    static {
        String path = MailSenderUtil.class.getResource("/config/mail.properties").getPath();
        try (FileReader fr = new FileReader(path)) {
            mailProps.load(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean sendEmail(String to, String subject, String content) {
        Session session = Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        mailProps.getProperty("mail.smtp.user"),
                        mailProps.getProperty("mail.smtp.password")
                );
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailProps.getProperty("mail.smtp.user")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
