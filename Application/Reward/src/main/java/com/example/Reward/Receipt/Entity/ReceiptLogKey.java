package com.example.Reward.Receipt.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptLogKey implements Serializable {
    @Column
    private String approvalNum;
    @Column
    private String dealTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReceiptLogKey)) return false;
        ReceiptLogKey that = (ReceiptLogKey) o;
        return approvalNum.equals(that.approvalNum) && dealTime.equals(that.dealTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(approvalNum, dealTime);
    }
}
