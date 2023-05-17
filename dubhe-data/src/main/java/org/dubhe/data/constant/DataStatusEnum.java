

package org.dubhe.data.constant;

import lombok.Getter;

/**
 * @description 数据(文件)状态(是否删除状态)
 * @date 2020-06-03
 */
@Getter
public enum DataStatusEnum {

    /**
     * 新增文件
     */
    ADD(0, " 新增"),
    /**
     * 已删除文件
     */
    DELETE(1, "删除"),

    /**
     * 正常文件
     */
    NORMAL(2, "正常"),
    ;

    DataStatusEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private int value;
    private String msg;

}
