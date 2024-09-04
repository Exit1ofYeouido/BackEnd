package com.example.Search.Search.Common.Repository;

import com.example.Search.Search.Common.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}
