package com.dorukt.rabbitmq.producer;

import com.dorukt.rabbitmq.model.ActivateModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationProducer {

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;

    @Value("${rabbitmq.activation-bindingKey}")
    private String activationBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void activation(Long authId){
        rabbitTemplate.convertAndSend(exchange,activationBindingKey,authId);
    }
}
