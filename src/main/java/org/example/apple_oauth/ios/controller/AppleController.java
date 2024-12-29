package org.example.apple_oauth.ios.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.apple_oauth.ios.dto.AppleDto;
import org.example.apple_oauth.ios.entity.MsgEntity;
import org.example.apple_oauth.ios.service.AppleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("apple")
public class AppleController {

    private final AppleService appleService;

    @PostMapping("/callback")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request) throws Exception {
        AppleDto appleInfo = appleService.getAppleInfo(request.getParameter("code"));

        return ResponseEntity.ok()
                .body(new MsgEntity("Success", appleInfo));
    }
}
