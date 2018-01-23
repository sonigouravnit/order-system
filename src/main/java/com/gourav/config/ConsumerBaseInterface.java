package com.gourav.config;

import javax.annotation.PostConstruct;

/**
 * Created by gouravsoni on 23/01/18.
 */
public interface ConsumerBaseInterface {


    @PostConstruct
    void addTopicToConsumer();

    boolean handleResponse(Object output);
}
