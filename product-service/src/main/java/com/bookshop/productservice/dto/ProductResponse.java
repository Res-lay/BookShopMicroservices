package com.bookshop.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;
    private String title;
    private String authorId;
    private String genre;
    private int yearPublished;
    private String publisher;
    private String description;
    private BigDecimal price;
}
