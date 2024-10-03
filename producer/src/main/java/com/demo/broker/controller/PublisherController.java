package com.demo.broker.controller;

import com.demo.broker.model.Message;
import com.demo.broker.service.Producer;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class PublisherController {

    private final Producer producer;

    public PublisherController(Producer producer) {
        this.producer = producer;
    }


    @PostMapping("/queue/{queue}")
    public ResponseEntity<String> publish(@PathVariable String queue, @RequestBody Message message) {
        try {
            producer.send(queue,message);
            return new ResponseEntity<>("Message Sent", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/topic/{topic}")
    public ResponseEntity<String> publishTopic(@PathVariable String topic, @RequestBody Message message) {
        try {
            producer.sendTopic(topic,message);
            return new ResponseEntity<>("Message Sent", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
