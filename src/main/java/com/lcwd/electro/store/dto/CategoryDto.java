package com.lcwd.electro.store.dto;

import com.lcwd.electro.store.validate.ImageNameValid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
