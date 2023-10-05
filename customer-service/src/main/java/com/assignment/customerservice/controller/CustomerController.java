package com.assignment.customerservice.controller;

import com.assignment.customerservice.dto.CustomerRequest;
import com.assignment.customerservice.dto.CustomerResponse;
import com.assignment.customerservice.entity.Customer;
import com.assignment.customerservice.service.CustomerService;
import com.assignment.customerservice.specfication.CustomerSpecificationsBuilder;
import com.assignment.customerservice.specfication.SearchOperation;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRequest customerRequest){
        customerService.createCustomer(customerRequest);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("status", 201);
        map.put("message", "Record is Saved Successfully!");
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCustomers(){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<CustomerResponse> customers = customerService.getAllCustomers();
        if (!customers.isEmpty()){
            map.put("status", 200);
            map.put("customers", customers);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else{
            map.clear();
            map.put("status", 400);
            map.put("message", "customers not found");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            Customer savedCustomer = customerService.updateCustomer(id, customer);
            map.put("status", 204);
            map.put("savedCustomer", savedCustomer);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch (Exception exception){
            map.clear();
            map.put("status", 400);
            map.put("message", "Unable to update");
            return new ResponseEntity<>(map, HttpStatus.NOT_MODIFIED);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") long id) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            customerService.remove(id);
            map.put("status", 200);
            map.put("message", "Record is deleted successfully!");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            map.clear();
            map.put("status", 400);
            map.put("message", "Data is not deleted");
            return new ResponseEntity<>(map, HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/searchCustomer/spec")
    @ResponseBody
    public List<Customer> findAllBySpecification(@RequestParam(value = "search") String search) {
        CustomerSpecificationsBuilder builder = new CustomerSpecificationsBuilder();
        String operationSetExper = Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
        }

        Specification<Customer> spec = builder.build();
        return customerService.findAll(spec);

    }

    @GetMapping("/getById/{id}")
    public Customer getCustomerById(@PathVariable("id") long id){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Customer customer = customerService.getCustomerById(id);
        return customer;
    }

    @GetMapping("/filterSearch")
    public ResponseEntity<?> searchFilter(@RequestBody Map<String,String> body){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<Customer> customers = customerService.searchFilter(body);
        if (!customers.isEmpty()){
            map.put("status", 200);
            map.put("customers", customers);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else{
            map.clear();
            map.put("status", 400);
            map.put("message", "customers is not found");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }

}
