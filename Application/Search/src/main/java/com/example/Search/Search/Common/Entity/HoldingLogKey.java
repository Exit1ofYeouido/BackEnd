package com.example.Search.Search.Common.Entity;

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
public class HoldingLogKey implements Serializable {
    private String memberId;
    private String code;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoldingLogKey)) return false;
        HoldingLogKey that = (HoldingLogKey) o;
        return memberId.equals(that.memberId) && code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, code);
    }
}
