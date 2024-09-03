package com.example.Home.Common.Repository;

import com.example.Home.Common.Entity.MemberStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberStockRepository extends JpaRepository<MemberStock, Long> {
    List<MemberStock> findByMemberId(Long memberId);
}
