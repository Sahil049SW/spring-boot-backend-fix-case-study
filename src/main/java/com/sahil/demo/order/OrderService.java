package com.sahil.demo.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Intentionally broken example: the entity is returned after the repository
     * transaction has completed, leaving the lazy customer association detached.
     */
    public PurchaseOrder brokenFind(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    }

    /**
     * Fixed example: load and map the required fields while the persistence
     * context is active. The controller only receives a DTO.
     */
    @Transactional(readOnly = true)
    public OrderResponse fixedFind(Long id) {
        PurchaseOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomer().getName()
        );
    }
}
