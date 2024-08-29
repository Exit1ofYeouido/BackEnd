package com.example.Mypage.Common.Repository;

import com.example.Mypage.Common.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepostory extends JpaRepository<Member,Long> {
}
