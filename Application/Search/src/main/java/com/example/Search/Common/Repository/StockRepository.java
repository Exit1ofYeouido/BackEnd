package com.example.Search.Common.Repository;

import com.example.Search.Common.Entity.Stock;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, String> {
    @Query(value = "SELECT s FROM Stock s WHERE s.code NOT IN :excludeCodes ORDER BY RAND() LIMIT :limitCount")
    List<Stock> findRandomStocksExcluding(@Param("excludeCodes") List<String> excludeCodes,
                                          @Param("limitCount") int limitCount);

    @Query(value = "SELECT s FROM Stock s WHERE s.name LIKE %:keyword% ORDER BY s.name")
    Page<Stock> findSimilarStocks(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT s FROM Stock s ORDER BY RAND()")
    List<Stock> findRandomStocks(Pageable pageable);

    Stock findByCode(String code);

    Stock findByName(String enterpriseName);
}