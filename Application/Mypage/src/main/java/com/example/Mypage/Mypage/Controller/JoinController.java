package com.example.Mypage.Mypage.Controller;

import com.example.Mypage.Mypage.Dto.in.MemberSignupRequestDTO;
import com.example.Mypage.Mypage.Service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final SignupService signupService;

    @PostMapping("/signup")
    @Operation(description = "회원가입 API")
    public ResponseEntity<?> joinUser(@Valid @RequestBody MemberSignupRequestDTO memberSignupRequestDTO) {
        signupService.saveMember(memberSignupRequestDTO);
        return ResponseEntity.ok("good");
    }

}

