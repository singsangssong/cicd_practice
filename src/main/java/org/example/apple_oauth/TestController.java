package org.example.apple_oauth;

import lombok.RequiredArgsConstructor;
import org.example.apple_oauth.ios.Member;
import org.example.apple_oauth.ios.MemberDto;
import org.example.apple_oauth.ios.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MemberRepository memberRepository;

    @GetMapping("/health")
    public ResponseEntity<String> testHealth() {
        return ResponseEntity.ok("healthy man!");
    }

    @GetMapping("/wow")
    public ResponseEntity<String> testPlusCode() {
        return ResponseEntity.ok("this is 안지현!");
    }

    @GetMapping("/data")
    public ResponseEntity<?> getMember() {
        List<Member> all = memberRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @PostMapping("/data")
    public ResponseEntity<?> saveMember() {
        MemberDto memberDto = MemberDto.builder().name("song").age(15l).build();
        Member member = Member.builder().name("song").age(15l).build();

        Member save = memberRepository.save(member);
        return ResponseEntity.ok(save.getId());
    }
}
