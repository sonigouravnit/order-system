package com.gourav.service.impl;

import com.gourav.kafka.KafkaProducers;
import com.gourav.models.Orders;
import com.gourav.repository.OrderRepository;
import com.gourav.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by gouravsoni on 23/01/18.
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Autowired
    private KafkaProducers kafkaProducers;

    @Value("{kafka.smsTopic}")
    private String smsTopic;

    @Value("{kafka.emailTopic}")
    private String emailTopic;

    @Value("{kafka.invoiceTopic}")
    private String invoiceTopic;

    @Autowired
    private OrderRepository orderRepository;

    public void processOrder(Orders orders) {
        try {
            String invoiceId = generateInvoiceId();
            orders.setInvoiceId(invoiceId);
            orderRepository.save(orders);


            // Sending only invoice id because in  real system components
            // can depend on other components as well

            kafkaProducers.pushData(invoiceId, smsTopic);
            kafkaProducers.pushData(invoiceId, emailTopic);
            kafkaProducers.pushData(invoiceId, invoiceTopic);

        } catch (Exception e) {
            logger.error("Exception while processing order {}", e.getMessage(), e);
        }

    }

    private String generateInvoiceId() {
        return UUID.randomUUID().toString();
    }
}
