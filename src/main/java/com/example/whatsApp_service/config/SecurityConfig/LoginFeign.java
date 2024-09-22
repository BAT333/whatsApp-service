package com.example.whatsApp_service.config.SecurityConfig;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name ="auth")
public interface LoginFeign {

    @RequestMapping(method = RequestMethod.GET, value = "/api/auth/valid/{token}")
    void valid(@PathVariable String token);

}
