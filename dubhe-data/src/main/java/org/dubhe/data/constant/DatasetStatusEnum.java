package org.dubhe.data.constant;

import lombok.Getter;

/**
 * @description 数据集状态

 */
@Getter
public enum DatasetStatusEnum {
// 101:未标注
// 102:手动标注中
// 103:自动标注中
// 104:自动标注完成
// 105:标注完成
// 201:目标跟踪中
// 202:目标跟踪完成
// 203:目标跟踪失败
// 301:未采样
// 302:采样中
// 303:采样失败
// 401:增强中
// 402:导入中
    INIT(101, " 未标注"),
    MANUAL_ANNOTATING(102, "手动标注中"),
    AUTO_ANNOTATING(103, "自动标注中"),
    AUTO_FINISHED(104, "自动标注完成"),
    FINISHED(105, "已标注完成"),
    NOT_SAMPLE(201, "未采样"),
    FINISHED_TRACK(202, "目标跟踪完成"),
    SAMPLING(301, "采样中"),
    SAMPLE_FAILED(303, "采样失败"),
    ENHANCING(401, "增强中"),
    ;

    DatasetStatusEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private Integer value;
    private String msg;

}
