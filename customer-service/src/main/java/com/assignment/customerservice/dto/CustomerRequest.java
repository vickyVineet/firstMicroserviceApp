package com.assignment.customerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
    private String customerFirstName;
    private String customerLastName;
    private Long customerPhone;
    private String customerAddress;
}
