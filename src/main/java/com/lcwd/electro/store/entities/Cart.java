package com.lcwd.electro.store.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="cart_table")
@Builder
public class Cart{
@Id
private String cartId;
private Date createdDate;
@OneToOne
@JoinColumn(name = "user_id")
private User user;
@OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
private List<CartItem> items=new ArrayList<>();


}
