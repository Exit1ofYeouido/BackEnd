package com.example.Search.Common.Repository;

import com.example.Search.Common.Entity.MemberStockHolding;
import com.example.Search.Common.Entity.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberStockHoldingRepository extends JpaRepository<MemberStockHolding,Long> {

    Optional<MemberStockHolding> findByMemberIdAndStockCode(Long memberId, String stockCode);
}