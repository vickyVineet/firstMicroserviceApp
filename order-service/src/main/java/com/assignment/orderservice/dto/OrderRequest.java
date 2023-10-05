package com.assignment.orderservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Double discount;
    private Double amount;
    private Long customerId;
    private List<OrderItemDto> orderItemDtoList;
}
