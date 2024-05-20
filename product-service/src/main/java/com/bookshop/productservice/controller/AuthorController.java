package com.bookshop.productservice.controller;

import com.bookshop.productservice.dto.AuthorRequest;
import com.bookshop.productservice.service.AuthorService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/author")
@RequiredArgsConstructor
@SecurityRequirement(name="Keycloak")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAuthor(@RequestBody AuthorRequest authorRequest){
        authorService.createAuthor(authorRequest);
    }
}
