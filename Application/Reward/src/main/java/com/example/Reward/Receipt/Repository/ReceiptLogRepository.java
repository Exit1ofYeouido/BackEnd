package com.example.Reward.Receipt.Repository;

import com.example.Reward.Receipt.Entity.ReceiptLog;
import com.example.Reward.Receipt.Entity.ReceiptLogKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptLogRepository extends JpaRepository<ReceiptLog, ReceiptLogKey> {

}
