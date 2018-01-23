package com.gourav.service;

import com.gourav.models.Orders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gouravsoni on 23/01/18.
 */
public interface OrderService {
    @Transactional
    void processOrder(Orders orders);
}
