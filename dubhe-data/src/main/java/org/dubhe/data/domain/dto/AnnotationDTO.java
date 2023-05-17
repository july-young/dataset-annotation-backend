
package org.dubhe.data.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 标注DTO
 * @date 2021-01-06
 */
@Data
public class AnnotationDTO implements Serializable {

    /**
     * 标签ID
     */
    private Long categoryId;

    /**
     * 预估分
     */
    private Double score;
}
