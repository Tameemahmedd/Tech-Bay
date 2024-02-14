package com.lcwd.electro.store.repositories;

import com.lcwd.electro.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail(String userEmail);
    Optional<User> findByEmailAndPassword(String email,String password);
    List<User> findByNameContaining(String keyword);

    Optional<User> findByName(String name);

}
