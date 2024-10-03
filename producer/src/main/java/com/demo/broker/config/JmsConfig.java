package com.demo.broker.config;

//import ja.jms.ConnectionFactory;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;


@EnableJms
@Configuration
public class JmsConfig {

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        var jmsListenerFactory = new DefaultJmsListenerContainerFactory();
        jmsListenerFactory.setConnectionFactory(connectionFactory);
        jmsListenerFactory.setConcurrency("2-4");
        return jmsListenerFactory;
    }

    @Bean
    DynamicDestinationResolver destinationResolver() {
        return new DynamicDestinationResolver() {
            @Override
            public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException {
                if(destinationName.endsWith("Topic")) {
                    pubSubDomain = true;
                }
                else {
                    pubSubDomain = false;
                }
                return super.resolveDestinationName(session,destinationName,pubSubDomain);
            }
        };
    }

}
