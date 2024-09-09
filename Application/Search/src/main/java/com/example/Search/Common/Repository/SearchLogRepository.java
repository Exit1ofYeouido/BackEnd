package com.example.Search.Common.Repository;

import com.example.Search.Common.Entity.SearchLog;
import com.example.Search.Log.Dto.out.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchLogRepository extends JpaRepository<SearchLog,Long> {

    @Query("SELECT new com.example.Search.Log.Dto.out.MemberStockCountDto(CAST(s.searchTime AS date), COUNT(s)) " +
            "FROM SearchLog s " +
            "WHERE FUNCTION('YEAR', CAST(s.searchTime AS date)) = :year AND FUNCTION('MONTH', CAST(s.searchTime AS date)) = :month " +
            "AND s.memberId = :memberId And s.enterpriseName = :enterpriseName " +
            "GROUP BY CAST(s.searchTime AS date) " +
            "ORDER BY CAST(s.searchTime AS date)")
    List<MemberStockCountDto> countByMemberIdAndEnterpriseNameWithYearAndMonth(Long memberId, String enterpriseName, int year, int month);

    @Query("SELECT new com.example.Search.Log.Dto.out.MemberStockCountByYearDto(FUNCTION('MONTH', CAST(s.searchTime AS date)), COUNT(s)) " +
            "FROM SearchLog s " +
            "WHERE FUNCTION('YEAR', CAST(s.searchTime AS date)) = :year " +
            "AND s.memberId = :memberId And s.enterpriseName = :enterpriseName " +
            "GROUP BY FUNCTION('MONTH', CAST(s.searchTime AS date)) " +
            "ORDER BY FUNCTION('MONTH', CAST(s.searchTime AS date))")
    List<MemberStockCountByYearDto> countByMemberIdAndEnterpriseNameWithYear(Long memberId, String enterpriseName, int year);

    @Query("SELECT new com.example.Search.Log.Dto.out.MemberCountDto(s.enterpriseName, COUNT(s)) " +
            "FROM SearchLog s " +
            "WHERE s.memberId = :memberId " +
            "AND FUNCTION('YEAR', CAST(s.searchTime AS date)) = :year " +
            "AND FUNCTION('MONTH', CAST(s.searchTime AS date)) = :month " +
            "GROUP BY s.enterpriseName " +
            "ORDER BY COUNT(s) DESC")
    List<MemberCountDto> findByMemberIdWithYearAndMonth(Long memberId, int year, int month);

    @Query("SELECT new com.example.Search.Log.Dto.out.MemberCountDto(s.enterpriseName, COUNT(s)) " +
            "FROM SearchLog s " +
            "WHERE s.memberId = :memberId " +
            "AND FUNCTION('YEAR', CAST(s.searchTime AS date)) = :year " +
            "GROUP BY s.enterpriseName " +
            "ORDER BY COUNT(s) DESC")
    List<MemberCountDto> findByMemberIdWithYear(Long memberId, int year);

    @Query("SELECT new com.example.Search.Log.Dto.out.StockCountByYearDto(" +
            "FUNCTION('MONTH', CAST(s.searchTime AS date)), " +
            "COUNT(s), " +
            "SUM(CASE WHEN s.holding = true THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN s.holding = false THEN 1 ELSE 0 END)) " +
            "FROM SearchLog s " +
            "WHERE s.enterpriseName = :enterpriseName " +
            "AND FUNCTION('YEAR', CAST(s.searchTime AS date)) = :year " +
            "GROUP BY FUNCTION('MONTH', CAST(s.searchTime AS date)) " +
            "ORDER BY FUNCTION('MONTH', CAST(s.searchTime AS date))")
    List<StockCountByYearDto> findByEnterpriseNameWithYear(String enterpriseName, int year);

    @Query("SELECT new com.example.Search.Log.Dto.out.StockCountDto(" +
            "CAST(s.searchTime AS date), " +
            "COUNT(s), " +
            "SUM(CASE WHEN s.holding = true THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN s.holding = false THEN 1 ELSE 0 END)) " +
            "FROM SearchLog s " +
            "WHERE s.enterpriseName = :enterpriseName " +
            "AND FUNCTION('YEAR', CAST(s.searchTime AS date)) = :year " +
            "AND FUNCTION('MONTH', CAST(s.searchTime AS date)) = :month " +
            "GROUP BY CAST(s.searchTime AS date) " +
            "ORDER BY CAST(s.searchTime AS date)")
    List<StockCountDto> findByEnterpriseNameWithYearAndMonth(String enterpriseName, int year, int month);
}
