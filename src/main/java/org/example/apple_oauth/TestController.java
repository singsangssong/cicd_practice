package org.example.apple_oauth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/health")
    public ResponseEntity<String> testHealth() {
        return ResponseEntity.ok("healthy man!");
    }

    @GetMapping("/wow")
    public ResponseEntity<String> testPlusCode() {
        return ResponseEntity.ok("this is 안지현!");
    }
}
