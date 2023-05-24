
 
package org.dubhe.biz.base.enums;

/**
 * @description 度量文件生成状态

 **/
public enum MeasureStateEnum {

    MAKING(0, "生成中"),
    SUCCESS(1, "生成成功"),
    FAIL(2, "生成失败");


    /**
     * 编码
     */
    private Integer code;

    /**
     * 描述
     */
    private String description;

    MeasureStateEnum(int code, String description) {
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
