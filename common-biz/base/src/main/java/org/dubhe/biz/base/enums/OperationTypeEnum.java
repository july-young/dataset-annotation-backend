

package org.dubhe.biz.base.enums;

import lombok.Getter;

/**
 * @description 操作类型枚举

 */
@Getter
public enum OperationTypeEnum {
    /**
     * SELECT 查询类型
     */
    SELECT("select", "查询"),

    /**
     * UPDATE 修改类型
     */
    UPDATE("update", "修改"),

    /**
     * DELETE 删除类型
     */
    DELETE("delete", "删除"),

    /**
     * LIMIT 禁止操作类型
     */
    LIMIT("limit", "禁止操作"),

    /**
     * INSERT 新增类型
     */
    INSERT("insert", "新增类型"),

    ;

    /**
     * 操作类型值
     */
    private  String type;

    /**
     * 操作类型备注
     */
    private  String desc;

    OperationTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
