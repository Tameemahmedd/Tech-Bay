package com.lcwd.electro.store.dto;

import com.lcwd.electro.store.entities.CartItem;
import com.lcwd.electro.store.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private String cartId;
    private Date createdDate;
    private userDto user;
    private List<CartItemDto> items=new ArrayList<>();

}
