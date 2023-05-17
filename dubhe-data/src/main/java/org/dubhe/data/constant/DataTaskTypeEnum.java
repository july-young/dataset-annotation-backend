

package org.dubhe.data.constant;

import lombok.Getter;

/**
 * @description  数据集任务类型
 * @date 2020-08-27
 */
@Getter
public enum DataTaskTypeEnum {

    /**
     * 自动标注
     */
    ANNOTATION(0, "自动标注"),
    /**
     * ofrecord格式转换
     */
    OFRECORD(1, "ofrecord格式转换"),
    /**
     * imageNet
     */
    IMAGE_NET(2, "imageNet"),
    /**
     * 数据增强
     */
    ENHANCE(3, "数据增强"),
    /**
     * 目标跟踪
     */
    TARGET_TRACK(4, "目标跟踪"),
    /**
     * 视频采样
     */
    VIDEO_SAMPLE(5, "视频采样"),
    /**
     * 医学标注
     */
    MEDICINE_ANNOTATION(6,"医学标注"),
    /**
     * 文本分类
     */
    TEXT_CLASSIFICATION(7, "文本分类"),
    /**
     * 重新自动标注
     */
    AGAIN_ANNOTATION(8, "重新自动标注"),
    /**
     * csv导入
     */
    CSV_IMPORT(10, "csv导入"),
    /**
     * 删除Redis任务
     */
    REDIS_CLEAR(12, "删除Redis任务")
    ;

    DataTaskTypeEnum(Integer value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private Integer value;
    private String msg;

}
