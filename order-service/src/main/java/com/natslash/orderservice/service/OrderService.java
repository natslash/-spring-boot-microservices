package com.natslash.orderservice.service;

import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natslash.orderservice.dto.OrderLineItemsDto;
import com.natslash.orderservice.dto.OrderRequest;
import com.natslash.orderservice.dto.OrderResponse;
import com.natslash.orderservice.model.Order;
import com.natslash.orderservice.model.OrderLineItems;
import com.natslash.orderservice.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                    .stream()
                    .map(this::mapToDto)
                    .toList();
        order.setOrderLineItemsList(orderLineItems);

        orderRepository.save(order);
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
            OrderLineItems orderLineItems = new OrderLineItems();
            orderLineItems.setPrice(orderLineItemsDto.getPrice());
            orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
            orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
            return orderLineItems;
    }
}
