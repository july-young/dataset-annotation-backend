

package org.dubhe.data.constant;

import lombok.Getter;

/**
 * @description 标注类型枚举类

 */
@Getter
public enum AnnotateTypeEnum {

    /**
     * 目标检测
     */
    OBJECT_DETECTION(102, "目标检测"),
    /**
     * 图像分类
     */
    CLASSIFICATION(101, "图像分类"),
    /**
     * 目标跟踪
     */
    OBJECT_TRACK(201, "目标跟踪"),
    /**
     * 文本分类
     */
    TEXT_CLASSIFICATION(301, "文本分类"),
    /**
     * 语义分割
     */
    SEMANTIC_CUP(103, "语义分割"),
    /**
     * 音频分类
     */
    AUDIO_CLASSIFY(401, "声音分类"),
    /**
     * 文本分词
     */
    TEXT_SEGMENTATION(302, "文本分词"),
    /**
     * 命名实体识别
     */
    NAMED_ENTITY_RECOGNITION(303, "命名实体识别"),
    /**
     * 语音识别
     */
    SPEECH_RECOGNITION(402, "语音识别"),
    /**
     * 自定义导入
     */
    AUTO_IMPORT(10001, "自定义导入");


    AnnotateTypeEnum(Integer value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private Integer value;
    private String msg;


    public static boolean isStoreInEs(Integer value) {
        if (TEXT_CLASSIFICATION.getValue().equals(value)
                || TEXT_SEGMENTATION.getValue().equals(value)
                || NAMED_ENTITY_RECOGNITION.getValue().equals(value)) {
            return true;
        }
        return false;
    }

    public static Integer getConvertAnnotateType(String annotate) {
        for (AnnotateTypeEnum annotateTypeEnum : AnnotateTypeEnum.values()) {
            if (annotateTypeEnum.msg.equals(annotate)) {
                return annotateTypeEnum.value;
            }
        }
        return null;
    }

}
