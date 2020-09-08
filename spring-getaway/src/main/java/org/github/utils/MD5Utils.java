package org.github.utils;

import java.security.MessageDigest;

/**
 * @ClassName: MD5Utils
 * @Description:
 * @author: <p> 雅诗兰黛  熬夜不怕黑眼圈</p>
 * @date 2020-09-08 22:59
 * @Copyright: 墨铭琦妙代码研究中心
 */
public abstract class MD5Utils {
    private static final String MD5 = "MD5";
    private static final String DEFAULT_ENCODING = "UTF-8";

    public final static String MD5(String source, String encoding) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = source.getBytes(encoding);
            MessageDigest mdInst = MessageDigest.getInstance(MD5);
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final static String MD5(String source) {
        return MD5(source, DEFAULT_ENCODING);
    }

    public static void main(String[] args) {
        System.out.println(MD5("123456","UTF-8"));
    }
}
