package com.lcwd.electro.store.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="products")
@Builder
public class Product {
    @Id
   private String productId;

    @NotBlank(message = "Product Name is mandatory.")
    private String productName;

    @Column(length = 10000)
    private String description;
@NotNull(message = "Please specify a price.")
    private Double price;

    private Integer discountedPrice;

    private Integer quantity;

    private Date addedDate;

    private boolean live;

    private boolean stock;

    private String productImageName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
}
