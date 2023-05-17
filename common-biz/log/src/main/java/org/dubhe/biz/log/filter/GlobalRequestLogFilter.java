

package org.dubhe.biz.log.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @description 全局请求 日志过滤器
 * @date 2020-08-13
 */
public class GlobalRequestLogFilter extends BaseLogFilter {


    @Override
    public boolean checkLevel(ILoggingEvent iLoggingEvent) {
        return this.level != null;
    }

}