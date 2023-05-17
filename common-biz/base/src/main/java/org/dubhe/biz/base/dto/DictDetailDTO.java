package org.dubhe.biz.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description 字典详情DTO
 * @date 2020-06-01
 */
@Data
public class DictDetailDTO implements Serializable {

    private static final long serialVersionUID = 1521993584428225098L;

    @ApiModelProperty(value = "字典详情id")
    private Long id;

    @ApiModelProperty(value = "字典label")
    private String label;

    @ApiModelProperty(value = "字典详情value")
    private String value;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "字典id")
    private Long dictId;

    private Timestamp createTime;

    private Timestamp updateTime;
}
