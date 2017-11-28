package cn.zain.controller;

import cn.zain.model.dto.Msg;
import cn.zain.model.entity.SysUser;
import cn.zain.service.StatisticsService;
import cn.zain.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2016 www.yongzhian.cn. All Rights Reserved.
 *
 * @author Zain
 */
@Controller
@RequestMapping("/sysuser")
public class SysUserController extends AbstractController {
    @Resource
    private StatisticsService statisticsService;

    @Resource(name = "sysUserService")
    private SysUserService sysUserService;

    /**
     * 功能说明：用户登录
     *
     * @return
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public Msg login(@Param("username") String username, @Param("password") String password) {
        logger.debug("用户登录...");
        SysUser sysUser = sysUserService.getSysUser(username);
        if (null != sysUser && password.equals(sysUser.getPassword())) {
            return Msg.success();
        }
        return Msg.fail("认证失败!");
    }

    /**
     * 功能说明：单个或多个删除
     * 批量删除：1-2-3
     * 单个删除：1
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    public Msg deleteEmp(@PathVariable("ids") String ids) {
        //批量删除
        if (ids.contains("-")) {
            List<Long> delIds = new ArrayList<>();
            String[] strIds = ids.split("-");
            //组装id的集合
            for (String str : strIds) {
                delIds.add(Long.parseLong(str));
            }
            sysUserService.deleteSysUsers(delIds);
        } else {
            Long id = Long.parseLong(ids);
            sysUserService.deleteSysUser(id);
        }
        return Msg.success();
    }

    /**
     * 功能说明：修改用户信息
     *
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Msg saveSysUser(SysUser sysUser) {
        sysUserService.updateSysUser(sysUser);
        return Msg.success();
    }

    /**
     * 功能说明:查询用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Msg getSysUser(@PathVariable("id") Long id) {
        return Msg.success().add("sysUser", sysUserService.getSysUser(id));
    }


    /**
     * 功能说明:检查用户名是否可用
     *
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping("/checksysuser")
    public Msg checkSysUser(@RequestParam("username") String username) {
        //先判断用户名是否是合法的表达式;
        String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
        if (!username.matches(regx)) {
            return Msg.fail("用户名必须是6-16位数字和字母的组合或者2-5位中文");
        }

        //数据库用户名重复校验
        boolean b = sysUserService.checkSysUser(username);
        if (b) {
            return Msg.success();
        } else {
            return Msg.fail("用户名不可用");
        }
    }


    /**
     * 功能说明:添加用户信息
     * 1、支持JSR303校验
     * 2、导入Hibernate-Validator
     *
     * @return
     * @Valid的参数后必须紧挨着一个BindingResult 参数，否则spring会在校验不通过时直接抛出异常
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveSysUser(@Valid SysUser sysUser, BindingResult result) {
        if (result.hasErrors()) {
            //校验失败，应该返回失败，在模态框中显示校验失败的错误信息
            Map<String, Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError fieldError : errors) {
                logger.debug("错误的字段名：{}", fieldError.getField());
                logger.debug("错误信息：{}", fieldError.getDefaultMessage());
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return Msg.fail("校验失败").add("errorFields", map);
        } else {
            sysUserService.saveSysUser(sysUser);
            return Msg.success();
        }
    }

    /**
     * 功能说明：分页查询(用于AJAX，返回JSON)
     *
     * @param pageNum
     * @return
     */
    @RequestMapping("/grid/json")
    @ResponseBody
    public Msg getSysUsersWithJson(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        // 引入PageHelper分页插件
        // 在查询之前只需要调用，传入页码，以及每页的大小
        PageHelper.startPage(pageNum, 5);
        // startPage后面紧跟的这个查询就是一个分页查询
        List<SysUser> sysUsers = sysUserService.getAll();
        PageInfo page = new PageInfo(sysUsers, 5);
        return Msg.success().add("pageInfo", page);
    }

    /**
     * 功能说明：分页查询(返回页面)
     *
     * @param pageNum
     * @return
     */
    @RequestMapping("/grid")
    public String getSysUsers(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, Model model) {
        // 引入PageHelper分页插件
        // 在查询之前只需要调用，传入页码，以及每页的大小
        PageHelper.startPage(pageNum, 5);
        // startPage后面紧跟的这个查询就是一个分页查询
        List<SysUser> sysUsers = sysUserService.getAll();
        PageInfo page = new PageInfo(sysUsers, 5);
        model.addAttribute("pageInfo", page);
        return "sysuser/grid";
    }

    /**
     * 功能说明：文件方式导出
     *
     * @param collectionName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/export.do", method = RequestMethod.POST)
    @ResponseBody
    public Msg exportFilePath(@Param("collectionName") String collectionName, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize) {
        logger.debug("exportFilePath");
//       根目录 不够安全，采用SecurityFilter拦截，如需进一步效果则sessionID和文件名绑定校验
        String dir = request.getSession().getServletContext().getRealPath("/excel");

//        String dir = request.getSession().getServletContext().getRealPath("/WEB-INF") ; //此目录下spring mvc除了流没有较好方式返回，
        String fileName = statisticsService.exportMongoDbData2ExcelByFile(collectionName, pageNum, pageSize, dir);
        if (StringUtils.isBlank(fileName)) {
            return Msg.fail("导出失败!");
        }
        return Msg.success().add("fileName", "/excel/" + fileName);
    }

    /**
     * 功能说明：流方式导出
     *
     * @param response
     * @param collectionName
     * @param pageNum
     * @param pageSize
     * @throws IOException
     */
    @RequestMapping(value = "/export2.do", method = RequestMethod.GET)
    public void exportWithStream(HttpServletResponse response, @Param("collectionName") String collectionName, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize) throws IOException {
        if (pageSize > 5) {
            response.setContentType("application/json;charset=utf-8");
            response.getOutputStream().println("不能超过5条!");
            return;
        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((collectionName + ".xlsx").getBytes(), "iso-8859-1"));
        statisticsService.exportMongoDbData2ExcelByStream(collectionName, pageNum, pageSize, response.getOutputStream());
    }
}
