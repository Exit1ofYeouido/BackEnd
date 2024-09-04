package com.example.Search.Common.Repository;

import com.example.Search.Common.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}
