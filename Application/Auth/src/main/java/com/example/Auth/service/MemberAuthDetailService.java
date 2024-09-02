package com.example.Auth.service;

import com.example.Auth.entity.MemberAuth;
import com.example.Auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAuthDetailService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {

        //TODO: 예외처리 핸들러 만들어서 수정하기
        MemberAuth memberAuth = authRepository.findByMemberName(memberName).orElse(null);

        if (memberAuth == null) {
            return null;
        }

        return memberAuth;
    }
}
