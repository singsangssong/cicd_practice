package org.example.apple_oauth.ios;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MemberDto {
    private Long id;
    private String name;
    private Long age;
}
