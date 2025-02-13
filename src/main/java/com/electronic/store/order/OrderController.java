package com.electronic.store.order;

import com.electronic.store.helper.ApiResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public OrderDto createOrder(@Valid @RequestBody OrderRequest request){
        OrderDto createdOrder = orderService.create(request);
        return createdOrder;
    }

    @DeleteMapping("/delete/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ApiResponseMessage removeOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Success(true)
                .status(HttpStatus.OK)
                .Message("Order Deleted Successfully !!")
                .build();
        return message;
    }

    @GetMapping("/get/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OrderDto> getOrdersOfUser(@PathVariable String userId,
                                          @RequestParam(defaultValue = "0",required = false) int pageNumber,
                                          @RequestParam(defaultValue = "5",required = false) int pageSize,
                                          @RequestParam(defaultValue = "orderAmount",required = false) String field) {
        return orderService.getOrdersOfUser(userId, pageNumber, pageSize, field);
    }

    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OrderDto> getAllOrders(@RequestParam(defaultValue = "0",required = false) int pageNumber,
                                       @RequestParam(defaultValue = "5",required = false) int pageSize,
                                       @RequestParam(defaultValue = "orderAmount",required = false) String field) {
        return orderService.getAllOrders(pageNumber, pageSize, field);
    }

}
