package com.bookshop.productservice.service;

import com.bookshop.productservice.dto.AuthorRequest;
import com.bookshop.productservice.model.Author;
import com.bookshop.productservice.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public void createAuthor(AuthorRequest authorRequest){
        Author author = Author.builder()
                .name(authorRequest.getName())
                .nationality(authorRequest.getNationality())
                .birthdate(authorRequest.getBirthDate())
                .build();
        authorRepository.save(author);
        log.info("New author {} is saved", author.getId());
    }

    //todo: findAll
    //todo: getOne

}
