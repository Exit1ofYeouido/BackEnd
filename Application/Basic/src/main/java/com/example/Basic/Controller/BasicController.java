package com.example.Basic.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/basics")
@RestController
public class BasicController {

    @GetMapping("/hi")
    public ResponseEntity<?> gethi(){

        return ResponseEntity.ok("hi");
    }


}
