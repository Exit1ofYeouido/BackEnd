package com.example.Home.MemberRepository;

import com.example.Home.Member.MemberStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberStockRepository extends JpaRepository<MemberStock, Long> {
    List<MemberStock> findByMemberId(Long memberId);
}
