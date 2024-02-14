package com.lcwd.electro.store.dto;

import com.lcwd.electro.store.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.Date;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductDto {
    private String productId;
    private String productName;
    private String description;
    private Double price;
    private Integer discountedPrice;
    private Integer quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImageName;
    private CategoryDto category;
}
