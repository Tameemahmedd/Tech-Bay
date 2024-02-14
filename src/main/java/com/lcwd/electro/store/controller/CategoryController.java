package com.lcwd.electro.store.controller;

import com.lcwd.electro.store.dto.*;
import com.lcwd.electro.store.services.CategoryService;
import com.lcwd.electro.store.services.FileService;
import com.lcwd.electro.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private FileService fileService;
    @Value("${category.image.path}")
    private String imageUploadPath;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
@PostMapping
@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto categoryDto){
    CategoryDto categoryDto1 = categoryService.create(categoryDto);
return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
}
@PutMapping("/{categoryId}")
@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> update(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
return new ResponseEntity<>(categoryService.update(categoryDto,categoryId),HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String categoryId) throws IOException {

        categoryService.delete(categoryId);
        ApiResponseMessage deleted = ApiResponseMessage.builder().message("Deleted").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
@GetMapping
public ResponseEntity<PageableResponse<CategoryDto>> getAll( @RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber, @RequestParam(value = "pageSize",defaultValue = "10",required =false) Integer pageSize, @RequestParam(value = "sortBy",defaultValue = "title",required =false) String sortBy, @RequestParam(value = "sortDir",defaultValue = "asc",required =false) String sortDir){
    PageableResponse<CategoryDto> all = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
    return new ResponseEntity<>(all,HttpStatus.OK);
}
@GetMapping("/{categoryId}")
public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String categoryId){
return new ResponseEntity<>(categoryService.getSingleCategory(categoryId),HttpStatus.OK);
}

@GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchUsingKeyword(@PathVariable String keyword){
    return new ResponseEntity<>(categoryService.searchCategory(keyword),HttpStatus.FOUND);
}

@PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam MultipartFile file,@PathVariable String categoryId) throws IOException {
    String s = fileService.uploadImage(file, imageUploadPath);
    CategoryDto singleCategory = categoryService.getSingleCategory(categoryId);
    singleCategory.setCoverImage(s);
    categoryService.update(singleCategory,categoryId);
    ImageResponse response=ImageResponse.builder().imageName(s).message("Image is uploaded").success(true).status(HttpStatus.OK).build();
    return new ResponseEntity<>(response,HttpStatus.CREATED);
}

@GetMapping("/image/{categoryId}")
    public void getImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
    CategoryDto singleCategory = categoryService.getSingleCategory(categoryId);

    InputStream resource = fileService.getResource(imageUploadPath, singleCategory.getCoverImage());
    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    StreamUtils.copy(resource,response.getOutputStream());
}

//create product with categoryId
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId,@RequestBody ProductDto productDto){
        ProductDto productWithCategory = productService.createWithCategory(productDto, categoryId);
return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateProductWithCategory(@PathVariable String categoryId,@PathVariable String productId){
        ProductDto productDto = productService.updateWithCategory(productId, categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }


    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsWithCategory(@PathVariable String categoryId,@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber, @RequestParam(value = "pageSize",defaultValue = "10",required =false) Integer pageSize, @RequestParam(value = "sortBy",defaultValue = "productName",required =false) String sortBy, @RequestParam(value = "sortDir",defaultValue = "asc",required =false) String sortDir){
        PageableResponse<ProductDto> response = productService.getAllOfCategory(categoryId,pageNumber, pageSize, sortDir, sortBy);
        return new ResponseEntity<>(response,HttpStatus.FOUND);
    }






}
