package com.gourav.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gouravsoni on 23/01/18.
 */
@Component
public class ConsumerBase {

    private static Map<String, ConsumerBaseInterface> topicClassMapping = new HashMap<>();

    public Map<String, ConsumerBaseInterface> getTopicClassMapping() {
        return topicClassMapping;
    }

    public void addTopicToConsumer(String topic, ConsumerBaseInterface consumerBaseInterface) {
        if (null != topic) {
            if (!topicClassMapping.containsKey(topic)) {
                topicClassMapping.put(topic, consumerBaseInterface);
            } else {
                throw new Error(topic + " already present for consumer");
            }
        }
    }
}
