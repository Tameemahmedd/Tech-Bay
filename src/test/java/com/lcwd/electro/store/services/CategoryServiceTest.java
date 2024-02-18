package com.lcwd.electro.store.services;

import com.lcwd.electro.store.dto.CategoryDto;
import com.lcwd.electro.store.entities.Category;
import com.lcwd.electro.store.entities.Product;
import com.lcwd.electro.store.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class CategoryServiceTest {

    @MockBean
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CategoryService categoryService;
    Category category;
    Product product;
    @BeforeEach
    public void init(){

        category=Category.builder().title("Electronics")
                .coverImage("electronics.jpg")
                .description("This category belongs to electronics")
    .build();
      product= Product.builder()
                .productName("IPhone 13")
                .description("This is Iphone 13")
                .price(50000.0)
                .quantity(10)
                .live(true)
                .stock(true)
                .discountedPrice(49000)
                .productImageName("Iphone13.jpg")
                .category(category)
                .addedDate(new Date())
                .build();
      category.setProductList(List.of(product));
    }
@Test
    public void createCategory(){
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        CategoryDto categoryDto = categoryService.create(modelMapper.map(category, CategoryDto.class));
        Assertions.assertEquals(category.getTitle(),categoryDto.getTitle());
    }


}
