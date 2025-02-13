package com.electronic.store.cart;


import com.electronic.store.cartitem.CartItemDto;
import com.electronic.store.user.UserDto;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

    private String cartId;
    private Date createdDate;
    private UserDto user;
    private List<CartItemDto> cartItems = new ArrayList<>();

}

