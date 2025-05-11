package com.pioner.banking.api.controller;

import com.pioner.banking.api.dto.PhoneAddRequest;
import com.pioner.banking.api.dto.PhoneUpdateRequest;
import com.pioner.banking.api.security.JwtUtil;
import com.pioner.banking.service.PhoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/phones")
public class PhoneController {

    private final PhoneService phoneService;
    private final JwtUtil jwtUtil;

    public PhoneController(PhoneService phoneService, JwtUtil jwtUtil) {
        this.phoneService = phoneService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<Void> addEmail(@RequestBody PhoneAddRequest request) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        phoneService.addPhone(userId, request.getPhone());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateEmail(@RequestBody PhoneUpdateRequest request) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        phoneService.updatePhone(userId, request.getPhone(), request.getNewPhone());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{phone}")
    public ResponseEntity<Void> deleteEmail(@PathVariable String phone) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        phoneService.deletePhone(userId, phone);
        return ResponseEntity.noContent().build();
    }

    //TODO добавить validation exception handler

}
