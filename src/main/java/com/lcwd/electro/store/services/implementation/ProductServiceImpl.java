package com.lcwd.electro.store.services.implementation;

import com.lcwd.electro.store.dto.PageableResponse;
import com.lcwd.electro.store.dto.ProductDto;
import com.lcwd.electro.store.entities.Category;
import com.lcwd.electro.store.entities.Product;
import com.lcwd.electro.store.exceptions.ResourceNotFoundException;
import com.lcwd.electro.store.helper.Helper;
import com.lcwd.electro.store.repositories.CategoryRepository;
import com.lcwd.electro.store.repositories.ProductRepository;
import com.lcwd.electro.store.services.CategoryService;
import com.lcwd.electro.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
   @Autowired
   private ProductRepository productRepository;
   @Autowired
   private ModelMapper modelMapper;
    @Value("${product.image.path}")
    private String imageUploadPath;
@Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto) {

        Product product = modelMapper.map(productDto, Product.class);
        product.setProductId(UUID.randomUUID().toString());
        product.setAddedDate(new Date());
        Product saved = productRepository.save(product);
        return modelMapper.map(saved, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found with given Id."));
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());
        Product saved = productRepository.save(product);
        return modelMapper.map(saved, ProductDto.class);
    }

    @Override
    public void delete(String productId) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found with given Id."));
        Path path= Paths.get(imageUploadPath+product.getProductImageName());

        try {
            Files.delete(path);
        }
        catch (NoSuchFileException e){
            e.printStackTrace();
        }
        productRepository.delete(product);
    }

    @Override
    public ProductDto getSingleProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found with given Id."));
return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(Integer pageNumber,Integer pageSize,String sortDir,String sortBy) {
        Sort sort=sortDir.equalsIgnoreCase("desc")?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable page= PageRequest.of(pageNumber,pageSize,sort);
       Page<Product> productPage = productRepository.findAll(page);
       return Helper.getPageResponse(productPage, ProductDto.class);
    }

    @Override
    public  PageableResponse<ProductDto> getAllLive(Integer pageNumber,Integer pageSize,String sortDir,String sortBy) {
        Sort sort=sortDir.equalsIgnoreCase("desc")?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable page= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> productPage = productRepository.findByLiveTrue(page);
        return Helper.getPageResponse(productPage, ProductDto.class);
    }

    @Override
    public  PageableResponse<ProductDto> searchByTitle(String subTitle,Integer pageNumber,Integer pageSize,String sortDir,String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable page = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByProductNameContainingIgnoreCase(subTitle,page);
        return Helper.getPageResponse(productPage, ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        //fetch the category
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id."));
        Product product = modelMapper.map(productDto, Product.class);
        product.setProductId(UUID.randomUUID().toString());
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product saved = productRepository.save(product);
        return modelMapper.map(saved, ProductDto.class);
    }

    public ProductDto updateWithCategory(String productId, String categoryId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id."));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id."));
        product.setCategory(category);
        Product saved = productRepository.save(product);
        return  modelMapper.map(saved, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId,Integer pageNumber,Integer pageSize,String sortDir,String sortBy) {
        Sort sort=sortDir.equalsIgnoreCase("desc")?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable page= PageRequest.of(pageNumber,pageSize,sort);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id."));
        Page<Product> byCategory = productRepository.findByCategory(category,page);
        return Helper.getPageResponse(byCategory, ProductDto.class);
    }
}
