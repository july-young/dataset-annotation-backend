package org.dubhe.data.constant;

import lombok.Getter;

/**
 * @description 数据集转换状态
 */
@Getter
public enum ConversionStatusEnum {

    NOT_COPY(0, "未复制"),
    NOT_CONVERSION(1, "未转换"),
    HAS_CONVERTED(2, "已转换"),
    UNABLE_CONVERSION(3, "无法转换"),
    PUBLISHING(4,"发布中"),
    STOPPED(5,"停止")
    ;

    ConversionStatusEnum(Integer value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private Integer value;
    private String msg;

}
