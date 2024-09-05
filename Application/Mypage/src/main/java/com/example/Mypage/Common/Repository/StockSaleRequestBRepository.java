package com.example.Mypage.Common.Repository;

import com.example.Mypage.Common.Entity.StockSaleRequestA;
import com.example.Mypage.Common.Entity.StockSaleRequestB;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockSaleRequestBRepository extends JpaRepository<StockSaleRequestB, Long> {
    public List<StockSaleRequestB> findAllByMemberId(Long memberId);
}
