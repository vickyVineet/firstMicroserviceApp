package com.assignment.orderservice.repository;

import com.assignment.orderservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {
    @Query("SELECT o FROM OrderEntity o WHERE CONCAT(o.discount, ' ', o.amount, ' ', o.customerId, ' ', o.deleted) LIKE %?1%")
    public List<OrderEntity> search(String keyword);


    List<OrderEntity> findByDiscount(Double discount);

    List<OrderEntity> findByAmount(Double amount);

    List<OrderEntity> findByCustomerId(Long customerId);

    List<OrderEntity> findByDeleted(boolean deleted);
}
