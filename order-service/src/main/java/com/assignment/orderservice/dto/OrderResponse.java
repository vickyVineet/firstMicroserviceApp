package com.assignment.orderservice.dto;

import com.assignment.orderservice.entity.OrderItemsEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Double discount;
    private Double amount;
    private Long customerId;
    private List<OrderItemsEntity> orderItems;
    private boolean deleted;
}
