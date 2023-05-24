

package org.dubhe.biz.base.utils;

import org.dubhe.biz.base.constant.MagicNumConstant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description 时间格式转换工具类

 */
public class TimeTransferUtil {

    private static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'";

    /**
     * Date转换为UTC时间
     *
     * @param date
     * @return utcTime
     */
    public static String dateTransferToUtc(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        /**UTC时间与CST时间相差8小时**/
        calendar.set(Calendar.HOUR,calendar.get(Calendar.HOUR) - MagicNumConstant.EIGHT);
        SimpleDateFormat utcSimpleDateFormat = new SimpleDateFormat(UTC_FORMAT);
        Date utcDate = calendar.getTime();
        return utcSimpleDateFormat.format(utcDate);
    }
}
