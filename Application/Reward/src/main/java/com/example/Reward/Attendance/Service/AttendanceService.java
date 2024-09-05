package com.example.Reward.Attendance.Service;

import com.example.Reward.Advertisement.Kafka.Dto.Stock;
import com.example.Reward.Attendance.Dto.out.AttendResponseDTO;
import com.example.Reward.Attendance.Dto.out.GetAttendanceResponseDTO;
import com.example.Reward.Attendance.Dto.out.Reward;
import com.example.Reward.Attendance.Dto.out.StockInfoDTO;
import com.example.Reward.Attendance.Entity.Attendance;
import com.example.Reward.Attendance.Exception.AlreadyAttendedException;
import com.example.Reward.Attendance.Repository.AttendanceRepository;
import com.example.Reward.Common.Entity.Event;
import com.example.Reward.Common.Repository.EventRepository;
import com.example.Reward.Common.Service.GiveStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EventRepository eventRepository;
    private final GiveStockService giveStockService;

    public GetAttendanceResponseDTO findAttendInfo(Long memberId) {
        Attendance attendance = attendanceRepository.findByMemberId(memberId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate recentAttendDate = LocalDate.parse(attendance.getRecent(), formatter);
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        boolean isChecked = recentAttendDate.equals(today);
        int recentMonth = recentAttendDate.getMonthValue();
        int count = (currentMonth==recentMonth) ? attendance.getCount() : 0;

        GetAttendanceResponseDTO getAttendanceResponseDTO = GetAttendanceResponseDTO.builder()
                .isChecked(isChecked)
                .attendCount(count)
                .month(currentMonth)
                .build();

        return getAttendanceResponseDTO;
    }

    @Transactional
    public Boolean attend(Long memberId) {
        Attendance attendance = attendanceRepository.findByMemberId(memberId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate recentAttendDate = LocalDate.parse(attendance.getRecent(), formatter);
        LocalDate today = LocalDate.now();
        if(recentAttendDate.equals(today)) {
            throw new AlreadyAttendedException();
        }
        int currentMonth = today.getMonthValue();
        int recentMonth = recentAttendDate.getMonthValue();
        attendance.setRecent(today.toString());
        attendance.setCount((currentMonth==recentMonth) ? attendance.getCount()+1 : 1);
        attendanceRepository.save(attendance);
        return attendance.getCount() % 5 == 0;
    }

    public StockInfoDTO getRandomStock(Long memberId) {
        List<Event> eventList;
        eventList = eventRepository.findByRewardAmountLessThan(1L);
        if(eventList.isEmpty()) eventList = eventRepository.findByRewardAmountGreaterThanEqual(1L);
        Event randomStock = selectRandomEvent(eventList);
        StockInfoDTO stockInfoDTO = new StockInfoDTO(randomStock.getId(), randomStock.getContent().getId(), randomStock.getEnterpriseName(), randomStock.getStockCode());
        return stockInfoDTO;
    }

    private Event selectRandomEvent(List<Event> eventList) {
        Random random = new Random();
        int randomIndex = random.nextInt(eventList.size());
        return eventList.get(randomIndex);
    }

    @Transactional
    public AttendResponseDTO giveStock(Long memberId, StockInfoDTO stockInfoDTO, Integer priceOfStock, Double amountOfStock) {
        giveStockService.giveStock(memberId, stockInfoDTO.getEnterpriseName(), stockInfoDTO.getContentId(), priceOfStock, amountOfStock);
        Event event = eventRepository.findByEnterpriseNameContainingAndContentId(stockInfoDTO.getEnterpriseName(), stockInfoDTO.getContentId());
        event.setRewardAmount(event.getRewardAmount()-amountOfStock);
        eventRepository.save(event);
        Reward reward = Reward.builder()
                .enterpriseName(stockInfoDTO.getEnterpriseName())
                .amount(amountOfStock)
                .build();
        AttendResponseDTO attendResponseDTO = AttendResponseDTO.builder()
                .hasReward(true)
                .reward(reward)
                .build();
        return attendResponseDTO;
    }
}
