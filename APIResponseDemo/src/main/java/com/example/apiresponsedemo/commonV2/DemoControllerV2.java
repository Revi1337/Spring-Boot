package com.example.apiresponsedemo.commonV2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoControllerV2 {

    @GetMapping("/success-single-result2")
    public ResponseEntity<APIResponseV2<String>> successSingle() {
        return APIResponseV2.CREATED("success???");
    }

    @GetMapping("/success-multiple-result2")
    public ResponseEntity<APIResponseV2<List<String>>> successMultiple() {
        return APIResponseV2.OK(List.of("1", "2", "3", "4"));
    }

    @GetMapping("/fail-404-response2")
    public ResponseEntity<APIResponseV2<List<String>>> response404() {
        return APIResponseV2.NOT_FOUND(List.of("1", "2", "3", "4"));
    }

    @GetMapping("/fail-401-response2")
    public ResponseEntity<APIResponseV2<List<String>>> response401() {
        return APIResponseV2.UNAUTHORIZED(List.of("1", "2", "3", "4"));
    }

    @GetMapping("/fail-403-response2")
    public ResponseEntity<APIResponseV2<List<String>>> response403() {
        return APIResponseV2.FORBIDDEN(List.of("1", "2", "3", "4"));
    }
}
