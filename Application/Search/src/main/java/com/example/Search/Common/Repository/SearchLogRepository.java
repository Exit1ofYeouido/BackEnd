package com.example.Search.Common.Repository;

import com.example.Search.Common.Entity.SearchLog;
import com.example.Search.Log.Dto.out.GetDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchLogRepository extends JpaRepository<SearchLog,Long> {

    @Query("SELECT s.searchTime FROM SearchLog s WHERE FUNCTION('YEAR',s.searchTime)= :year AND FUNCTION('MONTH',s.searchTime)= :month AND s.code=:code ")
    List<GetDate> findByYearAndMonth(@Param("code") String code, @Param("year") Integer year, @Param("month") Integer month);


    @Query("SELECT s.searchTime FROM SearchLog s   WHERE FUNCTION('YEAR',s.searchTime)= :year AND FUNCTION('MONTH',s.searchTime)= :month AND s.code=:code AND s.holding=True  ")
    List<GetDate> findByGetStockLogs(String code, Integer year, Integer month);


}
