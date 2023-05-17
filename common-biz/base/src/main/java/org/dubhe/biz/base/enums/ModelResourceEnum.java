

package org.dubhe.biz.base.enums;

import lombok.Getter;

/**
 * @description 模型资源枚举类
 * @date 2020-11-19
 */
@Getter
public enum ModelResourceEnum {

    /**
     * 我的模型
     */
    MINE(0, "我的模型"),
    /**
     * 预置模型
     */
    PRESET(1, "预置模型"),
    /**
     * 炼知模型
     */
    ATLAS(2, "炼知模型");

    private Integer type;

    private String description;

    ModelResourceEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    /**
     * 根据类型获取枚举类对象
     *
     * @param type 类型
     * @return 枚举类对象
     */
    public static ModelResourceEnum getType(Integer type) {
        for (ModelResourceEnum modelResourceEnum : values()) {
            if (modelResourceEnum.getType().compareTo(type) == 0) {
                //获取指定的枚举
                return modelResourceEnum;
            }
        }
        return null;
    }
}