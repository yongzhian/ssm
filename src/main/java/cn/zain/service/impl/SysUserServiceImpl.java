package cn.zain.service.impl;

import cn.zain.dao.SysUserDao;
import cn.zain.model.entity.SysUser;
import cn.zain.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Zain
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    //采用MapperScannerConfigurer自动装配
    @Autowired
    private SysUserDao sysUserDao;


    @Override
    public SysUser selectByUsername(String username) {
        return sysUserDao.selectByUsername(username);
    }
}
