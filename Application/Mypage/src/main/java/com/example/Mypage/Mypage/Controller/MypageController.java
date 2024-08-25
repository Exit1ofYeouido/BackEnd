package com.example.Mypage.Mypage.Controller;


import com.example.Mypage.Mypage.Dto.out.GetAllMyPageResponseDto;
import com.example.Mypage.Mypage.Service.MyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="마이페이지 API")
@RestController
@RequestMapping("/page")
@RequiredArgsConstructor
public class MypageController {


    private final MyService myService;



    @GetMapping("/all")
    @Operation(description = "마이페이지 종합")
    public ResponseEntity<?> getAllMypage(){

        Long memid=1L;
        GetAllMyPageResponseDto getAllMyPageResponseDto =myService.getAllMyPage(memid);

        return ResponseEntity.ok(getAllMyPageResponseDto);


    }


}
