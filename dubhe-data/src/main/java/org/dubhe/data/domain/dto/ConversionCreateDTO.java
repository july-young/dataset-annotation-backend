

package org.dubhe.data.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 转换回调信息
 * @date 2020-07-02
 */
@Data
public class ConversionCreateDTO implements Serializable {

    /**
     * 消息内容
     */
    private String msg;

}
