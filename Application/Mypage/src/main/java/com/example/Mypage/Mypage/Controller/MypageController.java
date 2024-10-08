package com.example.Mypage.Mypage.Controller;


import com.example.Mypage.Common.Entity.MemberStock;
import com.example.Mypage.Mypage.Dto.out.GetAllMyPageResponseDto;
import com.example.Mypage.Mypage.Dto.out.GetTutorialCheckResponseDto;
import com.example.Mypage.Mypage.Service.MyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "마이페이지 API")
@RestController
@RequestMapping("/page")
@RequiredArgsConstructor
public class MypageController {

    private final MyService myService;

    @GetMapping("/all")
    @Operation(description = "마이페이지 종합")
    public ResponseEntity<GetAllMyPageResponseDto> getAllMypage(@RequestHeader("memberId") String memberId) {
        GetAllMyPageResponseDto getAllMyPageResponseDto = myService.getAllMyPage(Long.valueOf(memberId));
        return ResponseEntity.ok(getAllMyPageResponseDto);
    }

    @GetMapping("/check")
    @Operation(description = "팝업체크")
    public ResponseEntity<?> getTutorialCheck(@RequestParam("type") String type,
                                              @RequestHeader("memberId") String memberId) {
        GetTutorialCheckResponseDto getTutorialCheckResponseDto = myService.getTutorialCheck(type,
                Long.valueOf(memberId));
        return ResponseEntity.ok(getTutorialCheckResponseDto);
    }

    @PostMapping("/notuto")
    @Operation(description = "오늘 하루 안보기 ")
    public ResponseEntity<?> postTutorialCheck(@RequestParam("type") String type,
                                               @RequestHeader("memberId") String memberId) {
        myService.saveTutorialCheck(type, Long.valueOf(memberId));
        return ResponseEntity.ok("");
    }

    @GetMapping("/allstock")
    @Operation(description = "내가 가진 모든 주식 조회")
    public ResponseEntity<?> getAllStock(@RequestHeader("memberId") String memberId) {
        List<MemberStock> memberStocks = myService.getAllStock(Long.valueOf(memberId));
        return ResponseEntity.ok(memberStocks);
    }

}
