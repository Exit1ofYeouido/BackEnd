package com.example.Home.HomeDTO;

import lombok.Getter;

@Getter
public class HomeResponseDTO {
    private int totalPoint;
    private int totalStock;
    private int totalAssets;
    private Double totalEarningRate;
    private String brandLogoUri;

    public HomeResponseDTO(int totalPoint, int totalStock, int totalAssets, double totalEarningRate, String brandLogoUri) {
        this.totalPoint = totalPoint;
        this.totalStock = totalStock;
        this.totalAssets = totalAssets;
        this.totalEarningRate = totalEarningRate;
        this.brandLogoUri = brandLogoUri;
    }
}