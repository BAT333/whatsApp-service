package com.example.whatsApp_service.service;


import com.example.whatsApp_service.model.DataTextDTO;
import com.example.whatsApp_service.model.WhatsAppMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class WhatService {

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;

    @Value("${whatsapp.access.token}")
    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendMessage(String phoneNumber, String messageText) {
        log.info("Sending a message on WhatsApp");
        var request = this.create(phoneNumber,messageText);
        return restTemplate.postForObject(whatsappApiUrl, request, String.class);
    }

    private HttpEntity<WhatsAppMessage> create(String phoneNumber, String messageText) {
        WhatsAppMessage message = new WhatsAppMessage(
                "whatsapp",
                phoneNumber,
                "text",
                new DataTextDTO(messageText)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        return new HttpEntity<>(message, headers);
    }
}

