

package org.dubhe.admin.event;

import org.dubhe.admin.domain.dto.EmailDTO;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description 邮箱事件发布者

 */
@Service
public class EmailEventPublisher {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 邮件发送事件
     *
     * @param dto
     */
    @Async("taskExecutor")
    public void sentEmailEvent(final EmailDTO dto) {
        try {
            EmailEvent emailEvent = new EmailEvent(dto);
            applicationEventPublisher.publishEvent(emailEvent);
        } catch (Exception e) {
            LogUtil.error(LogEnum.SYS_ERR, "EmailEventPublisher sentEmailEvent error , param:{} error:{}", dto, e);
        }
    }
}
