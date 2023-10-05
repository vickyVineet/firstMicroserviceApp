package com.assignment.customerservice.service;

import com.assignment.customerservice.entity.Customer;
import com.assignment.customerservice.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EntityManager entityManager;

    @Test
    @DisplayName("Should find customer by id")
    void getCustomerById(){

        CustomerService customerService = new CustomerService(customerRepository, entityManager);

        Customer customer = new Customer(123L,"Vineet","Anand",9386940060L,"Ranchi, Jharkhand",false);
        Mockito.when(customerRepository.findById(123L)).thenReturn(Optional.of(customer));

        Customer actualResponse = customerService.getCustomerById(123L);

        Assertions.assertThat(actualResponse.getCustomerId()).isEqualTo(customer.getCustomerId());

    }
}