package com.assignment.customerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private Long customerPhone;
    private String customerAddress;
    private boolean deleted;
}
