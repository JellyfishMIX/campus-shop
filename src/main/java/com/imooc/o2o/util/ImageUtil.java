package com.imooc.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath(); // 通过当前线程获取主类加载器，从而获得spring所在的根路径。
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random random = new Random();

    // 处理缩略图
    public static void generateThumbnail(CommonsMultipartFile thumbnail, String targetPath) {
        String realFileName = getRandomFileName();  // 获取文件随机名。因为用户上传图片的名字是随机的，可能会有很多重名，因此要取随即名。
        String extension = getFileExtension(thumbnail); // 获取用户上传文件的扩展名
        maikeDirPath(targetPath);   // 创建目标路径所涉及的目录。targetPath的项目目录可能不存在，先把路径创建出来

        String relativePath = targetPath + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativePath); // 把根路径与相对路径拼接起来。dest是destination的缩写
        try {
            Thumbnails.of(thumbnail.getInputStream()).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/water_mark.jpg")), 0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     *
     * @return
     */
    private static String getRandomFileName() {
        int randomNum = random.nextInt(89999) + 10000; // 获取随机的五位数
        String nowTimeStr = simpleDateFormat.format(new Date());
        return nowTimeStr + randomNum;
    }

    /**
     * 获取输入文件流的扩展名
     *
     * @param thumbnail
     * @return
     */
    private static String getFileExtension(CommonsMultipartFile thumbnail) {
        String originalFileName = thumbnail.getOriginalFilename();
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    /**
     * 创建目标路径所涉及的目录，即/home/xiangzai/xxx.jpg
     * 那么home/xiangzai/xxx.jpg都会自动创建
     *
     * @param targetPath
     * @return
     */
    private static void maikeDirPath(String targetPath) {
        String absolutePath = PathUtil.getImgBasePath() + targetPath;   // 合成：预先规定的项目图片路径 + 目标图片子路径
        File dirPath = new File(absolutePath);  // dir, directory的缩写，意思是目录
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    public static void main(String[] args) throws IOException {
        // 加水印
        Thumbnails.of(new File("/Users/qianshijie/Programming/Back-End/Java/Images/xiaohuangren.jpg"))
                .size(600, 509)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/water_mark.jpg")), 0.25f)
                .outputQuality(0.8f).toFile("/Users/qianshijie/Programming/Back-End/Java/Images/xiaohuangrennew.jpg");
    }
}
