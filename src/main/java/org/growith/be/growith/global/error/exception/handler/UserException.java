package org.growith.be.growith.global.error.exception.handler;

import org.growith.be.growith.global.error.code.BaseErrorCode;
import org.growith.be.growith.global.error.exception.GeneralException;

public class UserException extends GeneralException {
    public UserException(BaseErrorCode baseErrorCode){
        super(baseErrorCode);
    }
}
