

package org.dubhe.admin.event;

import org.dubhe.admin.domain.dto.EmailDTO;


/**
 * @description 邮件事件

 */
public class EmailEvent extends BaseEvent<EmailDTO> {

    private static final long serialVersionUID = 8103187726344703089L;

    public EmailEvent(EmailDTO msg) {
        super(msg);
    }

    public EmailEvent(Object source, EmailDTO msg) {
        super(source, msg);
    }

}
