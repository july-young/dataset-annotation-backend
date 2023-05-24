
package org.dubhe.biz.log.handler;


import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;

/**
 * @description 定时任务处理器, 主要做日志标识

 */
public class ScheduleTaskHandler {


    public static void process(Handler handler) {
        LogUtil.startScheduleTrace();
        try {
            handler.run();
        } catch (Exception e) {
            LogUtil.error(LogEnum.BIZ_SYS, "There is something wrong in schedule task handler ：{}", e);
        } finally {
            LogUtil.cleanTrace();
        }
    }


    public interface Handler {
        void run();
    }
}
