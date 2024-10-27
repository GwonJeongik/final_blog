package com.blog.domain.common.exception.user;

import com.blog.domain.common.exception.GlobalException;
import com.blog.domain.common.exception.GlobalExceptionConst;

public class NotFoundEmailException extends GlobalException {

    public NotFoundEmailException() {
        super(GlobalExceptionConst.NOT_FOUND_EMAIL);
    }
}
