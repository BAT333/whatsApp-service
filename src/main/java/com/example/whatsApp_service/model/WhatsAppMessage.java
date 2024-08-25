package com.example.whatsApp_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;




public record WhatsAppMessage (
        @JsonProperty("messaging_product")
        String messagingProduct,
        String to,
        String type,
        DataTextDTO text){



}
