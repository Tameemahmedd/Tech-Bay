package com.lcwd.electro.store.services;

import com.lcwd.electro.store.dto.PageableResponse;
import com.lcwd.electro.store.dto.userDto;
import com.lcwd.electro.store.entities.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface UserService {
    userDto createUser(userDto userDto);
    userDto updateUser(userDto userDto,String userId);
    void deleteUser(String userId) throws IOException;

    PageableResponse<userDto> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    userDto getSingleUser(String userId);
    userDto getUserByEmail(String userEmail);
    List<userDto> searchUser(String keyword);

    Optional<User> findUserByEmailOptional(String email);

}
