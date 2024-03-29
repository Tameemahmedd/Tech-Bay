package com.lcwd.electro.store.validate;

import lombok.extern.slf4j.Slf4j;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
@Slf4j
public class ImageNameValidator implements ConstraintValidator<ImageNameValid,String>
{

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
      log.info("Message from isValid: {}",s);
       if(s.isBlank())
       {
           return false;
       }
       else {
           return true;
       }
    }
}
