package com.lcwd.electro.store.services.implementation;

import com.lcwd.electro.store.dto.PageableResponse;
import com.lcwd.electro.store.dto.userDto;
import com.lcwd.electro.store.entities.Role;
import com.lcwd.electro.store.entities.User;
import com.lcwd.electro.store.exceptions.ResourceNotFoundException;
import com.lcwd.electro.store.helper.Helper;
import com.lcwd.electro.store.repositories.RoleRepository;
import com.lcwd.electro.store.repositories.UserRepository;
import com.lcwd.electro.store.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
@Autowired
   private UserRepository userRepository;
@Autowired
private ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;
@Autowired
private PasswordEncoder passwordEncoder;
    @Value("${admin.role.id}")
    String role_admin_id;
    @Value("${normal.role.id}")
    String role_normal_id;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    @Override
    public userDto createUser(userDto userDto) {
        String s = UUID.randomUUID().toString();
        userDto.setUserId(s);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user=dtoToEntity(userDto);
        
        //fetch role of normal user
        Role role = roleRepository.findById(role_normal_id).get();
       log.info("Role: {}",role.getRoleID());
        user.getRoles().add(role);
        User save = userRepository.save(user);
        userDto newDto=entityToDto(save);
        return newDto;
    }

    @Override
    public userDto updateUser(userDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found with Given Id."));
    user.setName(userDto.getName());
    user.setAbout(userDto.getAbout());
    user.setGender(userDto.getGender());
    user.setPassword(userDto.getPassword());
    user.setImageName(userDto.getImageName());

    User updatedUser= userRepository.save(user);

return entityToDto(updatedUser);
    }

    @Override
    public void deleteUser(String userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        //delete user image
        String fullPath = imageUploadPath + user.getImageName();
        try{
        Path path= Paths.get(fullPath);
        Files.delete(path);}
        catch (NoSuchFileException e){

            log.info("User Image not found in folder.");
        e.printStackTrace();
        }
        //delete user
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<userDto> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<userDto> response = Helper.getPageResponse(page, userDto.class);
        return response;
    }

    @Override
    public userDto getSingleUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return entityToDto(user);
    }

    @Override
    public userDto getUserByEmail(String userEmail) {
        User byEmail = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return entityToDto(byEmail);
    }

    @Override
    public List<userDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<userDto> userDtos = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public Optional<User> findUserByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    private User dtoToEntity(userDto userDto) {
//        User build = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .gender(userDto.getGender())
//                .about(userDto.getAbout())
//                .imageName(userDto.getImageName()).build();
        return modelMapper.map(userDto,User.class);
    }

    private userDto entityToDto(User save) {
//        userDto build = userDto.builder()
//                .userId(save.getUserId())
//                .name(save.getName())
//                .email(save.getEmail())
//                .password(save.getPassword())
//                .gender(save.getGender())
//                .about(save.getAbout())
//                .imageName(save.getImageName())
//                .build();
       return modelMapper.map(save,userDto.class);

    }



}
