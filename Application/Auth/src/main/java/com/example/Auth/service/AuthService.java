package com.example.Auth.service;

import com.example.Auth.exception.MembernameNotValidException;
import com.example.Auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;

    public boolean checkMembername(String memberName) {
        if (memberName.length() < 4 || memberName.matches("\\d+")) {
            throw new MembernameNotValidException();
        }
        return authRepository.existsByMemberName(memberName);
    }

}
