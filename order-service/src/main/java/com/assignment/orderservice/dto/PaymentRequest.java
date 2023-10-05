package com.assignment.orderservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PaymentRequest {
    private double price;
    private String currency;
    private String method;
    private String intent;
    private String description;
}
