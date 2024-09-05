package com.example.Reward.Receipt.Repository;

import com.example.Reward.Receipt.Entity.ReceiptLog;
import com.example.Reward.Receipt.Entity.ReceiptLogKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceiptLogRepository extends JpaRepository<ReceiptLog, ReceiptLogKey> {

    @Query("SELECT COUNT(R) FROM ReceiptLog R WHERE R.receiptLogKey.approvalNumber = :approvalNumber AND R.receiptLogKey.dealTime = :dealTime")
    int countByApprovalNumberAndDealTime(@Param("approvalNumber") String approvalNumber,
                                         @Param("dealTime") String dealTime);
}
