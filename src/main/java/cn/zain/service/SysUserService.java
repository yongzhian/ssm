package cn.zain.service;

import cn.zain.model.entity.SysUser;

/**
 * @author Zain
 */
public interface SysUserService {
    /**
     * 根据用户名查询系统用户信息
     *
     * @param username String
     * @return SysUser
     */
    SysUser selectByUsername(String username);
}
