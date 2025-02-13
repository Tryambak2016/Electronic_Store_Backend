package com.electronic.store.product;

import com.electronic.store.category.Category;
import com.electronic.store.category.CategoryRepository;
import com.electronic.store.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ModelMapper mapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDto create(ProductDto productDto,String categoryId) {
        Product product = mapper.map(productDto, Product.class);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        product.setCategory(category);
        product.setProductId(UUID.randomUUID().toString());
        product.setAddedDate(new Date());
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setLive(productDto.isLive());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setStock(productDto.isStock());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setProductImageName(productDto.getProductImageName());
        Product updatedProduct = productRepository.save(product);
        return mapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public ProductDto get(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found"));
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public Page<ProductDto> getAllProducts(int pageNumber, int pageSize, String field) {
        Sort sort = (field != null && !field.isEmpty()) ? Sort.by(field) : Sort.by("defaultField");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        return productRepository.findAll(pageRequest).map(product -> mapper.map(product, ProductDto.class));
    }

    @Override
    public Page<ProductDto> getAllLiveProducts(int pageNumber, int pageSize, String field) {
        Sort sort = (field != null && !field.isEmpty()) ? Sort.by(field) : Sort.by("defaultField");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        return productRepository.findByLiveTrue(pageRequest).map(product -> mapper.map(product, ProductDto.class));
    }

    @Override
    public Page<ProductDto> findByTitleContaining(int pageNumber, int pageSize, String field, String title) {
        Sort sort = (field != null && !field.isEmpty()) ? Sort.by(field) : Sort.by("defaultField");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        return productRepository.findByTitle(title, pageRequest).map(product -> mapper.map(product, ProductDto.class));
    }

    @Override
    public Page<ProductDto> findByCategory(int pageNumber, int pageSize, String field, String categoryId) {
        Sort sort = (field != null && !field.isEmpty()) ? Sort.by(field) : Sort.by("defaultField");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found"));
        return productRepository.findByCategory(category,pageRequest).map(product -> mapper.map(product, ProductDto.class));
    }

    @Override
    public ProductDto giveCategoryToProduct(String categoryId, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found"));
        product.setCategory(category);
        return mapper.map(product, ProductDto.class);
    }

}
