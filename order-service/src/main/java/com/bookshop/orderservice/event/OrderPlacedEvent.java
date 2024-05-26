package com.bookshop.orderservice.event;

import com.bookshop.orderservice.model.Order;
import com.bookshop.orderservice.model.OrderLineItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {
    private String username;
    private String userEmail;
    private List<OrderLineItems> order;
}
