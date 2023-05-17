
package org.dubhe.recycle.enums;

/**
 * @description 垃圾回收状态
 * @date 2020-09-17
 */
public enum RecycleStatusEnum {

    PENDING(0, "待删除"),

    SUCCEEDED(1, "已删除"),
    FAILED(2, "删除失败"),
    DOING(3,"删除中"),

    RESTORING(4,"还原中"),
    RESTORED(5,"已还原"),
    ;

    /**
     * 编码
     */
    private Integer code;

    /**
     * 描述
     */
    private String description;

    RecycleStatusEnum(int code, String description) {
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
