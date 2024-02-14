package com.lcwd.electro.store.repositories;

import com.lcwd.electro.store.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems,Integer> {
}
