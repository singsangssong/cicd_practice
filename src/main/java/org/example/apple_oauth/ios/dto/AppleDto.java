package org.example.apple_oauth.ios.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AppleDto {

    private String id;
    private String token;
    private String email;

}
