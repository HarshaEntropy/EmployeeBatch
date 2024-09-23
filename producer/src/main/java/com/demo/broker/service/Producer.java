package com.demo.broker.service;

import com.demo.broker.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import java.lang.String;

@Service
public class Producer {

    Logger logger = LoggerFactory.getLogger(Producer.class);

    private final ObjectMapper mapper;
    private final JmsTemplate jmsTemplate;


    public Producer(ObjectMapper mapper, JmsTemplate jmsTemplate) {
        this.mapper = mapper;
        this.jmsTemplate = jmsTemplate;
    }

    public void send(String queue,Message message) {
        try {
            String jmsMessage = mapper.writeValueAsString(message);
            logger.info("Sending Message :: {}", jmsMessage);
            jmsTemplate.convertAndSend(queue, jmsMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
