package com.bookshop.orderservice.service;

import com.bookshop.orderservice.dto.InventoryResponse;
import com.bookshop.orderservice.dto.OrderLineItemsDto;
import com.bookshop.orderservice.dto.OrderRequest;
import com.bookshop.orderservice.model.Order;
import com.bookshop.orderservice.model.OrderLineItems;
import com.bookshop.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToOrderLineItems).toList();

        order.setOrderLineItemsList(orderLineItems);
        order.setOrderNumber(UUID.randomUUID().toString());

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/v1/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();


        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::getIsInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
            log.info("New order with id={} was placed", order.getId());
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    //todo: deleteOrder
    //todo: changeOrder

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
    }


}
