

package org.dubhe.biz.base.enums;


import lombok.Getter;

/**
 * @description 数据集类型

 */
@Getter
public enum DatasetTypeEnum {

    /**
     * 私有数据
     */
    PRIVATE(0, "私有数据"),
    /**
     * 团队数据
     */
    TEAM(1, "团队数据"),
    /**
     * 公开数据
     */
    PUBLIC(2, "公开数据");

    DatasetTypeEnum(Integer value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private Integer value;

    private String msg;

    /**
     * 数据类型校验 用户web端接口调用时参数校验
     *
     * @param value 数据类型
     * @return      参数校验结果
     */
    public static boolean isValid(Integer value) {
        for (DatasetTypeEnum datasetTypeEnum : DatasetTypeEnum.values()) {
            if (datasetTypeEnum.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

}
