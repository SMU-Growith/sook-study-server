package org.growith.be.growith.global.error;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.growith.be.growith.global.error.code.BaseCode;
import org.growith.be.growith.global.error.code.status.SuccessCode;

@Getter
@AllArgsConstructor
@JsonPropertyOrder()
public class ApiResponse<T> {

    //    @JsonProperty
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, SuccessCode._OK.getCode(),  SuccessCode._OK.getMessage(), result);
    }

    public static <T> ApiResponse<T> of (BaseCode code, T result){
        return new ApiResponse<>(true, code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), result);
    }

    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }
}
