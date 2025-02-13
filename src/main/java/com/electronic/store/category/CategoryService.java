package com.electronic.store.category;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);
    CategoryDto update(CategoryDto categoryDto, String categoryId);
    void delete(String categoryId);
    Page<CategoryDto> getAllCategory(int pageNumber, int pageSize, String field);
    CategoryDto get(String categoryId);
}
