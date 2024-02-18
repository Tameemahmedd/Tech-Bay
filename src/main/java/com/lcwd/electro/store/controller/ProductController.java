package com.lcwd.electro.store.controller;

import com.lcwd.electro.store.dto.ApiResponseMessage;
import com.lcwd.electro.store.dto.ImageResponse;
import com.lcwd.electro.store.dto.PageableResponse;
import com.lcwd.electro.store.dto.ProductDto;
import com.lcwd.electro.store.services.FileService;
import com.lcwd.electro.store.services.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
@SecurityRequirement(name="scheme1")
public class ProductController {

    @Autowired
    private ProductService productService;
@Value("${product.image.path}")
    private String imageUploadPath;
@Autowired
private FileService fileService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> createProduct( @RequestBody ProductDto productDto){
        ProductDto product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct( @RequestBody ProductDto productDto,@PathVariable String productId){
        ProductDto product = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(productDto,HttpStatus.OK
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) throws IOException {
productService.delete(productId);
ApiResponseMessage apiResponseMessage= ApiResponseMessage.builder().message("Product deleted successfully").success(true).status(HttpStatus.OK).build();
return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId)
    {
        ProductDto singleProduct = productService.getSingleProduct(productId);
        return new ResponseEntity<>(singleProduct,HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber, @RequestParam(value = "pageSize",defaultValue = "10",required =false) Integer pageSize, @RequestParam(value = "sortBy",defaultValue = "productName",required =false) String sortBy, @RequestParam(value = "sortDir",defaultValue = "asc",required =false) String sortDir){
        PageableResponse<ProductDto> products = productService.getAll(pageNumber, pageSize, sortDir, sortBy);
        return  new ResponseEntity<>(products,HttpStatus.FOUND);
    }
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber, @RequestParam(value = "pageSize",defaultValue = "10",required =false) Integer pageSize, @RequestParam(value = "sortBy",defaultValue = "productName",required =false) String sortBy, @RequestParam(value = "sortDir",defaultValue = "asc",required =false) String sortDir){
        PageableResponse<ProductDto> products = productService.getAllLive(pageNumber, pageSize, sortDir, sortBy);
        return  new ResponseEntity<>(products,HttpStatus.FOUND);
    }

    @GetMapping("/search/{subtitle}")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsUsingKeywords(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber, @RequestParam(value = "pageSize",defaultValue = "10",required =false) Integer pageSize, @RequestParam(value = "sortBy",defaultValue = "productName",required =false) String sortBy, @RequestParam(value = "sortDir",defaultValue = "asc",required =false) String sortDir,@PathVariable String subtitle){
        PageableResponse<ProductDto> searchedByTitle = productService.searchByTitle(subtitle, pageNumber, pageSize, sortDir, sortBy);
        return new ResponseEntity<>(searchedByTitle,HttpStatus.FOUND);
    }

    @PostMapping("/image/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("file") MultipartFile file,@PathVariable String productId ) throws IOException {
        String uploadedImage = fileService.uploadImage(file, imageUploadPath);
        ProductDto singleProduct = productService.getSingleProduct(productId);
        singleProduct.setProductImageName(uploadedImage);
        productService.updateProduct(singleProduct,productId);
        ImageResponse imageResponse= ImageResponse.builder().message("Image uploaded successfully").imageName(uploadedImage).status(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    @GetMapping("/image/{productId}")
    public void getImage(@PathVariable String productId , HttpServletResponse httpServletResponse) throws IOException {
        ProductDto singleProduct = productService.getSingleProduct(productId);
        InputStream resource = fileService.getResource(imageUploadPath, singleProduct.getProductImageName());
        httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,httpServletResponse.getOutputStream());
    }
}
