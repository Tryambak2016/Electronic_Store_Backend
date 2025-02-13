package com.electronic.store.cart;

import com.electronic.store.helper.ApiResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add-to-cart/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public CartDto addItemToCart(@RequestBody AddItemToCartRequest request, @PathVariable String userId){
        CartDto cartDto = cartService.addItemToCart(userId,request);
        return cartDto;
    }

    @DeleteMapping("/{userId}/item/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    private ApiResponseMessage removeItemFromCart(@PathVariable int itemId,@PathVariable String userId){
        cartService.removeItemFromCart(userId,itemId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Message("Item Has Removed")
                .status(HttpStatus.OK)
                .Success(true)
                .build();
        return message;
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/clear/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    private ApiResponseMessage clearCart(@PathVariable String userId){
        cartService.clearCart(userId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Message("cart cleared")
                .status(HttpStatus.OK)
                .Success(true)
                .build();
        return message;
    }

    @GetMapping("/get/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public CartDto getCart(@PathVariable String userId){
        CartDto cartDto = cartService.getCartByUser(userId);
        return cartDto;
    }
}
