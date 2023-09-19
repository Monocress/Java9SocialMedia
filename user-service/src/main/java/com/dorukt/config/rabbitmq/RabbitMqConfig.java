package com.dorukt.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.user-exchange}")
    private String userExchange;
    @Value("${rabbitmq.register-elastic-binding}")
    private String registerElasticBinding;

    @Value("${rabbitmq.register-elastic-queue}")
    private String registerElasticQueueName;
    @Value("${rabbitmq.register-queue}")
    private String registerQueue;
    @Value("${rabbitmq.activation-queue}")
    private String activateQueue;

    @Bean
    Queue registerQueue(){
        return new Queue(registerQueue);
    }

    @Bean
    Queue activateQueue(){
      return new Queue(activateQueue);
    }

    @Bean
    Queue registerElasticQueue(){
        return new Queue(registerElasticQueueName);
    }

    @Bean
    DirectExchange exchange(){
        return new DirectExchange(userExchange);
    }
    @Bean
    public Binding bindingRegisterElastic(final Queue registerElasticQueue,final DirectExchange exchange){
        return BindingBuilder.bind(registerElasticQueue).to(exchange).with(registerElasticBinding);
    }
}
