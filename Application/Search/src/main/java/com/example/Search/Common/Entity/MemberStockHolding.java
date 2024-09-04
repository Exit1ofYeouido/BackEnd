package com.example.Search.Common.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberStockHolding {
    @EmbeddedId
    private HoldingLogKey holdingLogKey;


    @ManyToOne
    @MapsId("code")
    @JoinColumn(name = "code", referencedColumnName = "code")
    Stock stock;
}
