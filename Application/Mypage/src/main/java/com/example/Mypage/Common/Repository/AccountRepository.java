package com.example.Mypage.Common.Repository;

import com.example.Mypage.Common.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
