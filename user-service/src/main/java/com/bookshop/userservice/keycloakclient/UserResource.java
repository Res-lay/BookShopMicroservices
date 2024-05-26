package com.bookshop.userservice.keycloakclient;

import com.bookshop.userservice.dto.UserDto;
import com.bookshop.userservice.models.User;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserResource {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;


    public List<UserDto> getUsers() {
        try {
            List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
            return mapUsers(userRepresentations);
        } catch (Exception e) {
            return null;
        }
    }

    public ResponseEntity<?> createUser(UserDto userDto) {
        UserRepresentation userRepresentation = mapUserRepresentation(userDto);
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
        userRepresentation.setEmailVerified(false);
        userRepresentation.setRequiredActions(List.of("VERIFY_EMAIL"));
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
        if (CollectionUtil.isNotEmpty(userRepresentations)) {
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

    public void sendVerificationMail(UserDto userDto) {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> users = keycloak.realm(realm).users().searchByEmail(userDto.getEmail(), true);
        usersResource.get(users.get(0).getId()).sendVerifyEmail();
    }
}
