package com.example.Auth.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MembernameCheckRequestDTO {

    @Size(min = 4, message = "아이디는 최소 4자리 이상이어야 합니다.")
    @Pattern(regexp = "^(?!\\d+$).*$", message = "아이디는 숫자만으로 구성될 수 없습니다.")
    private String username;
}
