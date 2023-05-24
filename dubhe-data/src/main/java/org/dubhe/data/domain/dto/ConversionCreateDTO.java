

package org.dubhe.data.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 转换回调信息

 */
@Data
public class ConversionCreateDTO implements Serializable {

    /**
     * 消息内容
     */
    private String msg;

}
