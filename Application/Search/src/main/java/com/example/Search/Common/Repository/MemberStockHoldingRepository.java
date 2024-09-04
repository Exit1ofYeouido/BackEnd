package com.example.Search.Common.Repository;

import com.example.Search.Common.Entity.MemberStockHolding;
import com.example.Search.Common.Entity.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberStockHoldingRepository extends JpaRepository<MemberStockHolding,Long> {


    @Query("SELECT m FROM memberStockHolding m WHERE FUNCTION('YEAR',m.searchTime)= :year AND FUNCTION('MONTH',m.searchTime)= :month AND m.code=:code ")
    List<SearchLog> findByYearAndMonth(@Param("code") String code,@Param("year") Integer year,@Param("month") Integer month);
}
