package org.example.apple_oauth.ios.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppleController {
    @GetMapping("/health")
    public ResponseEntity<String> createClientSecret(){
        return ResponseEntity.ok("healthy connect");
    }
}
