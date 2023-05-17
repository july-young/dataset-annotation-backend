
package org.dubhe.biz.base.vo;

import lombok.Data;

/**
 * @description
 * @date 2022/4/22
 **/
@Data
public class LabelGroupBaseVO {
    /**
     * 标签组ID
     */
    private Long id;

    /**
     * 标签组名称
     */
    private String name;
    /**
     * 标签组类型：0: private 私有标签组,  1:public 公开标签组
     */
    private Integer type;

    /**
     * 标签组描述
     */
    private String remark;
    /**
     * 标签组类型
     */
    private Integer labelGroupType;
}
