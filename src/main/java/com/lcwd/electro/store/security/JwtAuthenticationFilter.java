package com.lcwd.electro.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");
        log.info("Header :{} ",requestHeader);
        String username=null;
        String token=null;
        if(requestHeader!=null && requestHeader.startsWith("Bearer"))
        {
           token = requestHeader.substring(7);
            try{
                username= this.jwtHelper.getUsernameFromToken(token);
            }catch (IllegalArgumentException e){
                log.info("Illegal Argument when fetching the username.");
                e.printStackTrace();
            }catch (ExpiredJwtException e){
                log.info("Given JWT token is expired.");
                e.printStackTrace();
            }catch(MalformedJwtException e){
                log.info("Some changes has been made in the token. Invalid Token.");
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }



        }else log.info("Invalid Header Value.");

        //
        if(username!=null && SecurityContextHolder .getContext().getAuthentication()==null){

            //fetch user detail using username.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken =this.jwtHelper.validateToken(token, userDetails);
            if(validateToken){

                UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
                log.info("Validation failed.");
            }


        }
        filterChain.doFilter(request,response);

    }
}
