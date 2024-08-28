package com.example.Reward.Advertisement.Controller;

import com.example.Reward.Advertisement.Dto.in.GiveStockRequestDto;
import com.example.Reward.Advertisement.Dto.out.*;
import com.example.Reward.Advertisement.Service.AdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ad")
@Tag(name="광고 API")
@RequiredArgsConstructor
public class AdController {


    private final AdService adService;

    @GetMapping("/info")
    @Operation(description = "광고 의뢰한 기업들의 목록")
    public ResponseEntity<List<GetInfoResponseDto>> getInfo(@RequestHeader("memberId") String memberId){
        List<GetInfoResponseDto> getInfoResponseDtos=adService.getAdInfo(Long.valueOf(memberId));
        return ResponseEntity.ok(getInfoResponseDtos);
    }

    @GetMapping("/{media_id}")
    @Operation(description = "광고 영상 uri")
    public ResponseEntity<GetMediaUrlResponseDto> getAdUrl(@PathVariable Long media_id){
        GetMediaUrlResponseDto getMediaUrlResponseDto=adService.getMediaUrl(media_id);
        return ResponseEntity.ok(getMediaUrlResponseDto);
    }

    @GetMapping("/{media_id}/quiz")
    @Operation(description = "미디어 마다 quiz문제 와 답 출력")
    public ResponseEntity<GetQuizResponseDto> getQuiz(@PathVariable Long media_id){
        GetQuizResponseDto getQuizResponseDto=adService.getQuiz(media_id);
        return ResponseEntity.ok(getQuizResponseDto);
    }

    @PostMapping("/{media_id}/quiz")
    @Operation(description = "퀴즈 맞추고 주식 제공")
    public ResponseEntity<GiveStockResponseDto > saveQuiz(@PathVariable Long media_id, @RequestBody GiveStockRequestDto giveStockRequestDto,@RequestHeader("memberId") String memberId){
        //멤버 아이디 추가
        GiveStockResponseDto giveStockResponseDto =adService.giveStock(media_id,giveStockRequestDto,Long.valueOf(memberId));
        return ResponseEntity.ok(giveStockResponseDto);

    }

    @GetMapping("/detail")
    @Operation(description = "기업 세부정보 소개")
    public ResponseEntity<DetailEnterPriseResponseDto> detailEnterprise(@RequestParam ("enterpriseName") String enterpriseName){
        DetailEnterPriseResponseDto detailEnterPriseResponseDto=adService.getEnterpriseDetail(enterpriseName);
        return ResponseEntity.ok(detailEnterPriseResponseDto);
    }
}

