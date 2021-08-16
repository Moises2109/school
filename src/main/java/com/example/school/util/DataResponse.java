package com.example.school.util;

import com.example.school.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse<T> {
    private T data;
    private ErrorCode errorCode;
    private String message;

    public static DataResponse successNoData() {
        return DataResponse.builder().build();
    }

    public static <T> DataResponse<T> successWithData(T data) {
        return DataResponse.<T>builder()
                .data(data)
                .build();
    }

    public static DataResponse error(ErrorCode errorCode, String message) {
        return DataResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .build();
    }
}
