package com.example.Auth.repository;

import com.example.Auth.entity.MemberAuth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<MemberAuth, Long> {

    public Optional<MemberAuth> findByMemberName(String memberName);
    
    public boolean existsByMemberName(String memberName);
}
