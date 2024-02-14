package com.lcwd.electro.store.controller;

import com.lcwd.electro.store.dto.ApiResponseMessage;
import com.lcwd.electro.store.dto.ImageResponse;
import com.lcwd.electro.store.dto.PageableResponse;
import com.lcwd.electro.store.dto.userDto;
import com.lcwd.electro.store.services.FileService;
import com.lcwd.electro.store.services.implementation.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Api(value = "UserController",description = "This is user REST APIs.")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private FileService fileService;

@Value("${user.profile.image.path}")
    private String imageUploadPath;
@PostMapping
@ApiOperation(value = "Create new user.")
@ApiResponses(value = {
        @ApiResponse(code=200,message = "Success | OK"),
        @ApiResponse(code=401,message = "Not Authorized"),
        @ApiResponse(code=201,message = "New user created.")
})
    public ResponseEntity<userDto> createUser(@RequestBody @Valid userDto userDto){
        userDto user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<userDto> updateUser( @Valid @RequestBody userDto userDto,@PathVariable("userId") String userId){
        userDto userDto1 = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(userDto1, HttpStatus.OK);
}
    @DeleteMapping("/{userId}")
public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId) throws IOException {
    userService.deleteUser(userId);
        ApiResponseMessage deleted = ApiResponseMessage.builder().message("Deleted").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(deleted,HttpStatus.OK);
}
@GetMapping
@ApiOperation(value="Get all Users",tags = {"user-controller","user apis"})
public ResponseEntity<PageableResponse<userDto>> getAllUsers(
        @RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber,
        @RequestParam(value = "pageSize",defaultValue = "10",required =false) Integer pageSize,
        @RequestParam(value = "sortBy",defaultValue = "name",required =false) String sortBy,
        @RequestParam(value = "sortDir",defaultValue = "asc",required =false) String sortDir
        ){
    return new ResponseEntity<>( userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
}
    @GetMapping("/{userId}")
    @ApiOperation(value = "Get single user by userId.")
public ResponseEntity<userDto> getSingleUser(@PathVariable String userId){
    return new ResponseEntity<>(userService.getSingleUser(userId),HttpStatus.OK );
    }

    @GetMapping("/email/{userEmail}")
    public ResponseEntity<userDto> getSingleUserByEmail(@PathVariable String userEmail){
        return new ResponseEntity<>(userService.getUserByEmail(userEmail),HttpStatus.OK );
    }

    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<userDto>> searchUser(@PathVariable String keywords){
        return new ResponseEntity<>(userService.searchUser(keywords),HttpStatus.OK );
    }

    //upload user Image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage")MultipartFile file,@PathVariable String userId) throws IOException {
        String s = fileService.uploadImage(file, imageUploadPath);

        userDto user = userService.getSingleUser(userId);
        user.setImageName(s);
        userService.updateUser(user,userId);

        ImageResponse imageResponse=ImageResponse.builder()
                .imageName(s).success(true).message("Image Uploaded Successfully.").status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);

    }
    @GetMapping("/image/{userId}")
    public void getUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        userDto user = userService.getSingleUser(userId);
        log.info("User Image Name :{}",user.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
