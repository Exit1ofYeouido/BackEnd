package com.example.Search.Search.Common.Repository;

import com.example.Search.Search.Common.Entity.Stock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock,String> {
    @Query(value = "SELECT * FROM mo.stock s WHERE s.code NOT IN :excludeCodes ORDER BY RAND() LIMIT :limitCount", nativeQuery = true)
    List<Stock> findRandomStocksExcluding(@Param("excludeCodes") List<String> excludeCodes, @Param("limitCount") int limitCount);

    @Query(value = "SELECT s FROM Stock s WHERE s.name LIKE %:keyword% ORDER BY s.name")
    List<Stock> findSimilarStocks(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT s FROM Stock s ORDER BY RAND()")
    List<Stock> findRandomStocks(Pageable pageable);
}