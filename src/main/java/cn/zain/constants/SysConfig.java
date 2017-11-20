package cn.zain.constants;

import cn.zain.util.StringTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 读取系统配置文件
 *
 * @author Zain
 */
public class SysConfig {
    private static final Logger logger = LogManager.getLogger(SysConfig.class);

    private static final String SYS_CONFIG_PROPERTIES = "sys-config.properties";
    private static Properties prop;

    private SysConfig(){
        super();
    }

    static {
        prop = new Properties();
        try {
            InputStream stream = SysConfig.class.getClassLoader().getResourceAsStream(SYS_CONFIG_PROPERTIES);
            InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
            prop.load(isr);
            logger.info("配置文件：" + SYS_CONFIG_PROPERTIES + " 加载成功。");
            logger.info("配置信息:" + prop);
        } catch (Exception e) {
            logger.error("配置文件：" + SYS_CONFIG_PROPERTIES + " 加载失败。", e);
        }
    }

    public static String getProperty(String key) {
        String valStr = prop.getProperty(key);
        return valStr;
    }

    public static int getPropertyInt(String key) {
        String valStr = prop.getProperty(key);
        return StringTools.str2Int(valStr);
    }

    public static long getPropertyLong(String key) {
        String valStr = prop.getProperty(key);
        return StringTools.str2Long(valStr);
    }

    public static boolean getPropertyBoolean(String key) {
        String valStr = prop.getProperty(key);
        if (Boolean.TRUE.toString().equals(valStr)) {
            return true;
        } else {
            return false;
        }
    }
}
