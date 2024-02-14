package com.lcwd.electro.store.repositories;

import com.lcwd.electro.store.entities.Cart;
import com.lcwd.electro.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart,String> {
    Optional<Cart> findByUser(User user);
}
