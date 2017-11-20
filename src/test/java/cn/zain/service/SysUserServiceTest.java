package cn.zain.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:applicationContext.xml"})
public class SysUserServiceTest {
    private static Logger logger = LogManager.getLogger();

    @Resource
    private SysUserService sysUserService;
    @Test
    public void selectByUsernameTest() throws Exception {
        logger.info(sysUserService.selectByUsername("zain"));
    }
}
