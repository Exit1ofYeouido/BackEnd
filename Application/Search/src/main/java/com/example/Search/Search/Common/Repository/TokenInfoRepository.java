package com.example.Search.Search.Common.Repository;

import com.example.Search.Search.Common.Entity.TokenInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenInfoRepository extends JpaRepository<TokenInfo, Long> {
    Optional<TokenInfo> findTopByOrderByIdDesc();
}
