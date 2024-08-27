package com.example.Reward.Receipt.Service;

import com.example.Reward.Receipt.Dto.out.GetEnterpriseResponseDTO;
import com.example.Reward.Receipt.Entity.Event;
import com.example.Reward.Receipt.Repository.EventRepository;
import com.example.Reward.Receipt.Repository.PopupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final EventRepository eventRepository;
    private final PopupRepository popupRepository;

    public GetEnterpriseResponseDTO findEnterprises() {
        String popupType = "영수증";
        Long memberId = 1L;
        Long checked = popupRepository.exists(popupType, memberId);
        Boolean popupChecked = checked > 0;

        List<String> enterpriseList = new ArrayList<>();
        List<Event> eventEnterprises = eventRepository.findEventIdAndEnterpriseNameByRewardAmount();
        for(Event event : eventEnterprises) {
            enterpriseList.add(event.getEnterpriseName());
        }

        return GetEnterpriseResponseDTO
                .builder()
                .popupChecked(popupChecked)
                .enterprises(enterpriseList)
                .build();
    }
}
