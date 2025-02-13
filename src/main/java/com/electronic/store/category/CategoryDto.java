package com.electronic.store.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private String categoryId;
    @NotNull
    @Size(min = 3,max = 30,message = "Title is invalid")
    private String title;
    @NotNull(message = "description required !!")
    private String description;
}
