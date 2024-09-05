package com.example.Search.Common.Repository;

import com.example.Search.Common.Entity.SearchLog;
import com.example.Search.Log.Dto.out.MemberStockCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchLogRepository extends JpaRepository<SearchLog,Long> {

    @Query("SELECT s FROM SearchLog s WHERE substring(s.searchTime, 1, 4) = :year AND substring(s.searchTime, 6, 2) = :month AND s.enterpriseName = :enterpriseName")
    List<SearchLog> findByYearAndMonth(@Param("enterpriseName") String enterpriseName, @Param("year") String year, @Param("month") String month);



    @Query("SELECT s FROM SearchLog s  WHERE substring(s.searchTime,1,4)=:year AND substring(s.searchTime,6,2)=:month  AND s.enterpriseName=:enterpriseName AND s.holding=True  ")
    List<SearchLog> findByGetStockLogs(@Param("enterpriseName") String enterpriseName, @Param("year") String year, @Param("month") String month);


    @Query("SELECT s FROM SearchLog s  WHERE substring(s.searchTime,1,4)=:year  AND s.enterpriseName=:enterpriseName")
    List<SearchLog> findByYear(@Param("enterpriseName") String enterpriseName, @Param("year") String year);

    @Query("SELECT s FROM SearchLog s  WHERE substring(s.searchTime,1,4)=:year  AND s.enterpriseName=:enterpriseName AND s.holding=True")
    List<SearchLog> findBygetAllStockLogs(String enterpriseName, String year);

    @Query("SELECT new com.example.Search.Log.Dto.out.MemberStockCountDto(CAST(s.searchTime AS date), COUNT(s)) " +
            "FROM SearchLog s " +
            "WHERE FUNCTION('YEAR', CAST(s.searchTime AS date)) = :year AND FUNCTION('MONTH', CAST(s.searchTime AS date)) = :month " +
            "AND s.memberId = :memberId And s.enterpriseName = :enterpriseName " +
            "GROUP BY CAST(s.searchTime AS date) " +
            "ORDER BY CAST(s.searchTime AS date)")
    List<MemberStockCountDto> countByMemberIdAndEnterpriseName(Long memberId, String enterpriseName, int year, int month);
}
