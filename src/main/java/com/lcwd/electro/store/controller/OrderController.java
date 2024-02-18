package com.lcwd.electro.store.controller;

import com.lcwd.electro.store.dto.*;
import com.lcwd.electro.store.services.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@SecurityRequirement(name="scheme1")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest){
        OrderDto dto = orderService.createOrder(createOrderRequest);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Order is deleted")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        return new ResponseEntity<>(ordersOfUser,HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber, @RequestParam(value = "pageSize",defaultValue = "10",required =false) Integer pageSize, @RequestParam(value = "sortBy",defaultValue = "orderedDate",required =false) String sortBy, @RequestParam(value = "sortDir",defaultValue = "desc",required =false) String sortDir){
        PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orders,HttpStatus.FOUND);
    }

    @PutMapping
    public ResponseEntity<OrderDto> updateOrder(@RequestBody UpdateOrderDto updateOrderDto){
        OrderDto orderDto = orderService.updateOrder(updateOrderDto);
        return new ResponseEntity<>(orderDto,HttpStatus.OK);
    }


}
