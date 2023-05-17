
package org.dubhe.biz.base.enums;

/**
 * @description 算法状态枚举
 * @date 2020-08-19
 */
public enum AlgorithmStatusEnum {


    /**
     * 创建中
     */
    MAKING(0, "创建中"),
    /**
     * 创建成功
     */
    SUCCESS(1, "创建成功"),
    /**
     * 创建失败
     */
    FAIL(2, "创建失败");


    /**
     * 编码
     */
    private Integer code;

    /**
     * 描述
     */
    private String description;

    AlgorithmStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

