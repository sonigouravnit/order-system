package com.gourav.controller;

import com.gourav.models.Orders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by gouravsoni on 23/01/18.
 */
@RestController
@RequestMapping("/v1/order")
public class OrderController {

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateApplication(@RequestBody Orders orderDto) {
    }
}
