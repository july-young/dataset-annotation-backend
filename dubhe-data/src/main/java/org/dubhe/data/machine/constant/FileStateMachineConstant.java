
package org.dubhe.data.machine.constant;

/**
 * @description 文件状态事件常量

 */
public class FileStateMachineConstant {

    private FileStateMachineConstant() {
    }

    /**
     * 文件状态
     */
    public static final String FILE_STATE_MACHINE = "fileStateMachine";
    /**
     * 文件 未标注-->手动标注文件点击保存-->标注中
     */
    public static final String FILE_MANUAL_ANNOTATION_SAVE_EVENT = "manualAnnotationSaveEvent";
    /**
     * 文件 未标注-->点击完成-->标注完成
     */
    public static final String FILE_SAVE_COMPLETE_EVENT = "saveCompleteEvent";
    /**
     * 文件 自动标注完成-->目标跟踪完成-->目标跟踪完成
     */
    public static final String FILE_DO_FINISH_AUTO_TRACK_EVENT = "doFinishAutoTrackEvent";
    /**
     *文件  未标注-->自动标注完成(批量保存图片状态)-->自动标注完成
     */
    public static final String FILE_DO_FINISH_AUTO_ANNOTATION_BATCH_EVENT="doFinishAutoAnnotationBatchEvent";
    /**
     *文件  未标注-->自动标注完成(批量保存图片状态)-->自动标注完成未识别
     */
    public static final String FILE_DO_FINISH_AUTO_ANNOTATION_INFO_IS_EMPTY_BATCH_EVENT="doFinishAutoAnnotationInfoIsEmptyBatchEvent";

}