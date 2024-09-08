package com.example.Mypage.Common.Repository;

import com.example.Mypage.Common.Entity.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByMemberName(String memberName);
    
}
