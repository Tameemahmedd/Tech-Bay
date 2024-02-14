package com.lcwd.electro.store.dto;

import com.lcwd.electro.store.entities.Cart;
import com.lcwd.electro.store.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private Integer cartItemId;
    private ProductDto product;
    private  Integer quantity;
    private Double totalPrice;

}
