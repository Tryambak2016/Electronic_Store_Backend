package com.electronic.store.order;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderRequest {
    @NotBlank(message = "cart Id can not Be blank !!")
    private String cartId;
    @NotBlank(message = "user Id can not Be blank !!")
    private String userId;

    private String orderStatus = "PENDING";
    private String paymentStatus = "NOT PAID";

    @NotBlank(message = "cart Id can not Be blank !!")
    private String billingAddress;

    @NotBlank(message = "Phone number is required!!")
    private String billingPhone;

    @NotBlank(message = "Name id required!!")
    private String billingName;
}
