package com.example.Mypage.Common.Repository;

import com.example.Mypage.Common.Entity.CompletedStockSale;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompletedStockSaleRepository extends JpaRepository<CompletedStockSale, Long> {

    @Query("SELECT c FROM CompletedStockSale c WHERE c.stockCode = :stockCode AND c.soldTime = :startTime")
    CompletedStockSale findByStockCodeAndSoldTime(@Param("stockCode") String stockCode,
                                                  @Param("startTime") LocalDateTime startTime);

}
