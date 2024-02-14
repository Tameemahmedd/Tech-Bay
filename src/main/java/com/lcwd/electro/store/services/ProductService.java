package com.lcwd.electro.store.services;

import com.lcwd.electro.store.dto.PageableResponse;
import com.lcwd.electro.store.dto.ProductDto;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto,String productId);

    void delete(String productId) throws IOException;

    ProductDto getSingleProduct(String productId);

    PageableResponse<ProductDto> getAll(Integer pageNumber,Integer pageSize,String sortDir,String sortBy);

    PageableResponse<ProductDto> getAllLive(Integer pageNumber,Integer pageSize,String sortDir,String sortBy);

    PageableResponse<ProductDto> searchByTitle(String subTitle,Integer pageNumber,Integer pageSize,String sortDir,String sortBy);

    ProductDto createWithCategory(ProductDto productDto,String categoryId);

    ProductDto updateWithCategory(String productId,String categoryId);


    PageableResponse<ProductDto> getAllOfCategory(String categoryId,Integer pageNumber,Integer pageSize,String sortDir,String sortBy);
}
