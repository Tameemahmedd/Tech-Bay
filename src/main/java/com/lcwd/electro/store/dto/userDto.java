package com.lcwd.electro.store.dto;

import com.lcwd.electro.store.entities.Role;
import com.lcwd.electro.store.validate.ImageNameValid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class userDto {
    private String userId;
    @Size(min = 3, max= 100, message = "Invalid Name!!")
    @Schema(name = "username",accessMode = Schema.AccessMode.READ_ONLY,description = "name of user.")
    private String name;
    //@Email(message = "Invalid User Email")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid User Email.")
    private String email;
    @NotBlank(message = "Password is required!")
    private String password;
    @Size(min=4, max = 6,message = "Invalid Gender.")
    private String gender;
    @NotBlank(message = "Enter about yourself.")
    private String about;

@ImageNameValid
    private String imageName;

    private Set<RoleDto> roles= new HashSet<>();
}
