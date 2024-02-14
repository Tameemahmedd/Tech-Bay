package com.lcwd.electro.store.services;

import com.lcwd.electro.store.dto.CategoryDto;
import com.lcwd.electro.store.dto.PageableResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface CategoryService {

    CategoryDto create (CategoryDto categoryDto);
    CategoryDto update(CategoryDto categoryDto,String categoryId);
    void delete(String categoryId) throws IOException;
    //Integer pageNumber, Integer pageSize, String sortBy, String sortDir
    PageableResponse<CategoryDto> getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    CategoryDto getSingleCategory(String categoryId);

    List<CategoryDto> searchCategory(String keyword);
}
