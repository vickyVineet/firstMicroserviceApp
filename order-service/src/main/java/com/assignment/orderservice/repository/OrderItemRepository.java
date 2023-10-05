package com.assignment.orderservice.repository;

import com.assignment.orderservice.entity.OrderEntity;
import com.assignment.orderservice.entity.OrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemsEntity, Long>, JpaSpecificationExecutor<OrderItemsEntity> {
}
