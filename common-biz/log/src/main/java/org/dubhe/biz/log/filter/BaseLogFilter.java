

package org.dubhe.biz.log.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Marker;

/**
 * @description 自定义日志过滤器

 */
public class BaseLogFilter extends AbstractMatcherFilter<ILoggingEvent> {

    Level level;

    /**
     * 重写decide方法
     *
     * @param iLoggingEvent event to decide upon.
     * @return FilterReply
     */
    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }
        final String msg = iLoggingEvent.getMessage();
        //自定义级别
        if (checkLevel(iLoggingEvent) && msg != null && msg.startsWith(StrUtil.DELIM_START) && msg.endsWith(StrUtil.DELIM_END)) {
            final Marker marker = iLoggingEvent.getMarker();
            if (marker != null && this.getName() != null && this.getName().contains(marker.getName())) {
                return onMatch;
            }
        }

        return onMismatch;
    }

    /**
     * 检测日志级别
     * @param iLoggingEvent 日志事件
     * @return true 过滤当前级别 false 不过滤当前级别
     */
    protected boolean checkLevel(ILoggingEvent iLoggingEvent) {
        return this.level != null
                && iLoggingEvent.getLevel() != null
                && iLoggingEvent.getLevel().toInt() == this.level.toInt();
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public void start() {
        if (this.level != null) {
            super.start();
        }
    }
}