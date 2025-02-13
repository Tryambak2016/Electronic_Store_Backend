package com.electronic.store.cart;

public interface CartService {
    CartDto addItemToCart(String userId, AddItemToCartRequest request);
    void removeItemFromCart(String userId,int cartItem);
    void clearCart(String userId);
    CartDto getCartByUser(String userId);
}
