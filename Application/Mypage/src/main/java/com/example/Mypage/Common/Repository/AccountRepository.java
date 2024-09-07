package com.example.Mypage.Common.Repository;

import com.example.Mypage.Common.Entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findByMemberId(Long memberId);

    Optional<Account> findByAccountNumber(String accountNumber);
}
