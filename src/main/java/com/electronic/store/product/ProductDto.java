package com.electronic.store.product;

import com.electronic.store.category.CategoryDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
public class ProductDto {

    private String productId;
    private String title;
    private String description;
    @NotNull
    private int price;
    @NotNull
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private int discountedPrice;
    private String productImageName;
    private CategoryDto category;

}

