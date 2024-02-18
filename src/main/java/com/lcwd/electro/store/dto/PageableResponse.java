package com.lcwd.electro.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableResponse<T> {
    private List<T> content;
   private Integer pageNumber;
   private Integer pageSize;
   private Long totalElements;
   private Integer totalPages;
   private boolean lastPage;

}
