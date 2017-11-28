package cn.zain.controller;


import cn.zain.service.SysUserService;
import cn.zain.util.StringTools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zain
 * @date 2017/6/13
 */
public abstract class AbstractController {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    @Autowired
    protected HttpServletRequest request;

    /**
     * 功能说明：统一异常处理
     *
     * @param e 异常
     * @return Object
     */
    @ExceptionHandler
    @ResponseBody
    protected Object exceptionProcess(Exception e) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("result", "网络异常，请稍后再试。");
        logger.error("出现异常..", e);
        return returnMap;
    }
}
