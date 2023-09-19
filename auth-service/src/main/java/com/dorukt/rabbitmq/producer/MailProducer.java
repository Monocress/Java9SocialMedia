package com.dorukt.rabbitmq.producer;

import com.dorukt.rabbitmq.model.MailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailProducer {

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;

    @Value("${rabbitmq.mail-bindingKey}")
    private String mailBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendMail(MailModel model){
        rabbitTemplate.convertAndSend(exchange,mailBindingKey,model);
    }
}
