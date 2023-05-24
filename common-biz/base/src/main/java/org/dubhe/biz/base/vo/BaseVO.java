

package org.dubhe.biz.base.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description VO基础类

 */
@Data
public class BaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long createUserId;

    private Timestamp createTime;

    private Long updateUserId;

    private Timestamp updateTime;
}
