package com.assignment.customerservice.service;

import com.assignment.customerservice.dto.CustomerRequest;
import com.assignment.customerservice.dto.CustomerResponse;
import com.assignment.customerservice.entity.Customer;
import com.assignment.customerservice.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final EntityManager entityManager;

    public void createCustomer(CustomerRequest customerRequest){
        Customer customer = Customer.builder()
                .customerAddress(customerRequest.getCustomerAddress())
                .customerPhone(customerRequest.getCustomerPhone())
                .customerFirstName(customerRequest.getCustomerFirstName())
                .customerLastName(customerRequest.getCustomerLastName())
                .build();

        customerRepository.save(customer);

        log.info("Customer {} is saved",customer.getCustomerId());
    }

    public List<CustomerResponse> getAllCustomers() {
//        Session session = entityManager.unwrap(Session.class);
//        Filter filter = session.enableFilter("deletedCustomerFilter");
//        filter.setParameter("isDeleted", isDeleted);
        List<Customer> customers =  customerRepository.findAll();
//        session.disableFilter("deletedCustomerFilter");
        return customers.stream().map(this::entityToResponse).toList();
    }

    private CustomerResponse entityToResponse(Customer customer) {
        return CustomerResponse.builder()
                .customerAddress(customer.getCustomerAddress())
                .customerFirstName(customer.getCustomerFirstName())
                .customerLastName(customer.getCustomerLastName())
                .customerPhone(customer.getCustomerPhone())
                .customerId(customer.getCustomerId())
                .deleted(customer.isDeleted())
                .build();
    }

    public Customer updateCustomer(long id, Customer customerReqObj){
        Optional<Customer> customerDataFromRepo = customerRepository.findById(id);
        Customer savedCustomer= null;

        if (customerDataFromRepo.isPresent()){
            Customer customer = customerDataFromRepo.get();
            customer.setCustomerAddress(customerReqObj.getCustomerAddress());
            customer.setCustomerPhone(customerReqObj.getCustomerPhone());
            customer.setCustomerFirstName(customerReqObj.getCustomerFirstName());
            customer.setCustomerLastName(customerReqObj.getCustomerLastName());
            savedCustomer = customerRepository.save(customer);
        }

            return savedCustomer;
    }

    public void remove(Long id){
        customerRepository.deleteById(id);
    }


    public List<Customer> findAll(Specification<Customer> spec) {
        return customerRepository.findAll(spec);
    }

    public Customer getCustomerById(long id) {
        Customer customer = new Customer();
        Optional<Customer> customerOpt = customerRepository.findById(id);
        if (customerOpt.isPresent())    customer = customerOpt.get();
        return customer;
    }

    public List<Customer> searchFilter(Map<String,String> filter){
        List<Customer> customerList = new ArrayList<>();

        filter.forEach((k,v) -> {
            switch (k){
                case "customerFirstName" -> customerList.addAll(customerRepository.findByCustomerFirstName(v));
                case "customerLastName" -> customerList.addAll(customerRepository.findByCustomerLastName(v));
                case "customerPhone" -> customerList.addAll(customerRepository.findByCustomerPhone(Integer.getInteger(v)));
                case "customerAddress" -> customerList.addAll(customerRepository.findByCustomerAddress(v));
                default -> throw new IllegalArgumentException("Unknown operator filter");
            }
        });

        return customerList;
    }
}
