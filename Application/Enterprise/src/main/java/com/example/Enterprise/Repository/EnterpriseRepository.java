package com.example.Enterprise.Repository;

import com.example.Enterprise.Entity.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterpriseRepository extends JpaRepository<Enterprise,Long> {
}
