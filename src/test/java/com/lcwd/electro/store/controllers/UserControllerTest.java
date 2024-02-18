package com.lcwd.electro.store.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electro.store.dto.PageableResponse;
import com.lcwd.electro.store.dto.userDto;
import com.lcwd.electro.store.entities.Role;
import com.lcwd.electro.store.entities.User;
import com.lcwd.electro.store.services.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Set;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

@MockBean
private UserService userService;
    User user;
    Role role;
@Autowired
    private ModelMapper mapper;
@Autowired
private MockMvc mockMvc;
@BeforeEach
public void init(){
    role=  Role.builder().roleName("NORMAL").roleID("abc").build();
    user= User.builder()
            .name("Ronaldo")
            .email("ronaldo@gmail.com")
            .about("I am a footballer")
            .gender("Male")
            .imageName("ronaldo.jpg")
            .password("123")
            .roles(Set.of(role))
            .build();
}
@Test
public void createUserTest() throws Exception {
userDto dto=mapper.map(user, userDto.class);
    Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);

//actual request to url
    this.mockMvc.perform(
            MockMvcRequestBuilders
                    .post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonString(user))
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").exists());




}
@Test
public void updateUserTest() throws Exception {
    String userId="111";
    userDto userDto= mapper.map(user, userDto.class);
    Mockito.when(userService.updateUser(Mockito.any(),Mockito.anyString())).thenReturn(userDto);

this.mockMvc.perform(MockMvcRequestBuilders.put("/users/"+userId)
                .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtb2hhbW1lZEBnbWFpbC5jb20iLCJpYXQiOjE3MDgyMjMxMTYsImV4cCI6MTcwODI0MTExNn0.gCDQ3kOqGgkOD4TV8mVlUPVOmMNwNvAoK1j1AZc1caQnyX3Z28eiJ5lC0aI9KYeIThf8dJ2Xn_qOqUdI7oVsHw")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertObjectToJsonString(user))
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").exists());

}
@Test
public void getAllUsersTest() throws Exception {

    userDto userDto1= userDto.builder()
            .name("Hp")
            .email("Hp@gmail.com")
            .about("I am a footballer")
            .gender("Male")
            .imageName("Hp.jpg")
            .password("123")
            .build();
    userDto userDto2= userDto.builder()
            .name("Dell")
            .email("Dell@gmail.com")
            .about("I am a footballer")
            .gender("Male")
            .imageName("Dell.jpg")
            .password("123")
            .build();

    PageableResponse<userDto> pageableResponse=new PageableResponse<>();
    pageableResponse.setContent(Arrays.asList(userDto1,userDto2));
    pageableResponse.setLastPage(false);
    pageableResponse.setPageSize(10);
    pageableResponse.setPageNumber(100);
    pageableResponse.setTotalElements(1000L);
    Mockito.when(userService.getAllUsers(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);
this.mockMvc.perform(
        MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
).andDo(print()).andExpect(status().isOk());

}


    private String convertObjectToJsonString(Object user) {
try{
return new ObjectMapper().writeValueAsString(user);

}
catch (Exception e){
    e.printStackTrace();
    return null;
}
}


}
