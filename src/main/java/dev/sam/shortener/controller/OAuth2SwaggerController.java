package dev.sam.shortener.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "OAuth2 Security")
public class OAuth2SwaggerController {
    
    @Operation(summary = "Login with Google")
    @GetMapping("/oauth2/authorization/google")
    public void fakeGoogleLogin() {}
    
    @Operation(summary = "Login Callback")
    @GetMapping("/login/oauth2/code/google")
    public void fakeGoogleCallback() {}
}