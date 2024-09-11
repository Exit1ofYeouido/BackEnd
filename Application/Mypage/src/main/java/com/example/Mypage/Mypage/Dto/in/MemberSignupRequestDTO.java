package com.example.Mypage.Mypage.Dto.in;

import com.example.Mypage.Common.Entity.Member;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import lombok.Data;

@Data
public class MemberSignupRequestDTO {

    private String name;

    private String memberName;

    private String memberPassword;

    private String phoneNumber;

    @Email
    private String email;

    private String birth;

    private Integer sex;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .birth(convertStringToDate(birth, sex))
                .phoneNumber(phoneNumber)
                .email(email)
                .sex(convertGenderNumberToGenderCode(sex))
                .memberName(memberName)
                .role("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Date convertStringToDate(String date, Integer number) {
        String yearPrefix = (number <= 2) ? "19" : "20";
        
        if (number > 4) {
            throw new DateTimeParseException("입력하신 생년월일을 확인하세요", date, 0);
        }

        String formattedDate = yearPrefix + date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate localDate = LocalDate.parse(formattedDate, formatter);
            return Date.from(localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(e.getMessage(), date, 0);
        }

    }

    private Character convertGenderNumberToGenderCode(Integer genderNumber) {
        if (genderNumber % 2 != 0) {
            return 'M';
        }
        return 'F';
    }
}
