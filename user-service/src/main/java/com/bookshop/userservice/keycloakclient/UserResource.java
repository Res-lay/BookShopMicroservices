package com.bookshop.userservice.keycloakclient;

import com.bookshop.userservice.dto.UserDto;
import com.bookshop.userservice.security.KeycloakSecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserResource {
    @Autowired
    private KeycloakSecurityUtil keycloakSecurityUtil;

    @Value("${realm}")
    private String realm;

    public List<UserDto> getUsers(){
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
        return mapUsers(userRepresentations);
    }

    public ResponseEntity<?> createUser(UserDto userDto){
        UserRepresentation userRepresentation = mapUserRepresentation(userDto);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).users().create(userRepresentation);
        return ResponseEntity.ok().body(userRepresentation);
    }

    private UserRepresentation mapUserRepresentation(UserDto userDto) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDto.getUsername());
        userRepresentation.setFirstName(userDto.getFirstName());
        userRepresentation.setLastName(userDto.getLastName());
        userRepresentation.setEmail(userDto.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        List<CredentialRepresentation> credentialRepresentations = new ArrayList<>();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(userDto.getPassword());
        credentialRepresentations.add(credentialRepresentation);
        userRepresentation.setCredentials(credentialRepresentations);
        return userRepresentation;

    }

    private List<UserDto> mapUsers(List<UserRepresentation> userRepresentations) {
        List<UserDto> userDtos = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRepresentations)){
            userRepresentations.forEach(userRepresentation -> {
                userDtos.add(mapUser(userRepresentation));
            });
        }
        return userDtos;
    }

    private UserDto mapUser(UserRepresentation userRepresentation) {
        UserDto userDto = UserDto.builder()
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .email(userRepresentation.getEmail())
                .username(userRepresentation.getUsername())
                .build();

        return userDto;
    }

}
