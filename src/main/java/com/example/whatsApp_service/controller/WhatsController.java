package com.example.whatsApp_service.controller;

import com.example.whatsApp_service.service.WhatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "http://172.27.64.1:8082")
public class WhatsController {
    @Autowired
    private WhatService service;

    @PostMapping("/send")
    @Transactional
    @Operation(summary ="Send a message via WhatsApp", description = "Request the number and message to be sent and send the message that needs to be sent to specific customers.")
    public ResponseEntity<String> send(@RequestParam String phoneNumber, @RequestParam String message){
        return ResponseEntity.ok(this.service.sendMessage(phoneNumber,message));
    }

}
