package com.lcwd.electro.store.services.implementation;

import com.lcwd.electro.store.dto.CreateOrderRequest;
import com.lcwd.electro.store.dto.OrderDto;
import com.lcwd.electro.store.dto.PageableResponse;
import com.lcwd.electro.store.dto.UpdateOrderDto;
import com.lcwd.electro.store.entities.*;
import com.lcwd.electro.store.exceptions.BadApiRequestException;
import com.lcwd.electro.store.exceptions.ResourceNotFoundException;
import com.lcwd.electro.store.helper.Helper;
import com.lcwd.electro.store.repositories.CartRepository;
import com.lcwd.electro.store.repositories.OrderRepository;
import com.lcwd.electro.store.repositories.UserRepository;
import com.lcwd.electro.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CartRepository cartRepository;
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        String userId= orderDto.getUserId();
        String cartId= orderDto.getCartId();
        //fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        //fetch cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart with given id not found"));
        List<CartItem> cartItems = cart.getItems();

        if(cartItems.size() == 0)
        {
            throw new BadApiRequestException("Invalid number of items in cart.");
        }
        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user).build();

        AtomicReference<Double> orderAmount=new AtomicReference<>(0.0);
        List<OrderItems> orderItems = cartItems.stream().map(cartItem ->
        {
            OrderItems orderItem = OrderItems.builder()
                    .quantity(cartItem. getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());
        //clear cart after creating order
        cart.getItems().clear();
        cartRepository.save(cart);
        Order saved = orderRepository.save(order);
        return mapper.map(saved,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order with given id not found."));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepository.findAll(pageable);
        return Helper.getPageResponse(page, OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(UpdateOrderDto updateOrderDto) {
        Order order = orderRepository.findById(updateOrderDto.getOrderId()).orElseThrow(() -> new ResourceNotFoundException("Order with id given id not found."));
        order.setOrderStatus(updateOrderDto.getOrderStatus());
        order.setPaymentStatus(updateOrderDto.getPaymentStatus());
        order.setDeliveredDate(new Date());
        Order saved = orderRepository.save(order);
        return mapper.map(saved, OrderDto.class);
    }
}
