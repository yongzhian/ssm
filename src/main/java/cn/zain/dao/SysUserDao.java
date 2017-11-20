package cn.zain.dao;

import cn.zain.model.entity.SysUser;

public interface SysUserDao {
    SysUser selectByPrimaryKey(Long id);

    SysUser selectByUsername(String username);
}