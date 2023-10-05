package com.assignment.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE order_details SET deleted = true WHERE order_id=?")
@FilterDef(name = "deletedOrderFilter", parameters = @ParamDef(name = "isDeleted", type = org.hibernate.type.descriptor.java.BooleanJavaType.class
))
@Filter(name = "deletedOrderFilter", condition = "deleted = :isDeleted")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    @Column
    private Double discount;
    private Double amount;
    @Column(name = "customer_id")
    private Long customerId;
    @OneToMany(targetEntity = OrderItemsEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id_fk", referencedColumnName = "order_id")
    private List<OrderItemsEntity> orderItems;
    private boolean deleted = Boolean.FALSE;
}
