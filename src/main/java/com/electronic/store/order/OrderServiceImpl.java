package com.electronic.store.order;

import com.electronic.store.cart.Cart;
import com.electronic.store.cartitem.CartItem;
import com.electronic.store.cart.CartRepository;
import com.electronic.store.exceptions.BadApiRequest;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.product.ProductRepository;
import com.electronic.store.user.User;
import com.electronic.store.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDto create(OrderRequest orderDto) {

        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();
        //fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart with given id not found on server !!"));
        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.size() <= 0) {
            throw new BadApiRequest("Invalid number of items in cart !!");
        }

        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();


        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        //
        cart.getCartItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);
        return mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    @Override
    public Page<OrderDto> getOrdersOfUser(String userId,int pageNumber, int pageSize,String field) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Sort sort = (field != null && !field.isEmpty()) ? Sort.by(field) : Sort.by("defaultField");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return orderRepository.findByUserId(userId,pageable).map(order -> mapper.map(order, OrderDto.class));
    }

    @Override
    public Page<OrderDto> getAllOrders(int pageNumber, int pageSize, String field) {
        Sort sort = (field != null && !field.isEmpty()) ? Sort.by(field) : Sort.by("defaultField");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return orderRepository.findAll(pageable).map(order -> mapper.map(order, OrderDto.class));
    }

    @Override
    public OrderDto getOrderDetails(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapper.map(order, OrderDto.class);
    }
}
