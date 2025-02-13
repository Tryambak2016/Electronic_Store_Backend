package com.electronic.store.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    //create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto){
        return categoryService.create(categoryDto);
    }

    //update
    @PutMapping("/update/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto update(
            @RequestBody CategoryDto categoryDto,
            @PathVariable("categoryId") String categoryId)
    {
        return categoryService.update(categoryDto,categoryId);
    }

    //delete
    @DeleteMapping("/delete/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable("categoryId") String categoryId){
        categoryService.delete(categoryId);
        return "Category Deleted Successfully !!";
    }

    //get
    @GetMapping("/get/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public CategoryDto get(@PathVariable("categoryId") String categoryId){
        return categoryService.get(categoryId);
    }

    //get
    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CategoryDto> getAll(@RequestParam(defaultValue = "0",required = false) int pageNumber,
                                    @RequestParam(defaultValue = "5",required = false) int pageSize,
                                    @RequestParam(defaultValue = "title",required = false) String field){
        return categoryService.getAllCategory(pageNumber,pageSize,field);
    }
}
