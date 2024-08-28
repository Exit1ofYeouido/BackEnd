package com.example.Mypage.Mypage.Controller;


import com.example.Mypage.Mypage.Dto.out.GetAllMyPageResponseDto;
import com.example.Mypage.Mypage.Dto.out.GetTutorialCheckResponseDto;
import com.example.Mypage.Mypage.Service.MyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="마이페이지 API")
@RestController
@RequestMapping("/page")
@RequiredArgsConstructor
public class MypageController {


    private final MyService myService;



    @GetMapping("/all")
    @Operation(description = "마이페이지 종합")
    public ResponseEntity<GetAllMyPageResponseDto> getAllMypage(){

        Long memid=1L;
        GetAllMyPageResponseDto getAllMyPageResponseDto =myService.getAllMyPage(memid);

        return ResponseEntity.ok(getAllMyPageResponseDto);


    }
    @GetMapping("/check")
    public ResponseEntity<?> getTutorialCheck(@RequestParam("type") String type){

        //가정
        Long memId=1L;
        GetTutorialCheckResponseDto getTutorialCheckResponseDto=myService.getTutorialCheck(type,memId);

        return ResponseEntity.ok(getTutorialCheckResponseDto);


    }

    @PostMapping("/notuto")
    public ResponseEntity<?> postTutorialCheck(@RequestParam("type") String type){

        //가정
        Long memid=1L;
        myService.saveTutorialCheck(type,memid);


        return ResponseEntity.ok("");
    }


}
