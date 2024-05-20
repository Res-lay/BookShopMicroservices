package com.bookshop.userservice.security;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class KeycloakSecurityUtil {
    Keycloak keycloak;

    @Value("${server-url}")
    private String serverUrl;

    private String realm = "master";

    private String clientId = "admin-cli";

    @Value("${grant-type}")
    private String grantType;

    private String username = "admin";

    private String password = "123";

    public Keycloak getKeycloakInstance(){
        if (keycloak == null){
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl).realm(realm)
                    .clientId(clientId)
                    .grantType(grantType)
                    .username(username)
                    .password(password)
                    .build();
        }
        return keycloak;
    }
}
