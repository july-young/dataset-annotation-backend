package org.dubhe.data.machine.state;

import org.dubhe.data.domain.entity.Dataset;

/**
 * @description 数据集状态类
 * @date 2020-08-27
 */
public abstract class AbstractDataState {

    /**
     * 未采样事件 未采样-->调用采集图片程序-->采样中
     *
     * @param primaryKeyId 业务ID
     */
    public void sampledEvent(Long primaryKeyId) {
    }

    /**
     * 采样中事件 采样中-->调用采集图片程序-->未标注
     *
     * @param primaryKeyId 业务ID
     */
    public void samplingEvent(Long primaryKeyId) {
    }

    /**
     * 采样中事件 采样中-->调用采集图片程序-->采样失败
     *
     * @param primaryKeyId 业务ID
     */
    public void samplingFailureEvent(Long primaryKeyId) {
    }

    /**
     * 自动标注事件 未标注-->自动标准算法-->自动标注中
     *
     * @param primaryKeyId 业务ID
     */
    public void autoAnnotationEvent(Long primaryKeyId) {
    }

    /**
     * 手动标注中事件 手动标注中-->手动标注完成-->手动标注完成
     *
     * @param primaryKeyId 业务ID
     */
    public void manualAnnotationCompleteEvent(Long primaryKeyId) {
    }

    /**
     * 手动标注中事件 手动标注中-->删除文件,文件只包含自动标注完成-->自动标注完成
     *
     * @param primaryKeyId 业务ID
     */
    public void manualAutomaticLabelingCompletionEvent(Long primaryKeyId) {
    }

    /**
     * 手动标注保存 自动标注完成-->文件删除-->未标注
     *
     * @param primaryKeyId 业务ID
     */
    public void manualNotMarkedEvent(Long primaryKeyId) {
    }

    /**
     * 自动标注中事件 自动标注中-->自动标注算法-->自动标注完成
     *
     * @param primaryKeyId 业务ID
     */
    public void automaticLabelingEvent(Long primaryKeyId) {
    }

    /**
     * 手动标注保存 自动标注完成-->手动进行标注，点击保存-->标注中
     *
     * @param dataset 数据集详情
     */
    public void manualAnnotationSaveEvent(Dataset dataset) {
    }

    /**
     * 自动标注完成事件   自动标注完成-->调用增强算法-->增强中
     *
     * @param primaryKeyId 业务ID
     */
    public void strengthenEvent(Long primaryKeyId) {
    }

    /**
     * 自动标注完成事件 自动标注完成-->未标注
     *
     * @param primaryKeyId 数据集Id
     */
    public void deletePictureNotMarkedEvent(Long primaryKeyId){
    }

    /**
     * 自动标注完成事件   自动标注完成-->上传图片-->标注中
     *
     * @param primaryKeyId 业务ID
     */
    public void uploadPicturesEvent(Long primaryKeyId) {
    }

    /**
     * 标注完成事件  标注完成-->上传图片-->标注中
     *
     * @param primaryKeyId 业务ID
     */
    public void uploadSavePicturesEvent(Long primaryKeyId) {
    }

    /**
     * 标注完成事件  标注完成-->调用增强算法-->增强中
     *
     * @param primaryKeyId 业务ID
     */
    public void completeStrengthenEvent(Long primaryKeyId) {
    }

    /**
     * 标注完成事件  标注完成-->删除图片-->未标注
     *
     * @param primaryKeyId 业务ID
     */
    public void deletePicturesEvent(Long primaryKeyId) {
    }

    /**
     * 增强中状态事件  增强中-->标注完成-->标注完成
     *
     * @param primaryKeyId 业务ID
     */
    public void strengtheningCompleteEvent(Long primaryKeyId) {
    }

    /**
     * 增强中状态事件  增强中-->自动标注完成-->自动标注完成
     *
     * @param primaryKeyId 业务ID
     */
    public void strengtheningAutoCompleteEvent(Long primaryKeyId) {
    }

    /**
     * 清除标注事件
     *
     * @param primaryKeyId 业务ID
     */
    public void deleteAnnotatingEvent(Long primaryKeyId) {
    }

    /**
     * 目标跟踪失败事件 目标跟踪中-->>目标跟踪失败事件-->>目标跟踪失败
     *
     * @param primaryKeyId 业务ID
     */
    public void autoTrackFailEvent(Long primaryKeyId){
    }

    /**
     * 自动标注事件  手动标注中/未标注-->点击自动标注-->自动标注中
     *
     * @param primaryKeyId 业务ID
     */
    public void autoAnnotationsEvent(Long primaryKeyId) {
    }

    /**
     * 自动标注中事件 自动标注中-->自动标注完成-->自动标注完成
     *
     * @param dataset 数据集详情
     */
    public void doFinishAutoAnnotationEvent(Dataset dataset){
    }

    /**
     * 目标跟踪完成事件 目标跟踪中-->目标跟踪完成-->目标跟踪完成
     *
     * @param dataset 数据集详情
     */
    public void targetCompleteEvent(Dataset dataset){
    }

    /**
     * 标注完成事件 未标注/手动标注中/自动标注完成/目标跟踪完成-->点击完成-->标注完成
     *
     * @param dataset 数据集详情
     */
    public void finishManualEvent(Dataset dataset){
    }

    /**
     * 重新目标跟踪事件 目标跟踪失败-->点击重新目标跟踪-->目标跟踪中
     *
     * @param dataset 数据集详情
     */
    public void trackEvent(Dataset dataset){
    }

    /**
     * 删除文件事件
     *
     * @param dataset 数据集详情
     */
    public void deleteFilesEvent(Dataset dataset){
    }

    /**
     * 上传文件事件
     *
     * @param dataset 数据集详情
     */
    public void uploadFilesEvent(Dataset dataset){
    }

    /**
     * 增强完成事件
     *
     * @param dataset 数据集详情
     */
    public void enhanceFinishEvent(Dataset dataset){
    }

    /**
     * 表格导入事件 未标注 --> 导入表格 --> 导入中
     *
     * @param primaryKeyId 业务ID
     */
    public void tableImportEvent(Long primaryKeyId) {
    }

    /**
     * 表格导入完成时间 导入中 --> 解析表格 --> 未标注
     *
     * @param dataset 数据集详情
     */
    public void tableImportFinishEvent(Dataset dataset) {
    }

}
