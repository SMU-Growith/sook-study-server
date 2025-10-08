package org.growith.be.growith.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.growith.be.growith.global.error.code.BaseErrorCode;
import org.growith.be.growith.global.error.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason(){
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}
