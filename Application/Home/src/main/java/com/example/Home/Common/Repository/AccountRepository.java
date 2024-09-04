package com.example.Home.Common.Repository;

import com.example.Home.Common.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByMemberId(Long memberId);
}
