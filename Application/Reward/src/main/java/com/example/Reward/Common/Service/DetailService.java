package com.example.Reward.Common.Service;

import com.example.Reward.Common.Dto.DetailEnterPriseResponseDto;
import com.example.Reward.Common.Entity.Event;
import com.example.Reward.Common.Repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetailService {

    private final EventRepository eventRepository;

    @Transactional(readOnly=true)
    public DetailEnterPriseResponseDto getEnterpriseDetail(String enterpriseName) {
        Pageable pageable = PageRequest.of(0,1);
        List<Event> events=eventRepository.findByEnterpriseName(enterpriseName, pageable);
        return DetailEnterPriseResponseDto.of(events.get(0));
    }
}
