package com.lcwd.electro.store.repositories;

import com.lcwd.electro.store.entities.Category;
import com.lcwd.electro.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {

    Page<Product> findByProductNameContainingIgnoreCase(String subTitle,Pageable pageable);
    Page<Product> findByLiveTrue(Pageable pageable);


    Page<Product> findByCategory(Category category, Pageable page);
}
