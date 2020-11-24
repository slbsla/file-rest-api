package com.slb.file.api.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailingService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithAttachement(String email, File myFile, String subject, String fname) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setFrom("no-reply@me.com");
        helper.setText("<html><body><h1>Hello, your file is now available</h1><body></html>", true);
        FileSystemResource file  = new FileSystemResource(myFile);
        helper.addAttachment(fname, file);
        helper.setSubject(subject);
        mailSender.send(message);
    }
}
