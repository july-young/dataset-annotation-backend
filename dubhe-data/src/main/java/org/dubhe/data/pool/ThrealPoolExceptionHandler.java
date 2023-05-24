

package org.dubhe.data.pool;

import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;

/**
 * @description 异常线程池

 */
public class ThrealPoolExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LogUtil.error(LogEnum.BIZ_DATASET, "thread pool catch exception,thread:{},exception:{}", t, e);
    }

}
