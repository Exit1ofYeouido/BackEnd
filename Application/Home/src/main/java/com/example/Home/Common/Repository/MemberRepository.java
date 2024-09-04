package com.example.Home.Common.Repository;

import com.example.Home.Common.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
