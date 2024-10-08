package com.example.Search.Common.Repository;

import com.example.Search.Common.Entity.MemberStock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberStockRepository extends JpaRepository<MemberStock, Long> {
    @Query("SELECT ms FROM MemberStock ms WHERE ms.member.id = :memberId ORDER BY ms.amount DESC")
    List<MemberStock> findTop5ByMemberIdOrderByAmountDesc(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT ms.amount FROM MemberStock ms WHERE ms.member.id = :memberId AND ms.stockCode = :stockCode")
    Optional<Double> findStockAmountByMemberIdAndStockCode(@Param("memberId") Long memberId, @Param("stockCode") String stockCode);

    @Query("SELECT ms.amount FROM MemberStock ms WHERE ms.member.id = :memberId AND ms.stockCode = :stockCode")
    Optional<Double> findAvailableAmountByMemberIdAndStockCode(@Param("memberId") Long memberId, @Param("stockCode") String stockCode);

}
