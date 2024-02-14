package com.lcwd.electro.store.controller;

import com.lcwd.electro.store.dto.AddItemtoCartRequest;
import com.lcwd.electro.store.dto.ApiResponseMessage;
import com.lcwd.electro.store.dto.CartDto;
import com.lcwd.electro.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemsToCart(@RequestBody AddItemtoCartRequest request, @PathVariable String userId){
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> deleteItemFromCart(@PathVariable String userId, @PathVariable Integer cartItemId){
     cartService.removeItemFromCart(userId,cartItemId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("Item is removed successfully.").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){
        cartService.clearCart(userId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("Cart cleared successfully.").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart( @PathVariable String userId){
        CartDto cartByUser = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartByUser,HttpStatus.FOUND);
    }


}
