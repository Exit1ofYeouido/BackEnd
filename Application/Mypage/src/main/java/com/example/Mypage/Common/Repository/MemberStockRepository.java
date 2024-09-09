package com.example.Mypage.Common.Repository;

import com.example.Mypage.Common.Entity.MemberStock;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberStockRepository extends JpaRepository<MemberStock, Long> {
    MemberStock findByIdAndStockCode(Long memId, String code);

    @Query(nativeQuery = true, value = "SELECT * FROM member_stock m WHERE m.member_id = :memberId AND m.amount != 0")
    List<MemberStock> findByMemberId(@Param("memberId") Long memId);

    @Query(nativeQuery = true, value = "SELECT * FROM member_stock m WHERE :enterpriseName = m.stock_name AND m.member_id = :memId")
    MemberStock findByStockNameAndMember(@Param("enterpriseName") String enterpriseName, @Param("memId") Long memId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MemberStock> findByMemberIdAndStockCode(Long memId, String code);
}
