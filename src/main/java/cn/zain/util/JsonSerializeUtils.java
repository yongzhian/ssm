package cn.zain.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * 功能说明：Java对象序列化为JSON工具类。
 */
public class JsonSerializeUtils {

    private static final Logger logger = LogManager.getLogger(JsonSerializeUtils.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 功能说明：将JSON字符串反序列号化为对象。一般复杂对象为LinkedHashMap/ArrayList
     * 支持数组转换 ["HRCF10310100000071408150XX","HRCF10310100000071408151XX"]  String[]
     * [1，2，3]  int[]
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T deserialize(String json, Class<T> clazz) {

        if (null == clazz || String.class.equals(clazz)) {
            return (T) json;
        }
        Object object = null;
        try {
            object = objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("IOException when serialize object to json.json : " + json, e);
        }
        return (T) object;
    }

    /**
     * 功能说明 ：Json字符串反序列化为嵌套对象
     * demo:
     * [{"robot_sn":"HRCF10310100000071408150XX"},{"robot_sn":"HRCF10310100000071408151XX"}]
     *
     * @return
     * @author Zain 2016/10/25 14:29
     * @params collectionClass为list elementClass为自定义实体
     */
    public static <T> T deserialize(String json, Class<?> collectionClass, Class<?>... elementClass) {

        if (null == collectionClass || null == elementClass) {
            return (T) json;
        }
        Object object = null;
        objectMapper.enableDefaultTyping();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
        try {
            object = objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            logger.error("IOException when serialize object to json.json : " + json, e);

        }
        return (T) object;
    }

    /**
     * 功能说明：将对象序列号化为JSON字符串。
     *
     * @param object
     * @return
     */
    public static String serialize(Object object) {
        if (null == object) {
            return null;
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("IOException when serialize object to json", e);
        }
        return json;
    }


}
    
