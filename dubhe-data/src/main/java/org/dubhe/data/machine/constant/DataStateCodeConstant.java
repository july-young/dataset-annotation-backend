
package org.dubhe.data.machine.constant;

/**
 * @description 数据集状态码

 */
public class DataStateCodeConstant {

    private DataStateCodeConstant() {
    }

    /**
     * 未标注
     */
    public static final Integer NOT_ANNOTATION_STATE = 101;
    /**
     * 手动标注中
     */
    public static final Integer MANUAL_ANNOTATION_STATE = 102;
    /**
     * 自动标注中
     */
    public static final Integer AUTOMATIC_LABELING_STATE = 103;
    /**
     * 自动标注完成
     */
    public static final Integer AUTO_TAG_COMPLETE_STATE = 104;
    /**
     * 标注完成
     */
    public static final Integer ANNOTATION_COMPLETE_STATE = 105;

    /**
     * 目标跟踪中
     */
    public static final Integer TARGET_FOLLOW_STATE = 201;
    /**
     * 目标跟踪完成
     */
    public static final Integer TARGET_COMPLETE_STATE = 202;
    /**
     * 目标跟踪失败
     */
    public static final Integer TARGET_FAILURE_STATE = 203;

    /**
     * 未采样
     */
    public static final Integer NOT_SAMPLED_STATE = 301;
    /**
     * 采样中
     */
    public static final Integer SAMPLING_STATE = 302;
    /**
     * 采样失败
     */
    public static final Integer SAMPLED_FAILURE_STATE = 303;

    /**
     * 增强中
     */
    public static final Integer STRENGTHENING_STATE = 401;
    /**
     * 导入中
     */
    public static final Integer IN_THE_IMPORT_STATE = 402;

}
