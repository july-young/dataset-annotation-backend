
package org.dubhe.data.machine.constant;

/**
 * @description 数据集状态码
 * @date 2020-09-03
 */
public class FileStateCodeConstant {

    private FileStateCodeConstant(){

    }

    /**
     * 未标注
     */
    public static final Integer NOT_ANNOTATION_FILE_STATE = 101;
    /**
     * 手动标注中
     */
    public static final Integer MANUAL_ANNOTATION_FILE_STATE = 102;
    /**
     * 自动标注完成
     */
    public static final Integer AUTO_TAG_COMPLETE_FILE_STATE = 103;
    /**
     * 标注完成
     */
    public static final Integer ANNOTATION_COMPLETE_FILE_STATE = 104;
    /**
     * 标注未识别
     */
    public static final Integer ANNOTATION_NOT_DISTINGUISH_FILE_STATE = 105;
    /**
     * 目标跟踪完成
     */
    public static final Integer TARGET_COMPLETE_FILE_STATE = 201;

}