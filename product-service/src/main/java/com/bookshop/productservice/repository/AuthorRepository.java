package com.bookshop.productservice.repository;

import com.bookshop.productservice.model.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorRepository extends MongoRepository<Author, String> {
}
