package com.example.Home.HomeController;

import com.example.Home.HomeDTO.HomeResponseDTO;
import com.example.Home.HomeService.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;


    @GetMapping("/home")
    public ResponseEntity<HomeResponseDTO> getHomeData(@RequestHeader("memberId") String memberId) {
        HomeResponseDTO response = homeService.getHomeData(Long.valueOf(memberId));
        return ResponseEntity.ok().body(response);
    }
}
