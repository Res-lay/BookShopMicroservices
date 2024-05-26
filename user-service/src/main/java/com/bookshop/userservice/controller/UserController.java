package com.bookshop.userservice.controller;

import com.bookshop.userservice.dto.UserDto;
import com.bookshop.userservice.models.User;
import com.bookshop.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-service")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserDto user){
        userService.createUser(user);
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return userService.getAll();
    }

    @GetMapping("/users/{id}")
    public User getUsers(@PathVariable Long id){
        return userService.getUser(id);
    }


}
