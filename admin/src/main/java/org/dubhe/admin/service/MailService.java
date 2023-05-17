

package org.dubhe.admin.service;

import javax.mail.MessagingException;

/**
 * @description 邮箱服务 Service
 * @date 2020-06-01
 */
public interface MailService {

    /**
     * 简单文本邮件
     *
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param contnet 邮件内容
     */
    void sendSimpleMail(String to, String subject, String contnet);

    /**
     * HTML 文本邮件
     *
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param content HTML内容
     * @throws MessagingException
     */
    void sendHtmlMail(String to, String subject, String content) throws MessagingException;

    /**
     * 附件邮件
     *
     * @param to       接收者邮件
     * @param subject  邮件主题
     * @param content  HTML内容
     * @param filePath 附件路径
     * @throws MessagingException
     */
    void sendAttachmentsMail(String to, String subject, String content,
                                    String filePath) throws MessagingException;



    /**
     * 图片邮件
     *
     * @param to      接收者邮件
     * @param subject 邮件主题
     * @param content HTML内容
     * @param rscPath 图片路径
     * @param rscId   图片ID
     * @throws MessagingException
     */
    void sendLinkResourceMail(String to, String subject, String content,
                                     String rscPath, String rscId);

}
