package com.gourav.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by gouravsoni on 23/01/18.
 */
@Component
public class KafkaProducers {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducers.class);

    @Value("{kafka.brokerList}")
    private String kafkaBrokerList;

    @Autowired
    private ObjectMapper objectMapper;

    public KafkaProducers() {

    }

    public void pushData(Object data, String topic) throws Exception {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaBrokerList);
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("request.required.acks", "1"); // ack required
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        try {
            ProducerRecord<String, String> producerRecord;
            producerRecord = new ProducerRecord<>(topic, null, objectMapper.writeValueAsString(data));
            producer.send(producerRecord);
            logger.info(topic + " - sent " + objectMapper.writeValueAsString(data));
            producer.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
