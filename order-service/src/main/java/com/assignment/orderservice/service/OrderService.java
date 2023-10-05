package com.assignment.orderservice.service;

import com.assignment.orderservice.dto.*;
import com.assignment.orderservice.entity.OrderEntity;
import com.assignment.orderservice.entity.OrderItemsEntity;
import com.assignment.orderservice.repository.OrderItemRepository;
import com.assignment.orderservice.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final EntityManager entityManager;
    private final OrderItemRepository orderItemRepository;

    @Value("${url.payment-service}")
    private String paymentServiceURL;

    public String placeOrder(OrderRequest orderRequest){

        OrderEntity orderEntity = new OrderEntity();
        List<OrderItemsEntity> orderItemsEntities = orderRequest.getOrderItemDtoList().stream().map(this::reqToDto).toList();
        orderEntity.setOrderItems(orderItemsEntities);
        orderEntity.setAmount(orderRequest.getAmount());
        orderEntity.setDiscount(orderRequest.getDiscount());
        orderEntity.setCustomerId(orderRequest.getCustomerId());

        List<String> skuCodes = orderRequest.getOrderItemDtoList().stream()
                .map(OrderItemDto::getSkuCode).toList();

        //call Inventory service and place order if product is in stock
        StockResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes)
                                .build())
                .retrieve()
                .bodyToMono(StockResponse[].class)
                .block();
        boolean allItemsInStock = Arrays.stream(inventoryResponseArray).allMatch(StockResponse::isInStock);

        if (allItemsInStock){
//            Customer customerResponse = webClientBuilder.build().get()
//                    .uri("http://customer-service/api/customer/getById",
//                            uriBuilder -> uriBuilder.queryParam("id",orderRequest.getCustomerId()).build())
//                    .retrieve()
//                    .bodyToMono(Customer.class)
//                    .block();
//            // to do check for customer is there or not if there save the id else create customer and then save the id here
//
//            if (customerResponse.getCustomerId() != null){
//                orderRepository.save(orderEntity);
//            }else{
//                throw new IllegalArgumentException("Customer is not registered!");
//            }
            OrderEntity savedOrder = orderRepository.save(orderEntity);

            PaymentRequest paymentRequest = PaymentRequest.builder()
                    .currency("USD")
                    .description("testing")
                    .intent("sale")
                    .price(0.1)
                    .method("paypal")
                    .build();

            String paymentResponse = webClientBuilder.build().post().uri(paymentServiceURL)
                    .body(BodyInserters.fromValue(paymentRequest))
                    .retrieve().bodyToMono(String.class).block();
            return paymentResponse;
        }else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }


    }

    public List<OrderResponse> getAllOrders() {
//        Session session = entityManager.unwrap(Session.class);
//        Filter filter = session.enableFilter("deletedOrderFilter");
//        filter.setParameter("isDeleted", isDeleted);
        List<OrderEntity> orders =  orderRepository.findAll();
//        session.disableFilter("deletedOrderFilter");
        return orders.stream().map(this::entityToResponse).toList();
    }

    private OrderResponse entityToResponse(OrderEntity order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .amount(order.getAmount())
                .discount(order.getDiscount())
                .orderItems(order.getOrderItems())
                .deleted(order.isDeleted())
                .build();
    }

    public OrderEntity updateOrder(long id, OrderEntity orderReqObj){
        Optional<OrderEntity> orderDataFromRepo = orderRepository.findById(id);
        OrderEntity savedOrder= null;
        List<OrderItemsEntity> orderItemsEntities = new ArrayList<>();

        if (orderDataFromRepo.isPresent()){
            OrderEntity order = orderDataFromRepo.get();
            order.setCustomerId(orderReqObj.getCustomerId());
            order.setDiscount(orderReqObj.getDiscount());
            order.setAmount(orderReqObj.getAmount());
            order.setOrderItems(orderReqObj.getOrderItems());
            savedOrder = orderRepository.save(order);

            orderItemsEntities = order.getOrderItems();
            List<OrderItemsEntity> updatedOrderItems = orderItemsEntities.stream().map(this::updateOrderItem).toList();
            savedOrder.setOrderItems(updatedOrderItems);
        }

        return savedOrder;
    }

    public void remove(Long id){
        orderRepository.deleteById(id);
    }


    public List<OrderEntity> findAll(Specification<OrderEntity> spec) {
        return orderRepository.findAll(spec);
    }

    private OrderItemsEntity reqToDto(OrderItemDto orderItemDto) {
        return OrderItemsEntity.builder()
                .skuCode(orderItemDto.getSkuCode())
                .price(orderItemDto.getPrice())
                .quantity(orderItemDto.getQuantity())
                .amount(orderItemDto.getAmount())
                .discount(orderItemDto.getDiscount())
                .build();
    }

    private OrderItemsEntity updateOrderItem(OrderItemsEntity orderItemsEntity){
        Optional<OrderItemsEntity> orderItemOpt = orderItemRepository.findById(orderItemsEntity.getOrderItemId());
        OrderItemsEntity updatedOrderItem = new OrderItemsEntity();
        if (orderItemOpt.isPresent()){
            OrderItemsEntity orderItemFromRepo = orderItemOpt.get();
            orderItemFromRepo.setAmount(orderItemsEntity.getAmount());
            orderItemFromRepo.setDiscount(orderItemsEntity.getDiscount());
            orderItemFromRepo.setPrice(orderItemsEntity.getPrice());
            orderItemFromRepo.setQuantity(orderItemsEntity.getQuantity());
            orderItemFromRepo.setSkuCode(orderItemsEntity.getSkuCode());
            updatedOrderItem = orderItemRepository.save(orderItemFromRepo);
        }
        return updatedOrderItem;
    }

    public List<OrderEntity> searchFilter(Map<String,String> filter){
        List<OrderEntity> orderEntityList = new ArrayList<>();

        filter.forEach((k,v) -> {
            switch (k){
                case "discount" -> orderEntityList.addAll(orderRepository.findByDiscount(Double.valueOf(v)));
                case "amount" -> orderEntityList.addAll(orderRepository.findByAmount(Double.valueOf(v)));
                case "customerId" -> orderEntityList.addAll(orderRepository.findByCustomerId(Long.valueOf(v)));
                case "deleted" -> orderEntityList.addAll(orderRepository.findByDeleted(Boolean.parseBoolean(v)));
                default -> throw new IllegalArgumentException("Unknown operator filter");
            }
        });

        return orderEntityList;
    }
}
