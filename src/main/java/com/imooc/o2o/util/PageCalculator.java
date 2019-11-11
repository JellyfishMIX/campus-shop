package com.imooc.o2o.util;

public class PageCalculator {
    /**
     * 根据页索引和每页规格，返回对应的行数。因为前端发送的是所需页数，而SQL查询需要的是行数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static int calculatorRowIndex(int pageIndex, int pageSize) {
        return (pageIndex > 0) ? (pageIndex-1) * pageSize : 0;
    }
}
