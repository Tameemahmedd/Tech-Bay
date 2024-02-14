package com.lcwd.electro.store.helper;

import com.lcwd.electro.store.dto.PageableResponse;
import com.lcwd.electro.store.dto.userDto;
import com.lcwd.electro.store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    public static <U,V>PageableResponse<V> getPageResponse(Page<U> page,Class<V> vClass){
        List<U> entity = page.getContent();
        List<V> userDtos = entity.stream().map(object -> new ModelMapper().map(object,vClass)).collect(Collectors.toList());

        PageableResponse<V> response=new PageableResponse<>();
        response.setContent(userDtos);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;
    }
}
