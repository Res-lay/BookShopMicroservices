package com.bookshop.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value = "product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    private String id;
    private String title;
    @BsonProperty(value = "author_id")
    private String authorId;
    private String genre;
    @BsonProperty(value = "year_published")
    private int yearPublished;
    private String description;
    private String publisher;
    private BigDecimal price;
}
