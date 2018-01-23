package com.gourav.config.environmentConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gouravsoni on 23/01/18.
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties
public class EnvironmentConfig {
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentConfig.class);

    @Value("${kafkaConsumer.bootstrapServers}")
    private String[] bootstrapServers;

    @Value("${kafkaConsumer.groupId}")
    private String groupId;

    private Map<String, String> kafkaTopics;

    private static ArrayList<String> topicList = new ArrayList<>();
    private static ArrayList<String> bootstrapServersList = new ArrayList<>();


    public ArrayList<String> getBootstrapServers() {
        if (null != bootstrapServers) {
            if (bootstrapServersList.size() == 0) {
                bootstrapServersList.addAll(Arrays.asList(bootstrapServers));
            }
            return bootstrapServersList;
        }
        return bootstrapServersList;
    }

    public String getGroupId() {
        return groupId;
    }

    public Map<String, String> getKafkaTopics() {
        return kafkaTopics;
    }

    public ArrayList<String> getTopics() {
        if (null != kafkaTopics) {
            if (topicList.size() == 0) {
                topicList.addAll(kafkaTopics.values());
            }
            return topicList;
        }
        return topicList;
    }

    public void setBootstrapServers(String[] bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setKafkaTopics(HashMap<String, String> kafkaTopics) {
        this.kafkaTopics = kafkaTopics;
    }
}
