package com.example.Home.Kis;

public class ApiResponseDTO {
    private Output1 output1;

    public static class Output1 {
        private String stck_prpr;

        public String getStck_prpr() {
            return stck_prpr;
        }
    }

    public Output1 getOutput1() {
        return output1;
    }
}
