package com.assignment.orderservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    private Long orderItemId;
    private String skuCode;
    private Integer quantity;
    private Double price;
    private Double amount;
    private Double discount;
}
