package com.example.Basic.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/basics")
@RestController
public class BasicController {

    @GetMapping("/hi")
    @Operation(description = "이건 swagger 설명임 ㅇㅇ 그냥 보면됨 ㅇㅇㅇ")
    public ResponseEntity<?> gethi(){

        return ResponseEntity.ok("hi");
    }


}
