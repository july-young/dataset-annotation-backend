

package org.dubhe.data.service;

import org.dubhe.biz.db.utils.PageDTO;
import org.dubhe.data.domain.dto.ConversionCreateDTO;
import org.dubhe.data.domain.dto.DatasetVersionCreateDTO;
import org.dubhe.data.domain.dto.DatasetVersionDeleteDTO;
import org.dubhe.data.domain.dto.DatasetVersionQueryCriteriaDTO;
import org.dubhe.data.domain.entity.Dataset;
import org.dubhe.data.domain.entity.DatasetVersion;
import org.dubhe.data.domain.vo.DatasetVersionVO;

import java.util.List;
import java.util.Map;

/**
 * @description 数据集版本信息服务

 */
public interface DatasetVersionService {

    /**
     * 数据集版本发布
     *
     * @param datasetVersionCreateDTO
     * @return String 版本名
     */
    String publish(DatasetVersionCreateDTO datasetVersionCreateDTO);

    /**
     * 数据集版本列表
     */
    PageDTO<DatasetVersionVO> page(DatasetVersionQueryCriteriaDTO datasetVersionQueryCriteria);

    /**
     * 数据集版本删除
     */
    void versionDelete(DatasetVersionDeleteDTO datasetVersionDeleteDTO);

    /**
     * 数据集版本切换
     *
     * @param datasetId   数据集id
     * @param versionName 版本名称
     */
    void versionSwitch(Long datasetId, String versionName);

    /**
     * 获取下一个可用版本号
     *
     * @param datasetId 数据集id
     * @return String 下一个可用版本名称
     */
    String getNextVersionName(Long datasetId);

    /**
     * 数据集版本数据删除
     *
     * @param datasetId     数据集id
     * @param deleteFlag    删除标识
     */
    void updateStatusByDatasetId(Long datasetId, Boolean deleteFlag);

    /**
     * 训练任务所需版本
     *
     * @param id 数据集id
     * @return List<DatasetVersionVO> 版本列表
     */
    List<DatasetVersionVO> versionList(Long id);

    /**
     * 数据转换回调接口
     *
     * @param datasetVersionId    版本id
     * @param conversionCreateDTO 数据转换回调参数
     * @return int 影响版本数量
     */
    int finishConvert(Long datasetVersionId, ConversionCreateDTO conversionCreateDTO);

    /**
     * 文件复制
     */
    void fileCopy();

    /**
     * 标注文件复制
     */
    void annotationFileCopy();

    /**
     * 查询当前数据集版本的原始文件数量
     *
     * @param datasetId 数据集id
     * @return Integer 原始文件数量
     */
    Integer getSourceFileCount(Long datasetId);

    /**
     * 获取数据集版本详情
     *
     * @param datasetVersionId 数据集版本ID
     * @return 数据集版本详情
     */
    DatasetVersion detail(Long datasetVersionId);

    /**
     * 更新数据集版本状态
     *
     * @param id            数据集ID
     * @param sourceStatus  原状态
     * @param targetStatus  目标状态
     */
    void update(Long id, Integer sourceStatus, Integer targetStatus);

    /**
     * 获取数据集版本数据
     *
     * @param dataset 数据集实体
     * @return 数据集版本信息
     */
    DatasetVersion getDatasetVersionSourceVersion(Dataset dataset);

    /**
     * 获取数据集版本
     *
     * @param datasetId 数据集ID
     * @param versionName  版本名
     * @return DatasetVersion 数据集版本
     */
    DatasetVersion getVersionByDatasetIdAndVersionName(Long datasetId, String versionName);

    /**
     * 根据数据集ID删除数据信息
     *
     * @param datasetId 数据集ID
     */
    void deleteByDatasetId(Long datasetId);

    /**
     * 备份数据集版本数据
     * @param originDataset      原数据集实体
     * @param targetDateset      目标数据集实体
     * @param currentVersionName 版本名称
     */
    void backupDatasetVersionDataByDatasetId(Dataset originDataset, Dataset targetDateset, String currentVersionName);

    /**
     * 根据数据集ID查询版本名称列表
     * @param datasetId 数据集ID
     * @return  版本名称
     */
    List<String> getDatasetVersionNameListByDatasetId(Long datasetId);

    /**
     * 生成ofRecord文件
     *
     * @param datasetId          数据集ID
     * @param versionName        版本名称
     */
    void createOfRecord(Long datasetId, String versionName);

    /**
     * 插入es数据
     *
     * @param versionSource         源版本
     * @param versionTarget         目标版本
     * @param datasetId             数据集id
     * @param datasetIdTarget       目标数据集id
     * @param fileNameMap           文件列表
     */
    void insertEsData(String versionSource, String versionTarget, Long datasetId, Long datasetIdTarget, Map<String, Long> fileNameMap);

    /**
     * 生成版本数据
     *
     * @param datasetVersion  版本详情
     */
    void insert(DatasetVersion datasetVersion);

    /**
     * 更新版本
     *
     * @param datasetVersion 数据集版本
     */
    void updateByEntity(DatasetVersion datasetVersion);
}
