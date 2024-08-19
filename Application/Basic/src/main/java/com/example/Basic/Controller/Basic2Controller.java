package com.example.Basic.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/basic2")
@RestController
@Tag(name="기본 2")
public class Basic2Controller {

    @GetMapping
    @Operation(description = "오~ 데스페라도 ~ why dont u ~")
    public void eagles(){
        System.out.println("이글스의 호텔 켈리포니아.");
    }
}
