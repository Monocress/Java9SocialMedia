package com.dorukt.service;

import com.dorukt.rabbitmq.model.MailModel;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {
private final JavaMailSender javaMailSender;
//public void sendMail(String content){
//    SimpleMailMessage mailMessage = new SimpleMailMessage();
//    mailMessage.setFrom("${java9mail}");
//    mailMessage.setTo("java5and6mo@gmail.com");
//    mailMessage.setSubject("Aktivasyon Kodunuz....");
//    mailMessage.setText("code:"+content);
//    javaMailSender.send(mailMessage);
//}
    public void sendMail(MailModel model){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("${java9mail}");
        mailMessage.setTo(model.getEmail());
        mailMessage.setSubject("Aktivasyon Kodunuz....");
        mailMessage.setText("code:"+model.getActivationCode());
        javaMailSender.send(mailMessage);
    }

}
