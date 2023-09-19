package com.dorukt.rabbitmq.consumer;

import com.dorukt.rabbitmq.model.MailModel;
import com.dorukt.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailConsumer {

    private final MailSenderService mailSenderService;

    @RabbitListener(queues = "${rabbitmq.mail-queue}")
    public void sendMail(MailModel model){
        log.info("model => {}",model);
        mailSenderService.sendMail(model);
    }
}
