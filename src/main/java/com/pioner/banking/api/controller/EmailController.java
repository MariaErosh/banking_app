package com.pioner.banking.api.controller;

import com.pioner.banking.api.dto.EmailAddRequest;
import com.pioner.banking.api.dto.EmailUpdateRequest;
import com.pioner.banking.api.security.JwtUtil;
import com.pioner.banking.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/emails")
public class EmailController {

    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    public EmailController(EmailService emailService, JwtUtil jwtUtil) {
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<Void> addEmail(@RequestBody EmailAddRequest request) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        emailService.addEmail(userId, request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PutMapping
    public ResponseEntity<Void> updateEmail(@RequestBody EmailUpdateRequest request) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        emailService.updateEmail(userId, request.getEmail(), request.getNewEmail());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteEmail(@PathVariable String email) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        emailService.deleteEmail(userId, email);
        return ResponseEntity.noContent().build();
    }

}
