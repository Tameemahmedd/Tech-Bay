package com.lcwd.electro.store.repositories;

import com.lcwd.electro.store.entities.Order;
import com.lcwd.electro.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {
    List<Order> findByUser(User user);

}
