package com.imooc.o2o.exceptions;

public class PersonInfoOperationException extends RuntimeException {
    private static final long serialVersionUID = -5295089210799298853L;

    public PersonInfoOperationException(String errMsg) {
        super(errMsg);
    }
}
