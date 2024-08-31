package com.example.Reward.Advertisement.Service;


import com.example.Reward.Advertisement.Dto.in.GiveStockRequestDto;
import com.example.Reward.Advertisement.Dto.out.*;
import com.example.Reward.Advertisement.Entity.CheckToday;
import com.example.Reward.Advertisement.Entity.MediaHistory;
import com.example.Reward.Advertisement.Entity.MediaLink;
import com.example.Reward.Advertisement.Entity.Quiz;
import com.example.Reward.Advertisement.Exception.NoStockException;
import com.example.Reward.Advertisement.Exception.NotMatchedEnterpriseName;
import com.example.Reward.Advertisement.Kafka.Service.KafkaOutputService;
import com.example.Reward.Advertisement.Repository.CheckTodayRepository;
import com.example.Reward.Advertisement.Repository.MediaHistoryRepository;
import com.example.Reward.Advertisement.Repository.MediaLinkRepository;
import com.example.Reward.Advertisement.Repository.QuizRepository;
import com.example.Reward.Advertisement.Webclient.ApiService;
import com.example.Reward.Advertisement.Webclient.ResultDto;
import com.example.Reward.Common.Entity.Event;
import com.example.Reward.Common.Repository.EventRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdService {

    private final MediaLinkRepository mediaLinkRepository;
    private final QuizRepository quizRepository;
    private final MediaHistoryRepository mediaHistoryRepository;
    private final CheckTodayRepository checkTodayRepository;
    private final EventRepository eventRepository;
    private final KafkaOutputService kafkaOutputService;
    private final ApiService apiservice;


    @Transactional(readOnly=true)
    public List<GetInfoResponseDto> getAdInfo(Long memId) {
        List<String> enterpriseNoneList=new ArrayList<>();
        List<Long> mediaLinkIdList=new ArrayList<>();

        List<CheckToday> checkToday=checkTodayRepository.findBymemberId(memId);
        List<MediaHistory> mediaHistory=mediaHistoryRepository.findBymemberId(memId);

        for (CheckToday check:checkToday){
            enterpriseNoneList.add(check.getEnterpriseName());
        }


        for (MediaHistory media:mediaHistory){
            mediaLinkIdList.add(media.getMediaLink().getId());
        }

        List<MediaLink> mediaLinks=mediaLinkRepository.findmedialink(mediaLinkIdList,enterpriseNoneList);

        Collections.shuffle(mediaLinks);
        List<GetInfoResponseDto> getInfoResponseDtos=new ArrayList<>();


        List<String> lists=new ArrayList<>();
        for (MediaLink medialink:mediaLinks){
            if (!lists.contains(medialink.getEnterpriseName())) {
                GetInfoResponseDto getInfoResponseDto = GetInfoResponseDto
                        .builder()
                        .name(medialink.getEnterpriseName())
                        .thumbnail(medialink.getThumnail())
                        .mediaId(medialink.getId())
                        .thumbnailName(medialink.getThumnailName())
                        .build();

                lists.add(medialink.getEnterpriseName());
                getInfoResponseDtos.add(getInfoResponseDto);
            }
        }

        Collections.shuffle(getInfoResponseDtos);


        return getInfoResponseDtos;

    }

    @Transactional(readOnly=true)
    public GetMediaUrlResponseDto getMediaUrl(Long mediaId) {
        Optional<MediaLink> mediaLink=mediaLinkRepository.findById(mediaId);
        mediaLink.orElseThrow(()-> new RuntimeException());
        return GetMediaUrlResponseDto.of(mediaLink);
    }


    @Transactional(readOnly=true)
    public GetQuizResponseDto getQuiz(Long mediaId) {
        Quiz quiz=quizRepository.findByMediaLinkId(mediaId);
        return GetQuizResponseDto.of(quiz);
    }


    @Transactional
    public GiveStockResponseDto giveStock(Long mediaId, GiveStockRequestDto giveStockRequestDto,Long memId) {



        //미디어 시청내역 체크  (중복제외 로직 포함)

        Optional<MediaLink> mediaLink=mediaLinkRepository.findById(mediaId);

        MediaHistory mediaHistory=mediaHistoryRepository.findByMemberIdAndMediaLinkId(memId,mediaLink.get().getId());
        if (mediaHistory ==null) {
            MediaHistory new_mediaHistory = MediaHistory.builder().memberId(memId).mediaLink(mediaLink.get()).build();
            mediaHistoryRepository.save(new_mediaHistory);
        }


        //오늘봤던 checkToday 체크 (중복제외 로직 포함)
        CheckToday checkToday=checkTodayRepository.findByEnterpriseNameAndMemberId(giveStockRequestDto.getEnterpriseName(),memId);
        if (checkToday ==null) {
            CheckToday new_checkToday = CheckToday.builder().enterpriseName(giveStockRequestDto.getEnterpriseName()).memberId(memId).build();
            checkTodayRepository.save(new_checkToday);
            checkToday=new_checkToday;
        }
        if (!checkToday.getEnterpriseName().equals(giveStockRequestDto.getEnterpriseName())){
            throw new NotMatchedEnterpriseName(checkToday.getEnterpriseName(),giveStockRequestDto.getEnterpriseName());
        }


        //만일 된다면 주식 가격을 불러오고 주식 100원마치 0.x주를 줘야함+event 삭제

        Event event=eventRepository.findByEnterpriseNameContaining(giveStockRequestDto.getEnterpriseName());

        //주식 이벤트 리워드 0 이하면 에러 return
        if (event.getRewardAmount()<=0){
            throw new NoStockException();
        }

        ResultDto resultDto=apiservice.getPrise(event.getStockCode());
        event.setRewardAmount(event.getRewardAmount()-resultDto.getAmount());
        eventRepository.save(event);


        //카프카로 멤버한테 요청해서 stock을 줌(완)

        kafkaOutputService.giveStock(memId,resultDto.getAmount(),event.getStockCode(),event.getEnterpriseName(),resultDto.getCost());

        return GiveStockResponseDto.givetostock(giveStockRequestDto.getEnterpriseName(),resultDto.getAmount());

    }

    @Transactional(readOnly=true)
    public DetailEnterPriseResponseDto getEnterpriseDetail(String enterpriseName) {

        Event event=eventRepository.findByEnterpriseNameContaining(enterpriseName);
        return DetailEnterPriseResponseDto.of(event);
    }
}
