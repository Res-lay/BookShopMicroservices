package com.bookshop.userservice.service;

import com.bookshop.userservice.dto.UserDto;
import com.bookshop.userservice.keycloakclient.UserResource;
import com.bookshop.userservice.models.User;
import com.bookshop.userservice.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepo userRepo;
    private final UserResource userResource;

    public ResponseEntity<?> createUser(UserDto userDto){
        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .firstname(userDto.getFirstName())
                .lastname(userDto.getLastName())
                .build();
        userRepo.save(user);
        return userResource.createUser(userDto);
    }

    public User getUser(Long id){
        Optional<User> user = userRepo.findById(id);
        return user.get();
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }
}
