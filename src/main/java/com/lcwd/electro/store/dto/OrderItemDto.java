package com.lcwd.electro.store.dto;

import com.lcwd.electro.store.entities.Order;
import com.lcwd.electro.store.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    private Integer orderItemId;
    private Integer quantity;
    private Double totalPrice;
    private ProductDto product;
    //private Order order;
}
