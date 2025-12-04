package org.growith.be.growith.global.error.exception.handler;

import org.growith.be.growith.global.error.code.BaseErrorCode;
import org.growith.be.growith.global.error.exception.GeneralException;

public class CommentException extends GeneralException {

    public CommentException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
