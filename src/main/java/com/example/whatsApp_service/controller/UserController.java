package com.example.whatsApp_service.controller;

import com.example.whatsApp_service.Domain.User;
import com.example.whatsApp_service.Domain.UserRole;
import com.example.whatsApp_service.config.TokenConfig.TokenService;
import com.example.whatsApp_service.model.DataLoginDTO;
import com.example.whatsApp_service.model.DataTokeDTO;
import com.example.whatsApp_service.repository.UserRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth")
@SecurityRequirement(name = "bearer-key")
public class UserController {
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    @Transactional
    public ResponseEntity<DataTokeDTO> login(@RequestBody @Valid DataLoginDTO dto){
        var user = new UsernamePasswordAuthenticationToken(dto.login(),dto.password());
        var useAuth = manager.authenticate(user);
        return ResponseEntity.ok(new DataTokeDTO(tokenService.generatesToken((User) useAuth.getPrincipal())));
    }
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<DataLoginDTO> register(@RequestBody @Valid DataLoginDTO dto, UriComponentsBuilder builder){
        var user = this.userRepository.save(new User(dto.login(),this.encoder(dto.password()), UserRole.ADMIN));
        var uri = builder.path("auth/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new DataLoginDTO(user.getLogin(), user.getPasswords()));
    }

    private String encoder(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
