

package org.dubhe.data.constant;

/**
 * @description 增强算法枚举

 */
public enum EnhanceTypeEnum {

    /**
     * 去雾
     */
    DEFOG(1, "去雾"),
    /**
     * 增雾
     */
    INCREASE_FOG(2, "增雾"),
    /**
     * 对比度增强
     */
    CONTRAST_ENHANCE(3, "对比度增强"),
    /**
     * 直方图均衡化
     */
    HISTOGRAM_AVERAGE(4, "直方图均衡化");

    private Integer key;
    private String value;

    EnhanceTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

}
