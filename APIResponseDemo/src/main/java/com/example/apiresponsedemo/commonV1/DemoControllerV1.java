package com.example.apiresponsedemo.commonV1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoControllerV1 {

    @GetMapping("/success-single-result1")
    public ResponseEntity<APIResponseV1<String>> successSingle() {
        APIResponseV1<String> apiResponseV1 = APIResponseV1.CREATED("success???");
        return ResponseEntity
                .status(apiResponseV1.flag())
                .body(apiResponseV1);
    }

    @GetMapping("/success-multiple-result1")
    public ResponseEntity<APIResponseV1<List<String>>> successMultiple() {
        APIResponseV1<List<String>> apiResponseV1 = APIResponseV1.OK(List.of("1", "2", "3", "4"));
        return ResponseEntity
                .status(apiResponseV1.flag())
                .body(apiResponseV1);
    }

    @GetMapping("/fail-404-response1")
    public ResponseEntity<APIResponseV1<List<String>>> response404() {
        APIResponseV1<List<String>> apiResponseV1 = APIResponseV1.NOT_FOUND(List.of("1", "2", "3", "4"));
        return ResponseEntity
                .status(apiResponseV1.flag())
                .body(apiResponseV1);
    }

    @GetMapping("/fail-401-response1")
    public ResponseEntity<APIResponseV1<List<String>>> response401() {
        APIResponseV1<List<String>> apiResponseV1 = APIResponseV1.UNAUTHORIZED(List.of("1", "2", "3", "4"));
        return ResponseEntity
                .status(apiResponseV1.flag())
                .body(apiResponseV1);
    }

    @GetMapping("/fail-403-response1")
    public ResponseEntity<APIResponseV1<List<String>>> response403() {
        APIResponseV1<List<String>> apiResponseV1 = APIResponseV1.FORBIDDEN(List.of("1", "2", "3", "4"));
        return ResponseEntity
                .status(apiResponseV1.flag())
                .body(apiResponseV1);
    }
}
