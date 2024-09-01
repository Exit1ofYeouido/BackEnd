package com.example.Mypage.Mypage.Service;

import com.example.Mypage.Common.Entity.Account;
import com.example.Mypage.Common.Repository.AccountRepository;
import com.example.Mypage.Mypage.Dto.out.GetPointResponseDto;
import com.example.Mypage.Mypage.Exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public GetPointResponseDto getPoint(Long memberId) {
        try {
            Account account = accountRepository.findByMemberId(memberId)
                    .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));
            return GetPointResponseDto.builder()
                    .point(account.getPoint())
                    .build();
        } catch (AccountNotFoundException e) {
            log.error("유효한 계좌를 찾을 수 없습니다. {}", e.getMessage());
            throw e;
        }
    }
}
