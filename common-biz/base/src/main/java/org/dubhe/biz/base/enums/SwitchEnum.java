

package org.dubhe.biz.base.enums;

/**
 * @description 是否开关枚举

 */
public enum SwitchEnum {
    /**
     * OFF 否
     */
    OFF(0, "否"),

    /**
     * ON 否
     */
    ON(1, "是"),

    ;

    private Integer value;

    private String desc;

    SwitchEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static SwitchEnum getEnumValue(Integer value) {
        switch (value) {
            case 0:
                return OFF;
            case 1:
                return ON;
            default:
                return OFF;
        }
    }

    public static Boolean getBooleanValue(Integer value) {
        switch (value) {
            case 1:
                return true;
            case 0:
                return false;
            default:
                return false;
        }
    }

    public static boolean isExist(Integer value) {
        for (SwitchEnum itm : SwitchEnum.values()) {
            if (value.compareTo(itm.getValue()) == 0) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return "[" + this.value + "]" + this.desc;
    }

}
