
package org.dubhe.data.machine.state;

import org.dubhe.data.domain.entity.Dataset;
import org.dubhe.data.domain.entity.DatasetVersionFile;

import java.util.HashSet;
import java.util.Set;

/**
 * @description 文件抽象类

 */
public abstract class AbstractFileState {

    /**
     * 文件 未标注-->手动标注文件点击保存-->标注中
     *
     * @param datasetVersionFile 数据集版本文件详情
     */
    public void manualAnnotationSaveEvent(DatasetVersionFile datasetVersionFile) {
    }

    /**
     * 文件 未标注-->点击完成-->标注完成
     *
     * @param datasetVersionFile 数据集版本文件详情
     */
    public void saveCompleteEvent(DatasetVersionFile datasetVersionFile) {
    }

    /**
     * 文件 自动标注完成-->点击保存-->手动标注中
     *
     * @param primaryKeyId 业务ID
     */
    public void saveAutoAnnotationEvent(Long primaryKeyId) {
    }

    /**
     * 文件  未标注-->自动标注完成(批量保存图片状态)-->自动标注完成
     * @param filesId       文件ID
     * @param datasetId     数据集ID
     * @param versionName   数据集版本名称
     */
    public void doFinishAutoAnnotationBatchEvent(Set<Long> filesId, Long datasetId, String versionName){
    }

    /**
     * 文件 自动标注完成-->目标跟踪完成-->目标跟踪完成
     *
     * @param dataset 数据集详情
     */
    public void doFinishAutoTrackEvent(Dataset dataset){
    }

    /**
     * 文件  未标注-->自动标注完成(批量保存图片状态)-->自动标注完成未识别
     *
     * @param filesId       文件ID
     * @param datasetId     数据集ID
     * @param versionName   数据集版本名称
     */
    public void doFinishAutoAnnotationInfoIsEmptyBatchEvent(Set<Long> filesId, Long datasetId, String versionName){
    }
}
