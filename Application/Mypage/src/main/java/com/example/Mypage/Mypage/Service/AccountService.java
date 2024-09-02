package com.example.Mypage.Mypage.Service;

import com.example.Mypage.Common.Entity.Account;
import com.example.Mypage.Common.Entity.AccountHistory;
import com.example.Mypage.Common.Repository.AccountHistoryRepository;
import com.example.Mypage.Common.Repository.AccountRepository;
import com.example.Mypage.Mypage.Dto.out.GetPointHistoryResponseDto;
import com.example.Mypage.Mypage.Dto.out.GetPointResponseDto;
import com.example.Mypage.Mypage.Exception.AccountNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;

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

    public List<GetPointHistoryResponseDto> getPointHistory(Long memberId, int index, int limit) {
        Pageable pageable = PageRequest.of(index, limit);
        Page<AccountHistory> accountHistoryPage = accountHistoryRepository.findByMemberId(memberId, pageable);
        List<AccountHistory> accountHistoryList = accountHistoryPage.getContent();

        return getPointHistoryResponseDtos(accountHistoryList);
    }

    private static List<GetPointHistoryResponseDto> getPointHistoryResponseDtos(
            List<AccountHistory> accountHistoryList) {
        return accountHistoryList.stream()
                .map(accountHistory -> GetPointHistoryResponseDto.builder()
                        .memberId(accountHistory.getMember().getId())
                        .requestPoint(accountHistory.getRequestPoint())
                        .resultPoint(accountHistory.getResultPoint())
                        .type(accountHistory.getType())
                        .createdAt(accountHistory.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
