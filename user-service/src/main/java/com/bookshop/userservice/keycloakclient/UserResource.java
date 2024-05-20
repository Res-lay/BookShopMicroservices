package com.bookshop.userservice.keycloakclient;

import com.bookshop.userservice.dto.User;
import com.bookshop.userservice.security.KeycloakSecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.coyote.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/keycloak")
@SecurityRequirement(name="Keycloak")
public class UserResource {
    @Autowired
    private KeycloakSecurityUtil keycloakSecurityUtil;

    @Value("${realm}")
    private String realm;

    @GetMapping("/users")
    public List<User> getUsers(){
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
        return mapUsers(userRepresentations);
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(User user){
        UserRepresentation userRepresentation = mapUserRepresentation(user);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).users().create(userRepresentation);
        return ResponseEntity.ok().body(userRepresentation);
    }

    private UserRepresentation mapUserRepresentation(User user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        List<CredentialRepresentation> credentialRepresentations = new ArrayList<>();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(user.getPassword());
        credentialRepresentations.add(credentialRepresentation);
        userRepresentation.setCredentials(credentialRepresentations);
        return userRepresentation;

    }

    private List<User> mapUsers(List<UserRepresentation> userRepresentations) {
        List<User> users = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRepresentations)){
            userRepresentations.forEach(userRepresentation -> {
                users.add(mapUser(userRepresentation));
            });
        }
        return users;
    }

    private User mapUser(UserRepresentation userRepresentation) {
        User user = User.builder()
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .email(userRepresentation.getEmail())
                .username(userRepresentation.getUsername())
                .build();

        return user;
    }

}
