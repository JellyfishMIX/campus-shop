package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;

    /**
     * 新建店铺
     * @param shop
     * @param imageHolder
     * @return
     */
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException {
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
                if (imageHolder.getImage() != null) {
                    // 存储图片
                    try {
                        this.addShopImg(shop, imageHolder);  // .addShopImg()中有一个.shop.setShopImg方法，执行后把当前对象shop中的图片地址shopImg更新
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
    @Override
    public Shop getShopByShopId(long shopId) {
        return shopDao.queryShopByShopId(shopId);
    }

    /**
     * 更改店铺信息
     * @param shop
     * @param imageHolder
     * @return
     * @throws ShopOperationException
     */
    @Override
    public ShopExecution modifyShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        } else {
            // 1.判断是否需要处理图片
            if (imageHolder.getImage() != null && imageHolder.getImageName() != null && !"".equals(imageHolder.getImageName())) {
                Shop tempShop = shopDao.queryShopByShopId(shop.getShopId());
                if (tempShop.getShopImg() != null) {
                    ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                }
                addShopImg(shop, imageHolder);
            }
        }
        // 2.更新店铺信息
        shop.setLastEditTime(new Date());
        int effectedNum = shopDao.updateShop(shop);
        if (effectedNum <= 0) {
            return new ShopExecution(ShopStateEnum.INNER_ERROR);
        } else {
            shop = shopDao.queryShopByShopId(shop.getShopId());
            return new ShopExecution(ShopStateEnum.SUCCESS, shop);
        }
    }

    /**
     * 私有方法，增加店铺图片并存储
     * @param shop
     * @param imageHolder
     */
    private void addShopImg(Shop shop, ImageHolder imageHolder) {
        // 图片存储在项目的图片目录下，获取该shop图片的相对路径
        String targetPath = PathUtil.getShopImgPath(shop.getShopId());  // 初等相对路径，需进一步加工得到完整相对路径。为什么这一步获得的是初等相对路径？因为这一步得到的是"targetPath"，当前店铺所在的图片储存目录，仅为当前店铺服务。

        // 处理缩略图，将当前店铺所在的图片储存目录targetPath与realFileName.extension拼接起来，生成当前项目所关联的图片文件夹中的"绝对路径": targetPath/realFileName
        // 然后将"图片文件夹中的绝对路径"根据运行设备，生成运行设备中的"绝对路径"。在此"绝对路径"中储存图片。最后，将"图片文件夹中的绝对路径"返回，以供服务器储存记录
        // 此方法内部会生成一个随机的文件名，realFileName.extension = "随机文件名"+"扩展名" 再与targetPath结合起来，得到 targetPath/realFileName。生成当前图片项目中的"绝对路径"，此"项目中绝对路径"并不等于"硬件设备储存中的根路径"
        String shopImgRelativePath = ImageUtil.generateThumbnail(imageHolder, targetPath);  // 将"图片文件夹中的绝对路径"返回

        System.out.println(shopImgRelativePath);
        shop.setShopImg(shopImgRelativePath);   // 把当前对象shop中的图片地址shopImg更新，用于更新数据库
    }

    /**
     * 根据shopCondition分页返回列表数据和店铺总数
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ShopExecution getShopListAndCount(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution shopExecution = new ShopExecution();
        if (shopList != null) {
            shopExecution.setShopList(shopList);
            shopExecution.setCount(count);
        } else {
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }

        return shopExecution;
    }
}
