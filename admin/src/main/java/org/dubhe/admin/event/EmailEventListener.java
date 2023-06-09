

package org.dubhe.admin.event;

import org.dubhe.admin.domain.dto.EmailDTO;
import org.dubhe.admin.service.MailService;
import org.dubhe.biz.base.enums.BaseErrorCodeEnum;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @description 邮箱事件监听

 */
@Component
public class EmailEventListener {

    @Resource
    private MailService mailService;


    @EventListener
    @Async("taskExecutor")
    public void onApplicationEvent(EmailEvent event) {
        EmailDTO emailDTO = (EmailDTO) event.getSource();
        sendMail(emailDTO.getReceiverMailAddress(), emailDTO.getSubject(), emailDTO.getCode());
    }


    /**
     * 发送邮件
     *
     * @param receiverMailAddress 接受邮箱地址
     * @param subject             标题
     * @param code                验证码
     */
    public void sendMail(final String receiverMailAddress, String subject, String code) {
        try {
            final StringBuffer sb = new StringBuffer();
            sb.append("<h2>" + "亲爱的" + receiverMailAddress + "您好！</h2>")
                    .append("<p style='text-align: center; font-size: 24px; font-weight: bold'>您的验证码为:" + code + "</p>");
            mailService.sendHtmlMail(receiverMailAddress, subject, sb.toString());
        } catch (Exception e) {
            LogUtil.error(LogEnum.SYS_ERR, "UserServiceImpl sendMail error , param:{} error:{}", receiverMailAddress, e);
            throw new BusinessException(BaseErrorCodeEnum.ERROR_SYSTEM.getCode(), BaseErrorCodeEnum.ERROR_SYSTEM.getMsg());
        }
    }

}
