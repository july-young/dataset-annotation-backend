

package org.dubhe.biz.base.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description VO基础类
 * @date 2020-05-22
 */
@Data
public class BaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long createUserId;

    private Timestamp createTime;

    private Long updateUserId;

    private Timestamp updateTime;
}
