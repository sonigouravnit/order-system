package com.gourav.kafka;

import com.gourav.utils.CommonUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by gouravsoni on 23/01/18.
 */
public abstract class KafkaConsumerBase {
    private static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private KafkaConsumer<String, String> kafkaConsumer;


    /**
     * @param bootstrapPaths ex: [localhost:2082]
     * @param groupId        ex: kafka_group_id
     * @param topicArray     [topic1, topic2]
     * @throws IllegalArgumentException
     * @throws Exception
     */
    public void initializeManualCommitKafkaConsumer(ArrayList<String> bootstrapPaths, String groupId,
                                                    ArrayList<String> topicArray) throws IllegalArgumentException, Exception {

        if (!validateInitializingComponents(bootstrapPaths, groupId)) {
            throw new IllegalArgumentException("Provide correct arrguments");
        } else {
            Properties props = new Properties();
            props.put("group.id", groupId);
            props.put("zookeeper.session.timeout.ms", "10000");
            props.put("zookeeper.sync.time.ms", "3000");
            props.put("auto.commit.interval.ms", "20000");

            props.put("enable.auto.commit", "false");
            props.put("key.deserializer", StringDeserializer.class.getName());
            props.put("value.deserializer", StringDeserializer.class.getName());
            props.put("bootstrap.servers", CommonUtils.StringListToCommaSeperated(bootstrapPaths));

            this.kafkaConsumer = new KafkaConsumer<String, String>(props);
            consumeManual(topicArray);
        }
    }

    private void consumeManual(ArrayList<String> topicArray) {
        if (null != kafkaConsumer) {
            Thread conThread = new Thread(new KafkaConsumerImpl(kafkaConsumer, topicArray, this));
            conThread.start();
        } else {
            throw new IllegalArgumentException(
                    "Initialize Kafka Consumer first using.- > new KafkaConsumerBase().initialize(args..)");
        }
    }

    /**
     * Subclass should Implements this method
     *
     * @param topic
     * @param output
     */
    public boolean KafkaConsumerOutput(String topic, String output) {
        logger.error("Please Extend KafkaConsumerBase class to get data in Custom defined consumer");
        return false;
    }

    private boolean validateInitializingComponents(ArrayList<String> zookeeperPaths, String groupId) {

        if (zookeeperPaths.size() > 0) {
            for (String url : zookeeperPaths) {
                if (!CommonUtils.validateUrl(url)) {
                    return false;
                }
            }
        }
        if (null == groupId || groupId.length() == 0) {
            return false;
        }
        return true;
    }


}
