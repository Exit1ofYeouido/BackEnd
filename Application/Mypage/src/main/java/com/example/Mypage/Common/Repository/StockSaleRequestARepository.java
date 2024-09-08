package com.example.Mypage.Common.Repository;

import com.example.Mypage.Common.Entity.StockSaleRequestA;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockSaleRequestARepository extends JpaRepository<StockSaleRequestA, Long> {

    public List<StockSaleRequestA> findAllByMemberId(Long memberId);
}
