package com.lcwd.electro.store.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
@Builder
public class User implements UserDetails {
    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;
    @Column(name="user_name")
    private String name;
    @Column(name="user_email",unique = true)
    private String email;
    @Column(name="user_password",length=1000)
    private String password;
    private String gender;
    @Column(length = 1000)
    private String about;
    @Column(name="user_image_name")
    private String imageName;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Order> orders=new ArrayList<>();
@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Role> roles=new HashSet<>();
@OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
private Cart cart;
//private String provider;
    @Override //TODO
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());
        return  authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}