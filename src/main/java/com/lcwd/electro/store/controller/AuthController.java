package com.lcwd.electro.store.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lcwd.electro.store.dto.JwtRequest;
import com.lcwd.electro.store.dto.JwtResponse;
import com.lcwd.electro.store.dto.userDto;
import com.lcwd.electro.store.entities.User;
import com.lcwd.electro.store.exceptions.BadApiRequestException;
import com.lcwd.electro.store.security.JwtHelper;
import com.lcwd.electro.store.services.UserService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = {"Authorization"},methods = {RequestMethod.GET,RequestMethod.POST},
//maxAge = 3600)
@Api(value = "AuthenticationController",description = "API for authentication.")
public class AuthController {
    @Value("${googleClientId}")
    private String googleClientId;
    @Value("${newPassword}")
    private String newPassword;
    private Logger logger= LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;
    @GetMapping("/current")
    public ResponseEntity<userDto> getUser(Principal principal){
        String name = principal.getName();
        return new ResponseEntity<>(mapper.map(userDetailsService.loadUserByUsername(name), userDto.class), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest){
        this.doAuthenticate(jwtRequest.getUsername(),jwtRequest.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String s = this.jwtHelper.generateToken(userDetails);
        userDto userDto = mapper.map(userDetails, userDto.class);
        JwtResponse response = JwtResponse.builder().jwtToken(s).user(userDto).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private void doAuthenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(username,password);
        try{
            manager.authenticate(authentication);
        }catch (BadCredentialsException e){
            throw new BadApiRequestException("Invalid username or password.");
        }
    }

    //login with google api
    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String,Object> data) throws IOException {

        String idToken=data.get("idToken").toString();

        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));

        GoogleIdToken googleIdToken=GoogleIdToken.parse(verifier.getJsonFactory(), idToken);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        logger.info("Payload :{} ",payload);
        String email = payload.getEmail();

        User user=null;
        user = userService.findUserByEmailOptional(email).orElse(null);

        if(user==null)
        {
            //create new user
            user=this.saveUser(email,data.get("name").toString(),data.get("photoUrl").toString());
        }
         ResponseEntity<JwtResponse> response=this.login(JwtRequest.builder().username(user.getEmail()).password(newPassword).build());
        return response;
    }

    private User saveUser(String email, String name, String photoUrl) {
        userDto build = userDto.builder().name(name)
                .email(email)
                .imageName(photoUrl)
                .password(newPassword)
                .roles(new HashSet<>())
                .build();
        userDto user = userService.createUser(build);
        return mapper.map(user,User.class);
    }

}
