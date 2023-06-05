package com.intern.utils;

import com.intern.data.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;

@Component
public class EmailSender {
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private JavaMailSender javaMailSender;

    public void simpleEmail(Mail mail) throws Exception{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getTo());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        javaMailSender.send(message);
    }

    public void emailWithAttachment(Mail mail,String filePath)throws MessagingException{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,StandardCharsets.UTF_8.name());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent());
        try {
            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(file.getFilename(),file);
        }catch (NullPointerException npe){
            System.out.println("UploadFile not found : " + npe.getMessage());
        }
        javaMailSender.send(message);
    }
    public void emailWithTemplate(Mail mail,String filePath) throws MessagingException{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        try{
            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(file.getFilename(),file);
            Context context = new Context();
            /*context.setVariable();*/
            String mailTemplate = templateEngine.process("email",context);
            helper.setTo(mail.getTo());
            helper.setSubject(mail.getSubject());
            helper.setText(mailTemplate,true);
            javaMailSender.send(message);
        }catch (NullPointerException npe){
            System.out.println("Error UploadFile Path : " +npe.getMessage());
        }
    }
}
