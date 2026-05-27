package com.jde.portal.controller;

import com.jde.portal.dto.SalesOrderDto;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*") // Allows your React app to connect without CORS errors
public class SalesOrderApiController {

    // Using a Queue allows multiple users to submit without overwriting data
    private static final ConcurrentLinkedQueue<SalesOrderDto> orderQueue = new ConcurrentLinkedQueue<>();

    // 1. Frontend submits data here
    @PostMapping("/create")
    public String receiveOrder(@RequestBody SalesOrderDto orderDto) {
        orderQueue.add(orderDto);
        return "Order queued successfully";
    }

    // 2. Orchestrator Connector pulls data from here
    @GetMapping("/get-pending")
    public SalesOrderDto getPendingOrder() {
        return orderQueue.poll(); // Returns and removes the oldest order
    }
}