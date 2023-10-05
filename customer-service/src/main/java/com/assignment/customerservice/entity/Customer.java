package com.assignment.customerservice.entity;

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
@Entity
@Table(name = "customer")
@SQLDelete(sql = "UPDATE customer SET deleted = true WHERE customer_id=?")
@FilterDef(name = "deletedCustomerFilter", parameters = @ParamDef(name = "isDeleted", type = org.hibernate.type.descriptor.java.BooleanJavaType.class
))
@Filter(name = "deletedCustomerFilter", condition = "deleted = :isDeleted")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "customer_first_name")
    private String customerFirstName;
    @Column(name = "customer_last_name")
    private String customerLastName;
    @Column(name = "customer_phone")
    private Long customerPhone;
    @Column(name = "customer_address")
    private String customerAddress;
    private boolean deleted = Boolean.FALSE;
}
