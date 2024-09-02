package com.example.Mypage.Common.Repository;

import com.example.Mypage.Common.Entity.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    @Query("SELECT t FROM Trade t WHERE t.member.id = :memberId ORDER BY t.createdAt DESC")
    Page<Trade> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
