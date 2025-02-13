package com.electronic.store.product;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    ProductDto create(ProductDto productDto,String categoryId);
    ProductDto update(ProductDto productDto,String productId);
    void delete(String productId);
    ProductDto get(String productId);
    Page<ProductDto> getAllProducts(int pageNumber, int pageSize, String field);
    Page<ProductDto> getAllLiveProducts(int pageNumber, int pageSize, String field);
    Page<ProductDto> findByTitleContaining(int pageNumber, int pageSize, String field, String title);
    Page<ProductDto> findByCategory(int pageNumber, int pageSize, String field, String categoryId);

    ProductDto giveCategoryToProduct(String categoryId, String productId);
}
