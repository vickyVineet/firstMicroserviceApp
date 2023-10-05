package com.assignment.orderservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Customer {
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private Long customerPhone;
    private String customerAddress;
    private boolean deleted = Boolean.FALSE;
}
