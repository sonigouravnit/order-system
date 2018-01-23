package com.gourav.controller;

import com.gourav.models.Orders;
import com.gourav.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by gouravsoni on 23/01/18.
 */
@RestController
@RequestMapping("/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateApplication(@RequestBody Orders orderDto) {
        orderService.processOrder(orderDto);
    }
}
