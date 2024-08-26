package com.example.Reward.Receipt.Dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetEnterpriseResponseDTO {

    private boolean popupChecked;

    private List<String> enterprises;

}
