package com.imooc.o2o.util;

public class PathUtil {
    // 获取当前系统的操作符
    private static String separator = System.getProperty("file.separator");

    // 获取预先规定的项目图片根路径
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";

        if (os.toLowerCase().startsWith("win")) {
            basePath = "D:/projectdev/java_image/";
        } else {
            basePath = "/Users/qianshijie/Programming/Back-End/Java/Images";
        }
        basePath = basePath.replace("/", separator);
        return basePath;
    }

    // 根据业务需求，获取项目图片子路径
    public static String getShopImgPath(long shopId) {
        String imagePath = "/upload/item/shop/" + shopId + "/";
        return imagePath.replace("/", separator);
    }
}
