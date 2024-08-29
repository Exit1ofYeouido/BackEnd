package com.example.Reward.Advertisement.Exception;

import com.example.Reward.Common.Response.CommonResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdResponse extends CommonResponse {

    private final Object data;

    private final Integer total;

    @Builder
    public AdResponse(int status, String message, Object data, Integer total) {
        super(status, message);
        this.data = data;
        this.total = total;
    }



    public static AdResponse error(String message) {
        return new AdResponse(400, message, null, null);
    }


}

