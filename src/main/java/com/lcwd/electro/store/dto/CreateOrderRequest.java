package com.lcwd.electro.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotBlank(message = "Cart id is required.")
    private String cartId;
    @NotBlank(message = "User id is required.")
    private String userId;

    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    @NotBlank(message = "Address is required.")
    private String billingAddress;
    @NotBlank(message = "Phone number is required.")
    private String billingPhone;
    @NotBlank(message = "Billing name is required.")
    private String billingName;


}
