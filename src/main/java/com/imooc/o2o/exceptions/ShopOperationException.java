package com.imooc.o2o.exceptions;

/**
 * 对RuntimeException做一层薄薄的封装
 * 意义在于看到这样异常时，能知道是与店铺操作相关的
 */
public class ShopOperationException extends RuntimeException{
    private static final long serialVersionUID = 2361446884822298905L;

    public ShopOperationException(String msg) {
        super(msg); // super(), 父类的构造方法
    }
}
