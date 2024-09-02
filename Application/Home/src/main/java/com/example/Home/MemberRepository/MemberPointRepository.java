package com.example.Home.MemberRepository;

import com.example.Home.Member.MemberPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberPointRepository extends JpaRepository<MemberPoint, Long> {
    Optional<MemberPoint> findByMember_Id(Long memberId);
}
