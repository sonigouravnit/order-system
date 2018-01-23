package com.gourav.kafka;

import com.gourav.utils.CommonUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by gouravsoni on 23/01/18.
 */
public class KafkaConsumerImpl implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    private ArrayList<String> topics;
    private KafkaConsumerBase baseClass;
    private KafkaConsumer<String, String> consumer;

    private long consumerTimeoutinMili = 1000 * 60 * 5;

    public KafkaConsumerImpl(KafkaConsumer<String, String> consumer, ArrayList<String> topics,
                             KafkaConsumerBase baseClass) {

        this.consumer = consumer;
        this.topics = topics;
        this.baseClass = baseClass;
    }

    public void run() {

        consumer.subscribe(topics);
        try {
            while (true) {
                logger.info("Kafka Consumer for " + CommonUtils.StringListToCommaSeperated(topics)
                        + " started : Inside while loop");
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                logger.info("Kafka Consumer for " + CommonUtils.StringListToCommaSeperated(topics)
                        + " polled total size : " + records.count());

                try {
                    for (ConsumerRecord<String, String> record : records) {

                        boolean doCommit = baseClass.KafkaConsumerOutput(record.topic(), record.value());
                        if (doCommit) {
                            consumer.commitSync(
                                    Collections.singletonMap(new TopicPartition(record.topic(), record.partition()),
                                            new OffsetAndMetadata(record.offset() + 1)));
                        } else {
                            logger.info("Kafka Consumer for " + CommonUtils.StringListToCommaSeperated(topics)
                                    + " Received not to commit command - consumer going fro sleep");
                            Thread.sleep(consumerTimeoutinMili);
                            consumer.seek(new TopicPartition(record.topic(), record.partition()), record.offset());
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    logger.error("Kafka Consumer for " + CommonUtils.StringListToCommaSeperated(topics)
                            + " got Exception inside while loop ", e);
                } catch (CommitFailedException e) {
                    logger.error("Kafka Consumer for " + CommonUtils.StringListToCommaSeperated(topics)
                            + " got CommitFailedException inside while loop ", e);
                }
            }
        } catch (Exception e) {
            logger.error("Kafka Consumer for " + CommonUtils.StringListToCommaSeperated(topics)
                    + " got Exception outside while loop, closing Consumer ", e);
            consumer.close();
        }
    }
}
