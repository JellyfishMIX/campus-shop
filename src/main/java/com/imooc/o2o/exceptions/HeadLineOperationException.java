package com.imooc.o2o.exceptions;

public class HeadLineOperationException extends RuntimeException {
    private static final long serialVersionUID = -6866853564850051110L;

    public HeadLineOperationException(String errMsg) {
        super(errMsg);
    }
}
