package com.lcwd.electro.store.services.implementation;

import com.lcwd.electro.store.dto.CategoryDto;
import com.lcwd.electro.store.dto.PageableResponse;
import com.lcwd.electro.store.entities.Category;
import com.lcwd.electro.store.exceptions.ResourceNotFoundException;
import com.lcwd.electro.store.helper.Helper;
import com.lcwd.electro.store.repositories.CategoryRepository;
import com.lcwd.electro.store.services.CategoryService;
import com.lcwd.electro.store.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private ModelMapper modelMapper;

    @Value("${category.image.path}")
    private String imageUploadPath;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        categoryDto.setCategoryId(UUID.randomUUID().toString());
        Category category = modelMapper.map(categoryDto, Category.class);
        Category save = categoryRepository.save(category);

        return modelMapper.map(save,CategoryDto.class) ;
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        Category category1 = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id."));
        category1.setTitle(categoryDto.getTitle());
        category1.setDescription(categoryDto.getDescription());
        category1.setCoverImage(categoryDto.getCoverImage());
        Category save = categoryRepository.save(category1);
        return modelMapper.map(save,CategoryDto.class) ;
    }

    @Override
    public void delete(String categoryId) throws IOException {
        Category category1 = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id."));
String s=imageUploadPath+category1.getCoverImage();
try {
    Path path = Paths.get(s);
    Files.delete(path);
}catch (NoSuchFileException e){

    log.info("Category Image not found in folder.");
    e.printStackTrace();
}
        categoryRepository.delete(category1);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort=sortDir.equalsIgnoreCase("desc")?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> all = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageResponse = Helper.getPageResponse(all, CategoryDto.class);
    return pageResponse;
    }

    @Override
    public CategoryDto getSingleCategory(String categoryId) {
        Category category1 = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id."));
return modelMapper.map(category1, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        List<Category> categoryList = categoryRepository.findCategoriesByTitleContaining(keyword);
        List<CategoryDto> categoryDtoList = categoryList.stream().map(l -> modelMapper.map(l, CategoryDto.class)).collect(Collectors.toList());
        return categoryDtoList;
    }


}
