package com.lcwd.electro.store.services;

import com.lcwd.electro.store.dto.AddItemtoCartRequest;
import com.lcwd.electro.store.dto.CartDto;

public interface CartService {
//add items to cart
//case1: if cart is not available then create a cart for the user
//case2:if cart is available then add items to the cart

    CartDto addItemToCart(String userId, AddItemtoCartRequest addItemtoCartRequest);

    void removeItemFromCart(String userId,Integer cartItem);

    void clearCart(String userId);

CartDto getCartByUser(String userId);



}
