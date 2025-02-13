package com.electronic.store.cart;

import com.electronic.store.cartitem.CartItem;
import com.electronic.store.cartitem.CartItemRepository;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.product.Product;
import com.electronic.store.product.ProductRepository;
import com.electronic.store.user.User;
import com.electronic.store.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper mapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        int quantity = request.getQuantity();
        String productId = request.getProductId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found !"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found"));

        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e){
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedDate(new Date());
        }
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getCartItems();
        items = items.stream().map(item -> {

            if (item.getProduct().getProductId().equals(productId)){
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getDiscountedPrice()

                );
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        if (!updated.get()){
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity*product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getCartItems().add(cartItem);

        }


        cart.setUser(user);

        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItem item = cartItemRepository.findById(cartItem).orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));
        cartItemRepository.delete(item);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Not Found"));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Not Found"));
        return mapper.map(cart,CartDto.class);
    }
}
