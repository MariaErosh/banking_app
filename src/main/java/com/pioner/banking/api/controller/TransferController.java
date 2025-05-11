package com.pioner.banking.api.controller;

import com.pioner.banking.api.dto.TransferRequest;
import com.pioner.banking.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest transferRequest) {
        Long userIdFrom = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        transferService.transfer(userIdFrom, transferRequest.getToUserId(), transferRequest.getSum());
        return  ResponseEntity.ok().build();
    }
}
