
package org.dubhe.biz.base.enums;

/**
 * @description 镜像来源枚举类

 */
public enum ImageSourceEnum {
    MINE(0, "我的镜像"),
    PRE(1, "预置镜像");


    /**
     * 编码
     */
    private Integer code;

    /**
     * 描述
     */
    private String description;

    ImageSourceEnum(int code, String description) {
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
