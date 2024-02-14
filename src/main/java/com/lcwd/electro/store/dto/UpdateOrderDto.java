package com.lcwd.electro.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderDto {
    private String orderId;
    private String orderStatus;
    private String paymentStatus;
    private Date deliveredDate;
}
