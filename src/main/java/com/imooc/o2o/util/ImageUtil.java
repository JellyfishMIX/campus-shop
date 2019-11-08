package com.imooc.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

// 这是一个工具类，不需要实例化出对象，因此此类中的方法均为static。成员变量也均为static
public class ImageUtil {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath(); // 通过当前线程获取主类加载器，从而获得spring所在的根路径。
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");    // 可以控制日期格式的对象。getRandomFileName()使用
    private static final Random random = new Random();  // 有生成随机数功能的对象。getRandomFileName()使用
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);   // 引入日志

    /**
     * 将CommonsMultipartFile转换为File
     * @param cFile
     * @return
     */
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
        File newFile = new File(cFile.getOriginalFilename());
        try {
            cFile.transferTo(newFile);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        } catch (IllegalStateException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return newFile;
    }
    /**
     * 处理缩略图，将当前店铺所在的图片储存目录targetPath与realFileName.extension拼接起来，生成当前项目所关联的图片文件夹中的"绝对路径": targetPath/realFileName
     * 然后将"图片文件夹中的绝对路径"根据运行设备，生成运行设备中的"绝对路径"。在此"绝对路径"中储存图片。最后，将"图片文件夹中的绝对路径"返回，以供服务器储存记录
     * 此方法内部会生成一个随机的文件名，realFileName.extension = "随机文件名"+"扩展名" 再与targetPath结合起来，得到 targetPath/realFileName。生成当前图片项目中的"绝对路径"，此"项目中绝对路径"并不等于"硬件设备储存中的根路径"
     * relativePath会存到数据库table`shop_img`中的shop_img。根目录与运行设备有关，如果服务器迁移到别的运行设备，希望也可以正常运行。因此存入相对路径，在service层取相对路径时再加工为绝对路径
     * @param thumbnailInputStream
     * @param targetPath
     * @return
     */
    public static String generateThumbnail(InputStream thumbnailInputStream, String fileName, String targetPath) {
        String realFileName = getRandomFileName();  // 获取文件随机名。因为用户上传图片的名字是随机的，可能会有很多重名，因此要取随即名。
        String extension = getFileExtension(fileName); // 获取用户上传文件的扩展名
        maikeDirPath(targetPath);   // 创建目标路径所涉及的目录。targetPath的项目目录可能不存在，先把路径创建出来

        String relativePath = targetPath + realFileName + extension;    // realFileName.extension = "随机文件名"+"扩展名" 再与targetPath结合起来，得到 targetPath/realFileName
        logger.debug("current relativePath is: " + relativePath);   // debug中记录当前的相对路径。一旦程序出错，就可以根据debug信息进行调试。同时还可以根据logger.error提示的信息，确认错误是什么
        File dest = new File(PathUtil.getImgBasePath() + relativePath); // 把根路径与相对路径拼接起来。dest是destination的缩写
        logger.debug("current absolutePath is: " + PathUtil.getImgBasePath() + relativePath);   // debug中记录当前的绝对路径。一旦程序出错，就可以根据debug信息进行调试。同时还可以根据logger.error提示的信息，确认错误是什么
        try {
            Thumbnails.of(thumbnailInputStream).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/water_mark.jpg")), 0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString()); // 一旦程序出错，就可以根据debug信息进行调试。同时还可以根据logger.error提示的信息，确认错误是什么
            e.printStackTrace();
        }
        return relativePath;    // relativePath会存到数据库table`shop_img`中的shop_img。根目录与运行设备有关，如果服务器迁移到别的运行设备，希望也可以正常运行。因此存入相对路径，在service层取相对路径时再加工为绝对路径
    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     *
     * @return
     */
    public static String getRandomFileName() {
        int randomNum = random.nextInt(89999) + 10000; // 获取随机的五位数
        String nowTimeStr = simpleDateFormat.format(new Date());    // 当前时间戳
        return nowTimeStr + randomNum;
    }

    /**
     * 获取输入文件流的扩展名
     *
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
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

    /**
     * 测试时所写所用，不重要，可忽略。可以给一张小黄人图片加水印
     * @param args
     * @throws IOException
     */
    // public static void main(String[] args) throws IOException {
    //     // 加水印
    //     Thumbnails.of(new File("/Users/qianshijie/Programming/Backend/Java/Images/o2o/xiaohuangren.jpg"))
    //             .size(600, 509)
    //             .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/water_mark.jpg")), 0.25f)
    //             .outputQuality(0.8f).toFile("/Users/qianshijie/Programming/Back-End/Java/Images/xiaohuangrennew.jpg");
    // }
}
