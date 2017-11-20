package cn.zain.controller;

import cn.zain.service.StatisticsService;
import cn.zain.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2016 www.yongzhian.cn. All Rights Reserved.
 *
 * @author Zain
 */
@Controller
@RequestMapping("/sysuser")
public class SysUserController extends AbstractController{
    @Resource
    private StatisticsService statisticsService;

    @RequestMapping(value = "/get.do", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> get() {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("method", "get");
//        Server server = new Server();
        return returnMap;

    }

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login() {
        logger.debug("用户登录...");
        Map<String, Object> returnMap = new HashMap<>();

        if(authCheck()){
            returnMap.put("result", 0);
        }else{
            returnMap.put("result", -1);
            returnMap.put("error_msg", "认证失败!");
        }
        return returnMap;
    }

    @RequestMapping(value = "/export.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object>  exportFilePath(@Param("collectionName")String collectionName,@Param("pageNum")int pageNum,@Param("pageSize")int pageSize) {
       logger.debug("exportFilePath");
//       根目录 不够安全，采用SecurityFilter拦截，如需进一步效果则sessionID和文件名绑定校验
       String dir = request.getSession().getServletContext().getRealPath("/excel") ;

//        String dir = request.getSession().getServletContext().getRealPath("/WEB-INF") ; //此目录下spring mvc除了流没有较好方式返回，
        String fileName = statisticsService.exportMongoDbData2ExcelByFile(collectionName,pageNum,pageSize,dir);
        if(StringUtils.isBlank(fileName)){
            return genFailReturnMap(null,"导出失败!");
        }
        Map<String, Object> returnMap = genSuccessReturnMap();
        returnMap.put("fileName","/excel/" + fileName);
        return returnMap;
    }

    @RequestMapping(value = "/export2.do", method = RequestMethod.GET)
    public void exportWithStream(HttpServletResponse response,@Param("collectionName")String collectionName,@Param("pageNum")int pageNum,@Param("pageSize")int pageSize) throws IOException {
       if(pageSize > 5){
           response.setContentType("application/json;charset=utf-8");
           response.getOutputStream().println("不能超过5条!");
           return;
       }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String((collectionName + ".xlsx").getBytes(), "iso-8859-1"));
        statisticsService.exportMongoDbData2ExcelByStream(collectionName,pageNum,pageSize,response.getOutputStream());
    }
}
