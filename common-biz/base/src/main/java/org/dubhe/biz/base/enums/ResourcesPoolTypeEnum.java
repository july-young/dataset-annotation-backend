
package org.dubhe.biz.base.enums;

/**
 * @description 规格类型

 */
public enum ResourcesPoolTypeEnum {

    CPU(0, "CPU"),
    GPU(1, "GPU");


    /**
     * 编码
     */
    private Integer code;

    /**
     * 描述
     */
    private String description;

    ResourcesPoolTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 是否是GPU编码
     * @param code
     * @return true 是 ，false 否
     */
    public static boolean isGpuCode(Integer code){
        return GPU.getCode().equals(code);
    }

}
