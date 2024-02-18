package com.lcwd.electro.store.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="categories")
@Builder
public class Category {
    @Id
    @Column(name = "category_id")
    private String categoryId;
    @Column(name="category_title", length=70,nullable = false)
    private String title;
    @Column(name="category_description",length = 500)
    private String description;
    private String coverImage;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> productList=new ArrayList<>();
}
