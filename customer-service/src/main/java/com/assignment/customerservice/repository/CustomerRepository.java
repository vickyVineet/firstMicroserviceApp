package com.assignment.customerservice.repository;

import com.assignment.customerservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    @Query("SELECT c FROM Customer c WHERE CONCAT(c.customerFirstName, ' ', c.customerLastName, ' ', c.customerPhone, ' ', c.customerAddress) LIKE %?1%")
    public List<Customer> search(String keyword);

    List<Customer> findByCustomerFirstName(String firstName);

    List<Customer> findByCustomerLastName(String lastName);

    List<Customer> findByCustomerPhone(Integer phone);


    List<Customer> findByCustomerAddress(String address);
}
