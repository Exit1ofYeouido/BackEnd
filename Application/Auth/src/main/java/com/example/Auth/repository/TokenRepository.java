package com.example.Auth.repository;

import com.example.Auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<RefreshToken, String> {
}
