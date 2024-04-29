package com.bookshop.productservice.service;

import com.bookshop.productservice.dto.ProductRequest;
import com.bookshop.productservice.dto.ProductResponse;
import com.bookshop.productservice.model.Product;
import com.bookshop.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .title(productRequest.getTitle())
                .authorId(productRequest.getAuthorId())
                .yearPublished(productRequest.getYearPublished())
                .genre(productRequest.getGenre())
                .publisher(productRequest.getPublisher())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("New product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

       return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .authorId(product.getAuthorId())
                .yearPublished(product.getYearPublished())
                .genre(product.getGenre())
                .publisher(product.getPublisher())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
