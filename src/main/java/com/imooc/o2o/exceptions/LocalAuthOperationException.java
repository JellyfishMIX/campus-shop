package com.imooc.o2o.exceptions;

public class LocalAuthOperationException extends RuntimeException {
    private static final long serialVersionUID = -8260236137099919700L;

    public LocalAuthOperationException(String errMsg) {
        super(errMsg);
    }
}
