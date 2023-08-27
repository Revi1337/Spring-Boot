package com.example.apiresponsedemo.commonV3;


import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;

public record APIResponseV3<T> (
        int flag,
        int totalResults,
        T result
) {

    public static <T> ResponseEntity<APIResponseV3<T>> OK(T result) {
        return createResponse(
                new APIResponseV3<>(
                        APIStatusCodeV3.OK,
                        List.of(result).size(),
                        result
                )
        );
    }

    public static <T extends Collection<?>> ResponseEntity<APIResponseV3<T>> OK(T results) {
        return createResponse(
                new APIResponseV3<>(
                        APIStatusCodeV3.OK,
                        results.size(),
                        results
                )
        );
    }

    public static <T> ResponseEntity<APIResponseV3<T>> CREATED(T result) {
        return createResponse(
                new APIResponseV3<>(
                        APIStatusCodeV3.CREATED,
                        List.of(result).size(),
                        result
                )
        );
    }

    public static <T> ResponseEntity<APIResponseV3<T>> BAD_REQUEST(T result) {
        return createResponse(
                new APIResponseV3<>(
                        APIStatusCodeV3.BAD_REQUEST,
                        List.of(result).size(),
                        result
                )
        );
    }

    public static <T> ResponseEntity<APIResponseV3<T>> NOT_FOUND(T result) {
        return createResponse(
                new APIResponseV3<>(
                        APIStatusCodeV3.NOT_FOUND,
                        List.of(result).size(),
                        result
                )
        );
    }

    public static <T> ResponseEntity<APIResponseV3<T>> UNAUTHORIZED(T result) {
        return createResponse(
                new APIResponseV3<>(
                        APIStatusCodeV3.UNAUTHORIZED,
                        List.of(result).size(),
                        result
                )
        );
    }

    public static <T> ResponseEntity<APIResponseV3<T>> FORBIDDEN(T result) {
        return createResponse(
                new APIResponseV3<>(
                        APIStatusCodeV3.FORBIDDEN,
                        List.of(result).size(),
                        result
                )
        );
    }

    private static <T> ResponseEntity<APIResponseV3<T>> createResponse(APIResponseV3<T> apiResponse)  {
        return ResponseEntity
                .status(apiResponse.flag())
                .body(apiResponse);
    }

}

