package com.example.Mypage.Common.Repository;

import com.example.Mypage.Common.Entity.AccountHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Integer> {

    @Query("SELECT ah FROM AccountHistory ah WHERE ah.member.id = :memberId ORDER BY ah.createdAt DESC")
    Page<AccountHistory> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

}
