
package org.dubhe.data.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 标签

 */
@Data
@Builder
public class LabelVO implements Serializable {

    /**
     * 标签ID
     */
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签颜色
     */
    private String color;
}
