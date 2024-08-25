package com.example.whatsApp_service.controller;

import com.example.whatsApp_service.service.WhatService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/whatsapp")
@SecurityRequirement(name = "bearer-key")
public class WhatsController {
    @Autowired
    private WhatService service;

    @PostMapping("/send")
    @Transactional
    public ResponseEntity<String> send(@RequestParam String phoneNumber, @RequestParam String message){
        return ResponseEntity.ok(this.service.sendMessage(phoneNumber,message));
    }

}
