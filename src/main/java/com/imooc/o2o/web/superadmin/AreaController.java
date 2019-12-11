package com.imooc.o2o.web.superadmin;

import com.imooc.o2o.entity.Area;
import com.imooc.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/superadmin")
public class AreaController {
    @Autowired
    private AreaService areaService;
    Logger logger = LoggerFactory.getLogger(AreaController.class);

    /**
     * 列出区域列表
     * @return
     */
    @RequestMapping(value = "/listarea", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listArea() {
        logger.info("===start===");
        long startTime = System.currentTimeMillis();

        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<Area> list;

        try {
            list = areaService.getAreaList();
            modelMap.put("success", true);
            modelMap.put("rows", list);
            modelMap.put("total", list.size());
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            logger.error("AreaController/listArea error! error:" + '\n' + e.toString());
        }

        long endTime = System.currentTimeMillis();
        logger.debug("costTime:[{}ms]", endTime-startTime);
        logger.info("===end===");
        return modelMap;
    }
}
