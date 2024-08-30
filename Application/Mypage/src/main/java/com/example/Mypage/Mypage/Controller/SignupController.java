package com.example.Mypage.Mypage.Controller;

import com.example.Mypage.Mypage.Dto.in.MemberSignupRequestDTO;
import com.example.Mypage.Mypage.Service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/signup")
    @Operation(description = "회원가입 API")
    public ResponseEntity<String> joinUser(@Valid @RequestBody MemberSignupRequestDTO memberSignupRequestDTO) {
        if (signupService.saveMember(memberSignupRequestDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패");

    }

}

