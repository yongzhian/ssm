package cn.zain.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES加密解密工具
 *
 * @author Zain
 */
public class AesUtil {

    private static final Logger logger = LogManager.getLogger(AesUtil.class);

    private static final String KEY = "W4YgAdDoTadR3Ky0";

    /**
     * 必须16为long型
     */
    private static final String IV = "9726836823768122";

    private static final String KEY_ALGORITHM = "AES";
    private static final byte[] SEED = {0x38, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x02, 0x78, 0x67,
            0x56, 0x45, 0x34, 0x23, 0x12, 0x01};

    private AesUtil() {
    }

    /**
     * 功能说明：通用AES加密方式
     * 可与python/C#等相互加密解密
     * 注意：由于AES仅支持16位字符加密和解密，如果非16位会自动补空格，解密时会去掉首尾空格，故需要加密串不能在首尾出现空格
     *
     * @param encryStr 待加密串
     * @return base64位字符串
     */
    public static String encryptComm(String encryStr) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = encryStr.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength
                        + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(KEY.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return StringTools.byte2BASE64(encrypted);
        } catch (Exception e) {
            logger.error("加密失败.源数据data : " + encryStr, e);
            return null;
        }
    }

    /**
     * 功能说明：通用AES解密方式
     * 可与python/C#等相互加密解密
     *
     * @param desStr 待解密串
     * @return String
     */
    public static String decryptComm(String desStr) {
        try {
            byte[] encrypted = StringTools.str2BASE64(desStr);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(KEY.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted);
            String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e) {
            logger.error("解密失败.待解密字符串desStr : " + desStr, e);
            return null;
        }
    }

    /**
     * 功能说明：加密
     *
     * @param content 需要加密的内容，默认工作模式为ECB
     * @return String
     * @deprecated 现用encryptComm替换
     */
    @Deprecated
    public static String encrypt(String content) {
        KeyGenerator kgen;
        try {
            kgen = KeyGenerator.getInstance(KEY_ALGORITHM);

            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(SEED);
            //kgen.init(128, new SecureRandom(SEED)); //如果这样linux会报错 Given final block not properly padded
            kgen.init(128, secureRandom);

            SecretKey secretKey = kgen.generateKey();

            SecretKeySpec key = new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);

            cipher.init(Cipher.ENCRYPT_MODE, key);
            return StringTools.byte2HexStr(cipher.doFinal(content.getBytes("utf-8")));
        } catch (Exception e) {
            logger.error("加密失败.", e);
        }
        return null;
    }

    /**
     * 功能说明：解密
     *
     * @param content 待解密内容
     * @return String
     * @deprecated 现用decryptComm替换
     */
    @Deprecated
    public static String decrypt(String content) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(SEED);
            //kgen.init(128, new SecureRandom(SEED)); //如果这样linux会报错 Given final block not properly padded
            kgen.init(128, secureRandom);

            SecretKey secretKey = kgen.generateKey();

            SecretKeySpec key = new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(StringTools.hexStr2Byte(content)), "UTF-8");
        } catch (Exception e) {
            logger.error("解密失败.", e);
        }
        return null;
    }
}
