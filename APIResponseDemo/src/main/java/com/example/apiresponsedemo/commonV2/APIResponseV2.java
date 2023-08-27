package com.example.apiresponsedemo.commonV2;


import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;

public record APIResponseV2<T> (
        int flag,
        int totalResults,
        T result
) {

    public static <T> ResponseEntity<APIResponseV2<T>> OK(T result) {
        APIResponseV2<T> apiResponseV2 = new APIResponseV2<>(
                APIStatusCodeV2.OK, List.of(result).size(), result
        );
        return ResponseEntity.status(apiResponseV2.flag()).body(apiResponseV2);
    }

    public static <T extends Collection<?>> ResponseEntity<APIResponseV2<T>> OK(T results) {
        APIResponseV2<T> apiResponseV2 = new APIResponseV2<>(
                APIStatusCodeV2.OK, results.size(), results
        );
        return ResponseEntity.status(apiResponseV2.flag()).body(apiResponseV2);
    }

    public static <T> ResponseEntity<APIResponseV2<T>> CREATED(T result) {
        APIResponseV2<T> apiResponseV2 = new APIResponseV2<>(
                APIStatusCodeV2.CREATED, List.of(result).size(), result
        );
        return ResponseEntity.status(apiResponseV2.flag()).body(apiResponseV2);
    }

    public static <T> ResponseEntity<APIResponseV2<T>> BAD_REQUEST(T result) {
        APIResponseV2<T> apiResponseV2 = new APIResponseV2<>(
                APIStatusCodeV2.BAD_REQUEST, List.of(result).size(), result
        );
        return ResponseEntity.status(apiResponseV2.flag()).body(apiResponseV2);
    }

    public static <T> ResponseEntity<APIResponseV2<T>> NOT_FOUND(T result) {
        APIResponseV2<T> apiResponseV2 = new APIResponseV2<>(
                APIStatusCodeV2.NOT_FOUND, List.of(result).size(), result
        );
        return ResponseEntity.status(apiResponseV2.flag()).body(apiResponseV2);
    }

    public static <T> ResponseEntity<APIResponseV2<T>> UNAUTHORIZED(T result) {
        APIResponseV2<T> apiResponseV2 = new APIResponseV2<>(
                APIStatusCodeV2.UNAUTHORIZED, List.of(result).size(), result
        );
        return ResponseEntity.status(apiResponseV2.flag()).body(apiResponseV2);
    }

    public static <T> ResponseEntity<APIResponseV2<T>> FORBIDDEN(T result) {
        APIResponseV2<T> apiResponseV2 = new APIResponseV2<>(
                APIStatusCodeV2.FORBIDDEN, List.of(result).size(), result
        );
        return ResponseEntity.status(apiResponseV2.flag()).body(apiResponseV2);
    }

}

