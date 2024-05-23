package com.bookshop.userservice.controller;

import com.bookshop.userservice.dto.AuthRequest;
import com.bookshop.userservice.dto.AuthResponse;
import com.bookshop.userservice.dto.UserDto;
import com.bookshop.userservice.keycloakclient.UserResource;
import com.bookshop.userservice.service.AuthService;
import com.bookshop.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-service/user")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody AuthRequest authRequest){
        AuthResponse response = authService.authorize(authRequest);
        if (response.getError() != null){
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@RequestBody UserDto userDto){
        return authService.register(userDto);
    }
}
