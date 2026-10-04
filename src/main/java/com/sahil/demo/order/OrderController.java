package com.sahil.demo.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /** Demonstrates the failure mode described in the case study. */
    @GetMapping("/broken/{id}")
    public PurchaseOrder broken(@PathVariable Long id) {
        return orderService.brokenFind(id);
    }

    /** Demonstrates the corrected implementation. */
    @GetMapping("/{id}")
    public OrderResponse fixed(@PathVariable Long id) {
        return orderService.fixedFind(id);
    }
}
