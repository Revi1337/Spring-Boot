package com.example.apiresponsedemo.commonV1;


import java.util.Collection;
import java.util.List;

public record APIResponseV1<T> (
        int flag,
        int totalResults,
        T result
) {

    public static <T> APIResponseV1<T> OK(T result) {
        return new APIResponseV1<>(
                APIStatusCodeV1.OK, List.of(result).size(), result
        );
    }

    public static <T extends Collection<?>> APIResponseV1<T> OK(T results) {
        return new APIResponseV1<>(
                APIStatusCodeV1.OK, results.size(), results
        );
    }

    public static <T> APIResponseV1<T> CREATED(T result) {
        return new APIResponseV1<>(
                APIStatusCodeV1.CREATED, List.of(result).size(), result
        );
    }

    public static <T> APIResponseV1<T> BAD_REQUEST(T error) {
        return new APIResponseV1<>(
                APIStatusCodeV1.BAD_REQUEST, List.of(error).size(), error
        );
    }

    public static <T> APIResponseV1<T> NOT_FOUND(T error) {
        return new APIResponseV1<>(
                APIStatusCodeV1.NOT_FOUND, List.of(error).size(), error
        );
    }

    public static <T> APIResponseV1<T> FORBIDDEN(T error) {
        return new APIResponseV1<>(
                APIStatusCodeV1.FORBIDDEN, List.of(error).size(), error
        );
    }

    public static <T> APIResponseV1<T> UNAUTHORIZED(T error) {
        return new APIResponseV1<>(
                APIStatusCodeV1.UNAUTHORIZED, List.of(error).size(), error
        );
    }

}

