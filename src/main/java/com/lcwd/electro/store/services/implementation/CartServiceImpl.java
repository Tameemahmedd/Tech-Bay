package com.lcwd.electro.store.services.implementation;

import com.lcwd.electro.store.dto.AddItemtoCartRequest;
import com.lcwd.electro.store.dto.CartDto;
import com.lcwd.electro.store.entities.Cart;
import com.lcwd.electro.store.entities.CartItem;
import com.lcwd.electro.store.entities.Product;
import com.lcwd.electro.store.entities.User;
import com.lcwd.electro.store.exceptions.BadApiRequestException;
import com.lcwd.electro.store.exceptions.ResourceNotFoundException;
import com.lcwd.electro.store.repositories.CartItemRepository;
import com.lcwd.electro.store.repositories.CartRepository;
import com.lcwd.electro.store.repositories.ProductRepository;
import com.lcwd.electro.store.repositories.UserRepository;
import com.lcwd.electro.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Override
    public CartDto addItemToCart(String userId, AddItemtoCartRequest request) {

        Integer quantity = request.getQuantity();
        String productId = request.getProductId();
        if(quantity<=0){
            throw new BadApiRequestException("Requested Quantity is not valid.");
        }
        //fetch product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id."));
        //fetch user from db.
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id."));
        Cart cart = null;
        try
        {
            cart=cartRepository.findByUser(user).get();
        }
        catch(NoSuchElementException e){
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedDate(new Date());
        }

        //cart operations

        //if cart items are already then update them.
        AtomicReference<Boolean> updated=new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        items = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotalPrice(quantity* product.getPrice());
                updated.set(true);
            }

            return item;
        }).collect(Collectors.toList());
//cart.setItems(updatedItems);


        //create items
        if(!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);


    }

    @Override
    public void removeItemFromCart(String userId, Integer cartItemId) {

        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found with given id."));
        cartItemRepository.delete(item);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id."));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart with given user not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }
    @Override
    public CartDto getCartByUser(String userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id."));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart with given user not found"));
        return mapper.map(cart, CartDto.class);
    }
}
