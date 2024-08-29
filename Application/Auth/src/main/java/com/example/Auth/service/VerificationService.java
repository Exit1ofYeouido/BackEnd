package com.example.Auth.service;

import com.example.Auth.dto.PhoneVerificationRequestDTO;
import com.example.Auth.dto.PhoneVerificationVerifyRequestDTO;
import com.example.Auth.entity.PhoneVerification;
import com.example.Auth.exception.VerificationCodeRequestException;
import com.example.Auth.repository.VerificationRepository;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VerificationService {

    private final VerificationRepository verificationRepository;

    public VerificationService(VerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    public String makeVerificationCode(PhoneVerificationRequestDTO phoneVerificationRequestDTO) {

        String phoneNumber = phoneVerificationRequestDTO.getPhoneNumber();
        String randomCode = createRandomNumber();

        PhoneVerification savedPhoneVerification = verificationRepository.findByPhoneNumber(
                phoneNumber);

        if (savedPhoneVerification != null) {
            throw new VerificationCodeRequestException("1분 경과 후 인증번호 재발급이 가능합니다.");
        }

        PhoneVerificationVerifyRequestDTO newPhoneVerificationVerifyRequestDTO = new PhoneVerificationVerifyRequestDTO(
                phoneNumber, randomCode);

        verificationRepository.save(newPhoneVerificationVerifyRequestDTO.toEntity());

        return randomCode;
    }

    public boolean checkVerificationCode(PhoneVerificationVerifyRequestDTO verificationVerifyRequestDTO) {
        PhoneVerification foundPhone = verificationRepository.findByPhoneNumber(
                verificationVerifyRequestDTO.getPhoneNumber());

        if (foundPhone.getVerificationCode().equals(verificationVerifyRequestDTO.getVerificationCode())) {
            log.info("Phone Verification : {} 인증성공", verificationVerifyRequestDTO.getPhoneNumber());
            return true;
        }
        log.info("Phone Verification Info : {} 인증실패", verificationVerifyRequestDTO.getPhoneNumber());
        return false;
    }

    private String createRandomNumber() {
        Random rand = new Random();
        String randomNum = "";
        for (int i = 0; i < 4; i++) {
            String random = Integer.toString(rand.nextInt(10));
            randomNum += random;
        }

        return randomNum;
    }
}
