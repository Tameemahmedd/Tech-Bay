package com.lcwd.electro.store.dto;

import com.lcwd.electro.store.validate.ImageNameValid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private String categoryId;
    @NotBlank
    @Size(min=3,max = 100,message = "Title must be of 3 chars")
    private String title;
    @NotBlank(message = "Description Required.")
    private String description;

    private String coverImage;
}
