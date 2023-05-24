
package org.dubhe.recycle.enums;

/**
 * @description 垃圾回收类型

 */
public enum RecycleTypeEnum {

    FILE(0, "文件"),
    TABLE_DATA(1, "表数据");

    private Integer code;

    private String type;


    RecycleTypeEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
