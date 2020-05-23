package com.vgg.fvp.common.smtp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private Logger logger = LoggerFactory.getLogger(EmailService.class);

    private JavaMailSender javaMailSender;

    public void sendSimpleMessage(MailContent mail) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setTo(mail.getReceiver());
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getSender());
            javaMailSender.send(message);
    }

    @Async
    public void sendMail(String from, String[] to, String subject, String title, String details){
        MailContent mailContent = mailTemplate(from, to, subject, title, details);
        try {
            sendSimpleMessage(mailContent);
        } catch (MessagingException | IOException e) {
            logger.error("Exception  occurred trying to send a mail", e);
        }
    }

    private MailContent mailTemplate(String from,String[] to,String subject,String title,String details){
        MailContent mail = new MailContent();
        mail.setSender(from);
        mail.setReceiver(to);
        mail.setSubject(subject);
        Map<String,Object> model = new HashMap<>();
        model.put("title",title);
        model.put("details",details);
        mail.setModel(model);
        return mail;
    }


    @Async
    public void sendCustomisedMail(MailContentDTO mailContentDTO) {
        MailContent mailContent = converter(mailContentDTO);
        try{
            sendSimpleMessage(mailContent);
        } catch (MessagingException | IOException e) {
            logger.error("Exception occurred trying to send a mail ", e);
        }

    }

    public MailContent converter(MailContentDTO mailContentDTO){
        MailContent mailContent = new MailContent();
        mailContent.setReceiver(new String[]{mailContentDTO.getReceiver()});
        mailContent.setSender(mailContent.getSender());
        mailContent.setSubject(mailContentDTO.getSubject());
        Map<String , Object> model = new HashMap<>();
        model.put("details", mailContentDTO.getContent());
        mailContent.setModel(model);
        return  mailContent;
    }
}
