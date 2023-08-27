package com.example.apiresponsedemo.commonV3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoControllerV3 {

    @GetMapping("/success-single-result3")
    public ResponseEntity<APIResponseV3<String>> successSingle() {
        return APIResponseV3.CREATED("success???");
    }

    @GetMapping("/success-multiple-result3")
    public ResponseEntity<APIResponseV3<List<String>>> successMultiple() {
        return APIResponseV3.OK(List.of("1", "2", "3", "4"));
    }

    @GetMapping("/fail-404-response3")
    public ResponseEntity<APIResponseV3<List<String>>> response404() {
        return APIResponseV3.NOT_FOUND(List.of("1", "2", "3", "4"));
    }

    @GetMapping("/fail-401-response3")
    public ResponseEntity<APIResponseV3<List<String>>> response401() {
        return APIResponseV3.UNAUTHORIZED(List.of("1", "2", "3", "4"));
    }

    @GetMapping("/fail-403-response3")
    public ResponseEntity<APIResponseV3<List<String>>> response403() {
        return APIResponseV3.FORBIDDEN(List.of("1", "2", "3", "4"));
    }

}
