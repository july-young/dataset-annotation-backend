

package org.dubhe.admin.enums;

/**
 * @description 邮箱code 类型

 */
public enum UserMailCodeEnum {
    /**
     * REGISTER_CODE 天枢:注册激活验证
     */
    REGISTER_CODE(1, "天枢:注册激活验证"),

    /**
     * MAIL_UPDATE_CODE 天枢:邮箱修改验证
     */
    MAIL_UPDATE_CODE(2, "天枢:邮箱修改验证"),

    /**
     * OTHER_CODE 天枢:其他验证码
     */
    OTHER_CODE(3, "天枢:其他验证码"),

    /**
     * FORGET_PASSWORD 天枢:忘记密码验证
     */
    FORGET_PASSWORD(4, "天枢:忘记密码验证"),

    ;

    private Integer value;

    private String desc;

    UserMailCodeEnum(Integer value, String desc) {
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

    public static UserMailCodeEnum getEnumValue(Integer value) {
        switch (value) {
            case 1:
                return REGISTER_CODE;
            case 2:
                return MAIL_UPDATE_CODE;
            case 4:
                return FORGET_PASSWORD;
            default:
                return OTHER_CODE;
        }
    }

    public static boolean isExist(Integer value) {
        for (UserMailCodeEnum itm : UserMailCodeEnum.values()) {
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
