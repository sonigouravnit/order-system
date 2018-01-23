package com.gourav.kafka;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gourav.config.ConsumerBase;
import com.gourav.config.environmentConfig.EnvironmentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by gouravsoni on 23/01/18.
 */
@Component
public class KafkaConsumerInstance extends KafkaConsumerBase {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerInstance.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EnvironmentConfig kafkaConfig;

    @Autowired
    private ConsumerBase consumerMappingProvider;

    private boolean validateKafkaConfig() {
        return null != kafkaConfig.getKafkaTopics() && kafkaConfig.getKafkaTopics().size() > 0
                && null != kafkaConfig.getBootstrapServers() && kafkaConfig.getBootstrapServers().size() > 0
                && null != kafkaConfig.getGroupId() && kafkaConfig.getGroupId().length() > 0;
    }

    @PostConstruct
    public void initializeConsumer() {
        try {
            if (validateKafkaConfig()) {
                initializeManualCommitKafkaConsumer(kafkaConfig.getBootstrapServers(),
                        kafkaConfig.getGroupId(), kafkaConfig.getTopics());
            } else {
                throw new Error("Please add bootstrap servers, kafka topics and kafka groupId in application.yml");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Error("Exception while initializing kafka consumer");
        }
    }

    public boolean KafkaConsumerOutput(String topic, String output) {
        try {
            if (kafkaConfig.getTopics().contains(topic)) {
                logger.debug("received topic : {} and data : {}", topic, output);
                return postOperation(topic, output);
            } else {
                logger.error("Received topic :  {} But expecting one of {}", topic,
                        kafkaConfig.getTopics().toArray().toString());
                return false;
            }
        } catch (JsonParseException e) {
            logger.error("JsonParseException topic {}, output {}, and skipping request", topic, output, e);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    private boolean postOperation(String topic, String output) throws IOException {
        return consumerMappingProvider.getTopicClassMapping().get(topic).handleResponse(objectMapper.readValue(output, String.class));
    }
}
