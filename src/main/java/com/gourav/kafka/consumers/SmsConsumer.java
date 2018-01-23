package com.gourav.kafka.consumers;

import com.gourav.config.ConsumerBase;
import com.gourav.config.ConsumerBaseInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by gouravsoni on 23/01/18.
 */
@Component
public class SmsConsumer implements ConsumerBaseInterface {

    private static final Logger logger = LoggerFactory.getLogger(SmsConsumer.class);

    @Value("${kafkaTopics.smsTopic:#{NULL}}")
    String smsTopic;

    @Autowired
    private ConsumerBase consumerBase;

    @Override
    @PostConstruct
    public void addTopicToConsumer() {
        consumerBase.addTopicToConsumer(smsTopic, this);
    }

    @Override
    public boolean handleResponse(Object output) {
        logger.info("SMS - sent for invoiceId {}", output);
        return true;
    }
}
