package cn.zain.service;


import cn.zain.model.entity.SysUser;

import java.util.List;

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
    SysUser getSysUser(String username);

    /**
     * 功能说明：查询所有的用户
     *
     * @return
     */
    List<SysUser> getAll();

    /**
     * 功能说明：检测用户名是否存在
     *
     * @param username
     * @return
     */
    boolean checkSysUser(String username);

    /**
     * 功能说明：根据ID查询用户信息
     *
     * @param id
     * @return
     */
    SysUser getSysUser(Long id);

    /**
     * 功能说明：更新用户（ID不能为空）
     *
     * @param sysUser
     */
    void updateSysUser(SysUser sysUser);

    /**
     * 功能说明：根据ID删除用户信息
     *
     * @param id
     * @return
     */
    void deleteSysUser(Long id);

    /**
     * 功能说明：根据IDS批量删除用户信息
     *
     * @param ids
     * @return
     */
    void deleteSysUsers(List<Long> ids);

    /**
     * 功能说明：添加用户
     *
     * @param sysUser
     */
    void saveSysUser(SysUser sysUser);
}
