package com.lcwd.electro.store.services;

import com.lcwd.electro.store.dto.CreateOrderRequest;
import com.lcwd.electro.store.dto.OrderDto;
import com.lcwd.electro.store.dto.PageableResponse;
import com.lcwd.electro.store.dto.UpdateOrderDto;

import java.util.List;

public interface OrderService {
    //create order
    OrderDto createOrder(CreateOrderRequest orderDto);
    //remove order
    void removeOrder(String orderId);
    //get order of user
    List<OrderDto> getOrdersOfUser(String userId);


    //get orders for admin
    PageableResponse<OrderDto> getOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    OrderDto updateOrder(UpdateOrderDto updateOrderDto);
}
