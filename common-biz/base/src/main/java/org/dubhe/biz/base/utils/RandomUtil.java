

package org.dubhe.biz.base.utils;


/**
 * @description  随机数工具类

 */
public class RandomUtil {

    /**
     * 生成随机6位数
     *
     * @return 6位随机数
     */
    public static String randomCode() {
        Integer res = (int) ((Math.random()) * 1000000);
        return res + "";
    }

}
