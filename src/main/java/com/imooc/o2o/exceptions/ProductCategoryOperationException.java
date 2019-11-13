package com.imooc.o2o.exceptions;

/**
 * 对RuntimeException做一层薄薄的封装
 * 意义在于看到这样异常时，能知道是与商品种类操作相关的
 */
public class ProductCategoryOperationException extends RuntimeException {
    private static final long serialVersionUID = 1182563719599527969L;

    public ProductCategoryOperationException(String msg) {
        super(msg);
    }
}
