package com.example.Search.Common.Repository;

import com.example.Search.Common.Entity.MemberStockHolding;
import com.example.Search.Common.Entity.SearchLog;
import com.example.Search.Log.Dto.out.GetDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberStockHoldingRepository extends JpaRepository<MemberStockHolding,Long> {



}