package com.gourav.kafka.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gourav.config.ConsumerBase;
import com.gourav.config.ConsumerBaseInterface;
import com.gourav.email.EmailUtil;
import com.gourav.models.Orders;
import com.gourav.pdf.Pdfutil;
import com.gourav.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * Created by gouravsoni on 23/01/18.
 */
@Component
public class InvoiceConsumer implements ConsumerBaseInterface {

    private static final Logger logger = LoggerFactory.getLogger(SmsConsumer.class);

    @Value("${kafkaTopics.invoiceTopic:#{NULL}}")
    String invoiceTopic;

    @Autowired
    private ConsumerBase consumerBase;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private Pdfutil pdfutil;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private EmailUtil emailUtil;

    @Override
    @PostConstruct
    public void addTopicToConsumer() {
        consumerBase.addTopicToConsumer(invoiceTopic, this);
    }

    @Override
    public boolean handleResponse(Object output) {
        try {

            genrateInvoice(output + "");
            return true;
        } catch (Exception e) {
            logger.error("Error while generating and sending Invoice for invoiceId {}", output, e);
            return false;
        }
    }

    private void genrateInvoice(String invoiceId) throws Exception {
        Orders order = orderRepository.findOne(invoiceId);

        if (null != order) {
            String filePath = pdfutil.generatePDF(order.getInvoiceId(), "Invoice for Order : " + order.getInvoiceId(), objectMapper.convertValue(order, HashMap.class));
            order.setInvoiceLocation(filePath);
            emailUtil.sendEmail(order.getEmailAddress(), order.getInvoiceLocation());
            order.setEmailSent(true);

            orderRepository.save(order);
        } else {
            logger.error("Invoice record not present in Order table {} ", invoiceId);
        }
    }
}
