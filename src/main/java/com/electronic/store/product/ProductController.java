package com.electronic.store.product;

import com.electronic.store.file.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static java.io.File.separator;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final FileService fileService;

    // create product
    @PostMapping("/create/{categoryId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDto create(@Valid @RequestBody ProductDto productDto,@PathVariable String categoryId, Authentication authentication) {
        log.info("Creating product {}", productDto);
        log.info("Product creating User is : {}", authentication.getName());
        return productService.create(productDto,categoryId);
    }

    //update product
    @PutMapping("/update/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDto update(@Valid @RequestBody ProductDto productDto, @PathVariable String productId){
        return productService.update(productDto,productId);
    }

    //delete products
    @DeleteMapping("/delete/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable String productId){
        productService.delete(productId);
        return "Product Deleted Successfully.";
    }

    // get all products
    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> getAll(@RequestParam(defaultValue = "0",required = false) int pageNumber,
                                @RequestParam(defaultValue = "5",required = false) int pageSize,
                                @RequestParam(defaultValue = "title",required = false) String field){
        return productService.getAllProducts(pageNumber,pageSize,field);
    }

    // get all product by live true
    @GetMapping("/get-all-live")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> getAllByLiveTrue(@RequestParam(defaultValue = "0",required = false) int pageNumber,
                                   @RequestParam(defaultValue = "5",required = false) int pageSize,
                                   @RequestParam(defaultValue = "title",required = false) String field){
        return productService.getAllLiveProducts(pageNumber,pageSize,field);
    }

    // find by title
    @GetMapping("/find-by-title/{title}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> findByTitleContaining(@PathVariable(value = "title",required = true) String title,
                                                  @RequestParam(defaultValue = "0",required = false) int pageNumber,
                                                  @RequestParam(defaultValue = "5",required = false) int pageSize,
                                                  @RequestParam(defaultValue = "title",required = false) String field){
        return productService.findByTitleContaining(pageNumber,pageSize,field,title);
    }

    @GetMapping("/find-by-category/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> findByCategory(@PathVariable(value = "categoryId",required = true) String categoryId,
                                                  @RequestParam(defaultValue = "0",required = false) int pageNumber,
                                                  @RequestParam(defaultValue = "5",required = false) int pageSize,
                                                  @RequestParam(defaultValue = "title",required = false) String field){
        return productService.findByCategory(pageNumber,pageSize,field,categoryId);
    }

    // file upload service for product
    @PostMapping("/image/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public String imageUpload(@RequestPart("file") MultipartFile file,
                              @PathVariable String productId) throws IOException {
        final String fileUploadSubPath = "products" + separator + productId;
        fileService.uploadFile(file, fileUploadSubPath);
        return "file uploaded successfully";
    }

    // serve product image
    @GetMapping("/serve-image/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> serveImage(@PathVariable String productId) throws IOException {
        final String fileUploadSubPath = "products" + separator + productId;
        Resource file = fileService.loadLatestFile(productId,fileUploadSubPath);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PutMapping("/update/{categoryId}/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDto updateCategory(@PathVariable String productId, @PathVariable String categoryId){
        return productService.giveCategoryToProduct(categoryId,productId);
    }
}
