package com.example.Enterprise.Repository;

import com.example.Enterprise.Entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock,Long> {
}
