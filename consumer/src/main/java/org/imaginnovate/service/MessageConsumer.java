package org.imaginnovate.service;

import lombok.RequiredArgsConstructor;
import org.imaginnovate.sheduler.BatchScheduler;
import org.slf4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
//@RequiredArgsConstructor
public class MessageConsumer {

    Logger LOGGER = getLogger(MessageConsumer.class);

    private final BatchScheduler batchScheduler;

    public MessageConsumer(BatchScheduler batchScheduler) {
        this.batchScheduler = batchScheduler;
    }

    @JmsListener(destination = "${spring.artemis.embedded.queues}")
    public void messageListener(String message) {

        LOGGER.info("Message received, {}", message);
        batchScheduler.runEmployeeBatchJob();

    }
}
