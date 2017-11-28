package cn.zain.service.impl;

import cn.zain.dao.SysUserDao;
import cn.zain.model.entity.SysUser;
import cn.zain.model.entity.SysUserExample;
import cn.zain.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Zain
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    //采用MapperScannerConfigurer自动装配
    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public SysUser getSysUser(String username) {
        SysUserExample sysUserExample = new SysUserExample();
        SysUserExample.Criteria criteria = sysUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<SysUser> sysUsers = sysUserDao.selectByExample(sysUserExample);
        if (null != sysUsers && sysUsers.size() > 0) {
            return sysUsers.get(0);
        }
        return null;
    }

    @Override
    public List<SysUser> getAll() {
        return sysUserDao.selectByExample(null);
    }

    @Override
    public boolean checkSysUser(String username) {
        return null != getSysUser(username);
    }

    @Override
    public SysUser getSysUser(Long id) {
        return sysUserDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateSysUser(SysUser sysUser) {
        sysUserDao.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public void deleteSysUser(Long id) {
        sysUserDao.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteSysUsers(List<Long> ids) {
        SysUserExample sysUserExample = new SysUserExample();
        SysUserExample.Criteria criteria = sysUserExample.createCriteria();
        criteria.andIdIn(ids);
        sysUserDao.deleteByExample(sysUserExample);
    }

    @Override
    public void saveSysUser(SysUser sysUser) {
        if (StringUtils.isBlank(sysUser.getIsValid())) {
            sysUser.setIsValid("T");
        }
        if (null == sysUser.getCreateTime()) {
            sysUser.setCreateTime(new Date());
        }
        if (StringUtils.isBlank(sysUser.getIsLocked())) {
            sysUser.setIsLocked("F");
        }
        sysUserDao.insert(sysUser);
    }
}
