package org.example.apple_oauth.ios;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long age;
}
