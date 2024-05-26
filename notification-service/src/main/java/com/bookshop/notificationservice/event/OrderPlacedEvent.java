package com.bookshop.notificationservice.event;

import com.bookshop.notificationservice.model.OrderLineItems;
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