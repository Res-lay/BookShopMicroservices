package com.bookshop.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    String authToken;
    String refreshToken;
    String error;
}
