package com.bookshop.productservice;

import com.bookshop.productservice.dto.AuthorRequest;
import com.bookshop.productservice.dto.ProductRequest;
import com.bookshop.productservice.repository.AuthorRepository;
import com.bookshop.productservice.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.8");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuthorRepository authorRepository;


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
    }

    @Test
    void shouldCreateAuthor() throws Exception {
        AuthorRequest authorRequest = getAuthorRequest();
        String authorRequestString = objectMapper.writeValueAsString(authorRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorRequestString))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product"))
                .andExpect(status().isOk());

        Assertions.assertEquals(1, authorRepository.findAll().size());
    }

    private AuthorRequest getAuthorRequest() {
        return AuthorRequest.builder()
                .name("Author Name")
                .birthDate("24.07.2005")
                .nationality("Nationality")
                .build();
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .authorId("id")
                .title("New Book")
                .price(BigDecimal.valueOf(300))
                .description("description")
                .genre("Fiction")
                .publisher("Publisher")
                .yearPublished(2024)
                .build();
    }

}
