package com.bookshop.userservice.service;

import com.bookshop.userservice.dto.AuthRequest;
import com.bookshop.userservice.dto.AuthResponse;
import com.bookshop.userservice.dto.UserDto;
import com.bookshop.userservice.keycloakclient.UserResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserResource userResource;
    private final UserService userService;

    private final AuthzClient authzClient = AuthzClient.create();

    public AuthResponse authorize(AuthRequest authRequest){
        AuthResponse authResponse = new AuthResponse();
        AuthorizationRequest request = new AuthorizationRequest();
        try {
            AuthorizationResponse response = authzClient.authorization(authRequest.getUsername(),
                    authRequest.getPassword()).authorize(request);
            authResponse.setAuthToken(response.getToken());
            authResponse.setRefreshToken(response.getRefreshToken());
        }catch (Exception e) {
            authResponse.setError(e.getMessage());
        }
        return authResponse;
    }

    public ResponseEntity<?> register(UserDto userDto){
        userResource.createUser(userDto);
        userResource.sendVerificationMail(userDto);
        log.info("verification email sent");
        return userService.createUser(userDto);
    }
}
