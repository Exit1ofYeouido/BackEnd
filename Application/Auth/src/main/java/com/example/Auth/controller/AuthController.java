package com.example.Auth.controller;

import com.example.Auth.dto.PhoneVerificationRequestDTO;
import com.example.Auth.dto.PhoneVerificationVerifyRequestDTO;
import com.example.Auth.service.AuthService;
import com.example.Auth.service.TokenService;
import com.example.Auth.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final TokenService tokenService;
    private final DefaultMessageService messageService;
    private final VerificationService verificationService;
    private final AuthService authService;

    public AuthController(@Value("${coolsms.apikey}") String apiKey,
                          @Value("${coolsms.apisecret}") String apiSecretKey,
                          VerificationService verificationService,
                          TokenService tokenService,
                          AuthService authService) {

        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
        this.verificationService = verificationService;
        this.tokenService = tokenService;
        this.authService = authService;
    }

    @PostMapping("/reissue")
    @Operation(description = "Access, Refresh 토큰 재발급")
    public ResponseEntity<?> reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        HttpServletResponse res = tokenService.reissueRefreshToken(request, response);
        return ResponseEntity.status(res.getStatus()).build();
    }


    @PostMapping("/phone-verification")
    @Operation(description = "핸드폰 번호 인증을 위한 인증코드 요청 API")
    public ResponseEntity<?> getPhoneVerificationCode(
            @RequestBody PhoneVerificationRequestDTO phoneVerificationRequestDTO,
            @Value("${coolsms.fromnumber}") String fromNumber) {

        Message message = new Message();
        String code = verificationService.makeVerificationCode(phoneVerificationRequestDTO);

        message.setFrom(fromNumber);
        message.setTo(phoneVerificationRequestDTO.getPhoneNumber());
        message.setText("[StockCraft] 인증번호 입니다. \n ["
                + code + "] \n 화면에 입력해주세요.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return new ResponseEntity<>("인증번호를 발송하였습니다.", HttpStatus.CREATED);
    }

    @PostMapping("/phone-verification/verify")
    @Operation(description = "인증코드를 검증하는 API")
    public ResponseEntity<?> checkVerifyPhoneCode(
            @RequestBody PhoneVerificationVerifyRequestDTO phoneVerificationVerifyRequestDTO) {

        if (verificationService.checkVerificationCode(phoneVerificationVerifyRequestDTO)) {
            return new ResponseEntity<>("인증번호 확인이 정상 완료되었습니다.", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("유효하지 않은 인증번호 입니다.", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/check-username/{username}")
    @Operation(description = "중복된 유저ID가 있는지 확인하는 API")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        Boolean isExist = authService.checkMemberName(username);
        return new ResponseEntity<>(isExist, HttpStatus.OK);
    }

}
