package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;

    /**
     * 新建店铺
     * @param shop
     * @param shopImgInputStream
     * @return
     */
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) {
        // if阵空值判断
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        // if还可以继续添加，未完待续...
        try {
            // 给店铺信息赋初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            // 添加店铺信息
            int effectedNum = shopDao.insertShop(shop); // 判断新增店铺是否有效
            if (effectedNum <= 0) {
                throw new ShopOperationException("店铺创建失败");   // 只有继承RuntimeException等类时(此处为ShopOperationException)，才可以回滚。Exception无法回滚
            } else {
                if (shopImgInputStream != null) {
                    // 存储图片
                    try {
                        this.addShopImg(shop, shopImgInputStream, fileName);  // .addShopImg()中有一个.shop.setShopImg方法，执行后把当前对象shop中的图片地址shopImg更新
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error: " + e.getMessage());
                    }
                    // 更新店铺的图片地址
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0) {
                        throw new ShopOperationException("更新店铺的图片地址失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error: " + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);    // 返回ShopStateEnum: 待审核，同时返回当前对象shop
    }

    /**
     * 通过shopId查询店铺
     * @param shopId
     * @return shop
     */
    public Shop getShopByShopId(long shopId) {
        return shopDao.getShopByShopId(shopId);
    }

    /**
     * 私有方法，增加店铺图片并存储
     * @param shop
     * @param shopImgInputStream, String fileName
     */
    private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) {
        // 图片存储在项目的图片目录下，获取该shop图片的相对路径
        String targetPath = PathUtil.getShopImgPath(shop.getShopId());  // 初等相对路径，需进一步加工得到完整相对路径。为什么这一步获得的是初等相对路径？因为这一步得到的是"targetPath"，当前店铺所在的图片储存目录，仅为当前店铺服务。

        // 处理缩略图，将当前店铺所在的图片储存目录targetPath与realFileName.extension拼接起来，生成当前项目所关联的图片文件夹中的"绝对路径": targetPath/realFileName
        // 然后将"图片文件夹中的绝对路径"根据运行设备，生成运行设备中的"绝对路径"。在此"绝对路径"中储存图片。最后，将"图片文件夹中的绝对路径"返回，以供服务器储存记录
        // 此方法内部会生成一个随机的文件名，realFileName.extension = "随机文件名"+"扩展名" 再与targetPath结合起来，得到 targetPath/realFileName。生成当前图片项目中的"绝对路径"，此"项目中绝对路径"并不等于"硬件设备储存中的根路径"
        String shopImgRelativePath = ImageUtil.generateThumbnail(shopImgInputStream, fileName, targetPath);  // 将"图片文件夹中的绝对路径"返回

        System.out.println(shopImgRelativePath);
        shop.setShopImg(shopImgRelativePath);   // 把当前对象shop中的图片地址shopImg更新，用于更新数据库
    }
}
