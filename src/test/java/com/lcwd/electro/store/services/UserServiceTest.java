package com.lcwd.electro.store.services;

import com.lcwd.electro.store.dto.PageableResponse;
import com.lcwd.electro.store.dto.RoleDto;
import com.lcwd.electro.store.dto.userDto;
import com.lcwd.electro.store.entities.Role;
import com.lcwd.electro.store.entities.User;
import com.lcwd.electro.store.repositories.RoleRepository;
import com.lcwd.electro.store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    String roleId;
    User user;
    Role role;
    @BeforeEach
    public void init(){
      role=  Role.builder().roleName("NORMAL").roleID("abc").build();
user=User.builder()
        .name("Ronaldo")
        .email("ronaldo@gmail.com")
        .about("I am a footballer")
        .gender("Male")
        .imageName("ronaldo.jpg")
        .password("123")
        .roles(Set.of(role))
        .build();

roleId="abc";
    }
    //create user
    @Test
    public void createUserTest(){
            Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
            Mockito.when(roleRepository.findById(Mockito.any())).thenReturn(Optional.of(role));
            userDto userDto = userService.createUser(modelMapper.map(user, userDto.class));
            Assertions.assertNotNull(userDto);
        }

        @Test
        public void updateUserTest(){
        String userId="111";

        userDto userDto= com.lcwd.electro.store.dto.userDto.builder()
                    .name("Cristiano Ronaldo")
                    .about("This updated user.")
                    .gender("Male")
                    .imageName("xyz.jpg")
                    .build();

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

            com.lcwd.electro.store.dto.userDto userDto1 = userService.updateUser(userDto, userId);
            System.out.println(userDto1.getName());
            System.out.println(userDto1.getImageName());

            Assertions.assertNotNull(userDto1);
            Assertions.assertEquals(userDto.getName(),userDto1.getName());
        }
        @Test
       public  void deleteUserTest() throws IOException {
                String userId="111";
        Mockito.when(userRepository.findById("111")).thenReturn(Optional.of(user));
        userService.deleteUser(userId);

        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
        }
        @Test
        public void getAllUserTest(){
         User user1= User.builder()
                    .name("Hp")
                    .email("Hp@gmail.com")
                    .about("I am a footballer")
                    .gender("Male")
                    .imageName("Hp.jpg")
                    .password("123")
                    .roles(Set.of(role))
                    .build();
           User user2= User.builder()
                    .name("Dell")
                    .email("Dell@gmail.com")
                    .about("I am a footballer")
                    .gender("Male")
                    .imageName("Dell.jpg")
                    .password("123")
                    .roles(Set.of(role))
                    .build();

            List<User> userList= Arrays.asList(user,user1,user2);
            Page page= new PageImpl(userList);

        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

       PageableResponse<userDto> allUser= userService.getAllUsers(1,2,"name","asc");

       Assertions.assertEquals(3,allUser.getTotalElements());

        }

        @Test
        public void getUserByIdTest(){
        String userId="222";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            userDto user1 = userService.getSingleUser(userId);
            Assertions.assertEquals("Ronaldo",user1.getName());
            Assertions.assertNotNull(user1);
        }


        @Test
        public void getUserByEmail(){
        String email="ronaldo@gmail.com";
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));

            userDto userByEmail = userService.getUserByEmail(email);

            Assertions.assertEquals(user.getEmail(),userByEmail.getEmail());
        }

        @Test
        public void searchUserTest(){
            User user1= User.builder()
                    .name("Hpl")
                    .email("Hp@gmail.com")
                    .about("I am a footballer")
                    .gender("Male")
                    .imageName("Hp.jpg")
                    .password("123")
                    .roles(Set.of(role))
                    .build();
            User user2= User.builder()
                    .name("Dell")
                    .email("Dell@gmail.com")
                    .about("I am a footballer")
                    .gender("Male")
                    .imageName("Dell.jpg")
                    .password("123")
                    .roles(Set.of(role))
                    .build();

            List<User> userList=Arrays.asList(user,user1,user2);
            List<userDto> userDtos = userList.stream().map(user ->
                modelMapper.map(user, userDto.class)
            ).collect(Collectors.toList());
            String keywords="l";

            Mockito.when(userRepository.findByNameContaining(keywords)).thenReturn(userList);

            List<userDto> l = userService.searchUser("l");

            Assertions.assertIterableEquals(userDtos,l);

        }
        @Test
        public void findByEmailOptional(){
        String email="ronaldo@gmail.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

            Optional<User> userByEmailOptional = userService.findUserByEmailOptional(email);
        Assertions.assertTrue(userByEmailOptional.isPresent());
            User user1 = userByEmailOptional.get();
            Assertions.assertEquals(user,user1);
        }


}
