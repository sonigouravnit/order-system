package com.gourav.kafka.consumers;

import com.gourav.config.ConsumerBase;
import com.gourav.config.ConsumerBaseInterface;
import com.gourav.email.EmailUtil;
import com.gourav.models.Orders;
import com.gourav.repository.OrderRepository;
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
public class EmailConsumer implements ConsumerBaseInterface {

    private static final Logger logger = LoggerFactory.getLogger(EmailConsumer.class);

    @Value("${kafkaTopics.emailTopic:#{NULL}}")
    String emailTopic;

    @Autowired
    private ConsumerBase consumerBase;


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailUtil emailUtil;

    @Override
    @PostConstruct
    public void addTopicToConsumer() {
        consumerBase.addTopicToConsumer(emailTopic, this);
    }

    @Override
    public boolean handleResponse(Object output) {
        try {
            sendEmail(output + "");
            logger.info("Email - sent for invoiceId {}", output);
            return true;
        } catch (Exception e) {
            logger.error("Error while sending email for invoice {}", output, e);
            return false;
        }
    }

    private void sendEmail(String invoiceId) throws Exception {
        Orders order = orderRepository.findOne(invoiceId);
        if (null != order) {
            if (!order.isEmailSent()) {
                emailUtil.sendEmail(order.getEmailAddress(), order.getInvoiceLocation());
                order.setEmailSent(true);
                orderRepository.save(order);
            }
        } else {
            logger.error("Invoice record not present in Order table {} ", invoiceId);
        }
    }

}
