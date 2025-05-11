package com.pioner.banking.api.controller;

import com.pioner.banking.api.dto.LoginRequest;
import com.pioner.banking.api.dto.UserDto;
import com.pioner.banking.dao.entity.User;
import com.pioner.banking.service.UserSearchService;
import com.pioner.banking.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userservice;

    private final UserSearchService userSearchService;

    public UserController(UserService userService, UserSearchService userSearchService){
        this.userservice = userService;
        this.userSearchService = userSearchService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userservice.getUserById(userId);
        return ResponseEntity.ok(new UserDto(user));
    }

    @PostMapping("/login/email")
    public ResponseEntity<String> authenticateByEmail(@RequestBody LoginRequest loginRequest) {
        String token = userservice.authenticateByEmail(loginRequest.getLogin(), loginRequest.getPassword());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login/phone")
    public ResponseEntity<String> authenticateByPhone(@RequestBody LoginRequest loginRequest) {
        String token = userservice.authenticateByPhone(loginRequest.getLogin(), loginRequest.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/search")
    public Page<UserDto> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userSearchService.searchUsers(name, phone, email, dateOfBirth, pageable).map(UserDto::new);
    }

}
