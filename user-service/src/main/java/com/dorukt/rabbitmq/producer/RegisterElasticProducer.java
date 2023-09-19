package com.dorukt.rabbitmq.producer;


import com.dorukt.rabbitmq.model.RegisterElasticModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterElasticProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.user-exchange}")
    private String directExchange;
    @Value("${rabbitmq.register-elastic-binding}")
    private String registerBindingKey;

    public void saveNewUser(RegisterElasticModel model){
        rabbitTemplate.convertAndSend(directExchange,registerBindingKey,model);
    }
}
