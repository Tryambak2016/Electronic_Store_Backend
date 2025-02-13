package com.electronic.store.order;

import com.electronic.store.helper.ApiResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderDto create(OrderRequest orderDto);
    void removeOrder(String orderId);
    Page<OrderDto> getOrdersOfUser(String userId,int pageNumber, int pageSize,String field);
    Page<OrderDto> getAllOrders(int pageNumber, int pageSize,String field);
    OrderDto getOrderDetails(String orderId);
}
