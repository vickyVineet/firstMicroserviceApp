package com.assignment.orderservice.controller;

import com.assignment.orderservice.dto.OrderRequest;
import com.assignment.orderservice.dto.OrderResponse;
import com.assignment.orderservice.entity.OrderEntity;
import com.assignment.orderservice.service.OrderService;
import com.assignment.orderservice.specfication.OrderSpecificationsBuilder;
import com.assignment.orderservice.specfication.SearchOperation;
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
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest){
        String response = orderService.placeOrder(orderRequest);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("status", 201);
//        map.put("message", "Order placed successfully!");
        map.put("message", "Order placed successfully! Payment link:- "+response);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCustomers(){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<OrderResponse> orders = orderService.getAllOrders();
        if (!orders.isEmpty()){
            map.put("status", 200);
            map.put("orders", orders);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else{
            map.clear();
            map.put("status", 400);
            map.put("message", "orders is not found");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") long id, @RequestBody OrderEntity customer){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            OrderEntity savedOrder = orderService.updateOrder(id, customer);
            map.put("status", 200);
            map.put("savedCustomer", savedOrder);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch (Exception exception){
            map.clear();
            map.put("status", 400);
            map.put("message", "Unable to update");
            return new ResponseEntity<>(map, HttpStatus.NOT_MODIFIED);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") long id) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            orderService.remove(id);
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

    @RequestMapping(method = RequestMethod.GET, value = "/searchOrder/spec")
    @ResponseBody
    public List<OrderEntity> findAllBySpecification(@RequestParam(value = "search") String search) {
        OrderSpecificationsBuilder builder = new OrderSpecificationsBuilder();
        String operationSetExper = Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
        }

        Specification<OrderEntity> spec = builder.build();
        return orderService.findAll(spec);

    }

    @GetMapping("/filterSearch")
    public ResponseEntity<?> searchFilter(@RequestBody Map<String,String> body){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<OrderEntity> orders = orderService.searchFilter(body);
        if (!orders.isEmpty()){
            map.put("status", 200);
            map.put("orders", orders);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else{
            map.clear();
            map.put("status", 400);
            map.put("message", "orders is not found");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }


}
