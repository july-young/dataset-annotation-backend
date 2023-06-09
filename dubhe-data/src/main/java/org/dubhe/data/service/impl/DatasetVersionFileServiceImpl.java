

package org.dubhe.data.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javafx.util.Pair;
import org.apache.commons.lang.ArrayUtils;
import org.dubhe.biz.base.constant.MagicNumConstant;
import org.dubhe.biz.base.constant.NumberConstant;
import org.dubhe.biz.base.service.UserContextService;
import org.dubhe.biz.base.utils.StringUtils;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.data.machine.statemachine.FileStateMachine;
import org.dubhe.data.constant.*;
import org.dubhe.data.dao.DatasetVersionFileMapper;
import org.dubhe.data.domain.bo.FileUploadBO;
import org.dubhe.data.domain.dto.DatasetVersionFileDTO;
import org.dubhe.data.domain.entity.*;
import org.dubhe.data.machine.constant.FileStateCodeConstant;
import org.dubhe.data.service.DataFileAnnotationService;
import org.dubhe.data.service.DatasetService;
import org.dubhe.data.service.DatasetVersionFileService;
import org.dubhe.data.service.FileService;
import org.dubhe.data.util.GeneratorKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static org.dubhe.data.constant.Constant.DEFAULT_VERSION;

/**
 * @description 数据集版本文件关系 服务实现类

 */
@Service
public class DatasetVersionFileServiceImpl extends ServiceImpl<DatasetVersionFileMapper, DatasetVersionFile>
        implements DatasetVersionFileService, IService<DatasetVersionFile> {

    @Resource
    private DatasetVersionFileMapper datasetVersionFileMapper;

    @Autowired
    private FileStateMachine fileStateMachine;

    @Resource
    @Lazy
    private DatasetService datasetService;

    @Resource
    @Lazy
    private FileService fileService;

    @Autowired
    private GeneratorKeyUtil generatorKeyUtil;

    @Autowired
    private DataFileAnnotationService dataFileAnnotationService;

    @Autowired
    private DataFileAnnotationServiceImpl dataFileAnnotationServiceImpl;

    @Autowired
    private UserContextService userContextService;


    /**
     * 查询数据集指定版本文件列表
     *
     * @param datasetId   数据集id
     * @param versionName 版本名称
     * @return List<DatasetVersionFile> 版本文件列表
     */
    @Override
    public List<DatasetVersionFile> findByDatasetIdAndVersionName(Long datasetId, String versionName) {
        return datasetVersionFileMapper.findByDatasetIdAndVersionName(datasetId, versionName);
    }

    /**
     * 批量写入数据集版本文件
     *
     * @param data 版本文件列表
     */
    @Override
    public void insertList(List<DatasetVersionFile> data) {
        LogUtil.debug(LogEnum.BIZ_DATASET, "save dataset version files start, file size {}", data.size());
        Long start = System.currentTimeMillis();
        data.stream().forEach(datasetVersionFile -> {
            if (null == datasetVersionFile.getAnnotationStatus()) {
                datasetVersionFile.setAnnotationStatus(FileStateCodeConstant.NOT_ANNOTATION_FILE_STATE);
            }
        });
        Queue<Long> dataFileIds = generatorKeyUtil.getSequenceByBusinessCode(Constant.DATA_VERSION_FILE, data.size());
        for (DatasetVersionFile datasetVersionFile : data) {
            datasetVersionFile.setId(dataFileIds.poll());
        }
        datasetVersionFileMapper.saveList(data);
        LogUtil.debug(LogEnum.BIZ_DATASET, "save dataset version files end, times {}", (System.currentTimeMillis() - start));
    }

    /**
     * 新增关系版本变更
     *
     * @param datasetId     数据集id
     * @param versionSource 源版本
     * @param versionTarget 目标版本
     */
    @Override
    public void newShipVersionNameChange(Long datasetId, String versionSource, String versionTarget) {
        List<DataFileAnnotation> dataFileAnnotations = dataFileAnnotationService.getAnnotationByVersion(datasetId, versionSource,
                MagicNumConstant.ZERO);
        datasetVersionFileMapper.newShipVersionNameChange(datasetId, versionSource, versionTarget);
        List<DataFileAnnotation> updateAnnotations = new ArrayList<>();
        dataFileAnnotations.stream().filter(dataFileAnnotation -> dataFileAnnotation.getStatus().equals(MagicNumConstant.ZERO))
                .forEach(dataFileAnnotation -> {
                    dataFileAnnotation.setStatus(MagicNumConstant.TWO);
                    dataFileAnnotation.setInvariable(MagicNumConstant.ONE);
                    updateAnnotations.add(dataFileAnnotation);
                });
        dataFileAnnotationService.updateDataFileAnnotations(updateAnnotations);
    }

    /**
     * 版本文件删除标记
     *
     * @param datasetId   数据集id
     * @param versionName 版本名称
     * @param fileIds     文件id列表
     */
    @Override
    public void deleteShip(Long datasetId, String versionName, List<Long> fileIds) {
        //删除数据集标注接口
        List<DatasetVersionFile> files = datasetVersionFileMapper.selectByDatasetIdAndVersionNameAndFileIds(datasetId, versionName, fileIds);
        if (!CollectionUtils.isEmpty(files) && Objects.isNull(versionName)) {
            List<Long> ids = files.stream().map(a -> a.getId()).collect(Collectors.toList());
            dataFileAnnotationService.updateStatusByVersionIds(datasetId, ids, true);
        }
        datasetVersionFileMapper.updateStatusByFileIdAndDatasetId(datasetId, versionName, fileIds);
    }

    @Override
    public List<Long> getFileIds(Long datasetId, String versionName) {
        LambdaQueryWrapper<DatasetVersionFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DatasetVersionFile::getDatasetId, datasetId);
        lambdaQueryWrapper.eq(DatasetVersionFile::getVersionName, versionName);
        lambdaQueryWrapper.select(DatasetVersionFile::getFileId);
        List<Object> objects = datasetVersionFileMapper.selectObjs(lambdaQueryWrapper);
        return objects.stream().filter(Objects::nonNull).map(x -> Long.parseLong(x.toString())).collect(Collectors.toList());
    }

    public void delete(Long datasetId, String versionName, List<Long> fileIds) {
        datasetVersionFileMapper.updateStatusByFileIdAndDatasetId(datasetId, versionName, fileIds);
        //删除数据集标注接口
        dataFileAnnotationService.updateStatusByVersionIds(datasetId, fileIds, true);
    }


    /**
     * 数据集版本文件标注状态修改
     *
     * @param datasetId    数据集id
     * @param versionName  版本名称
     * @param fileId       文件id
     * @param sourceStatus 源状态
     * @param targetStatus 目标状态
     * @return int         标注修改的数量
     */
    @Override
    public int updateAnnotationStatus(Long datasetId, String versionName, Set<Long> fileId, Integer sourceStatus, Integer targetStatus) {
        //获取当前版本数据集下第一张图片
        DatasetVersionFile versionFile = StringUtils.isBlank(versionName) ? null : getFirstByDatasetIdAndVersionNum(datasetId, versionName, null);

        UpdateWrapper<DatasetVersionFile> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("dataset_id", datasetId);
        updateWrapper.in("file_id", fileId);
        DatasetVersionFile datasetVersionFile = new DatasetVersionFile() {{
            setAnnotationStatus(targetStatus);
        }};
        if (StringUtils.isNotEmpty(versionName)) {
            updateWrapper.eq("version_name", versionName);
        }
        if (sourceStatus != null) {
            updateWrapper.eq("annotation_status", sourceStatus);
        }
        if (versionFile != null) {
            datasetVersionFile.setChanged(Constant.CHANGED);
        }
        //=======================嵌入状态机===================================//
        datasetVersionFile = baseMapper.selectOne(updateWrapper);
        if (versionFile != null) {
            datasetVersionFile.setChanged(Constant.CHANGED);
        }
        //执行自动标注算法事件
        if (targetStatus.equals(FileStateCodeConstant.MANUAL_ANNOTATION_FILE_STATE)) {
            fileStateMachine.manualAnnotationSaveEvent(datasetVersionFile);
        }
        if (targetStatus.equals(FileStateCodeConstant.ANNOTATION_COMPLETE_FILE_STATE)) {
            fileStateMachine.saveCompleteEvent(datasetVersionFile);
        }
        return 0;
    }

    /**
     * 获取数据集指定版本下文件列表(有效文件)
     *
     * @param datasetId   数据id
     * @param versionName 版本名称
     * @return List<DatasetVersionFile> 版本文件列表
     */
    @Override
    public List<DatasetVersionFile> getFilesByDatasetIdAndVersionName(Long datasetId, String versionName) {
        QueryWrapper<DatasetVersionFile> queryWrapper = new QueryWrapper();
        queryWrapper.eq("dataset_id", datasetId);
        if (StringUtils.isNotEmpty(versionName)) {
            queryWrapper.eq("version_name", versionName);
        }
        queryWrapper.notIn("status", MagicNumConstant.ONE);
        return baseMapper.selectList(queryWrapper);
    }


    /**
     * 数据集标注状态
     *
     * @param datasetId   数据集id
     * @param versionName 版本名称
     * @param statusSet   状态
     * @param offset      偏移量
     * @param limit       页容量
     * @param order       排序方式
     * @return 数据集版本文件列表
     */
    @Override
    public List<DatasetVersionFileDTO> getListByDatasetIdAndAnnotationStatus(Long datasetId, String versionName, Set<Integer> statusSet, Long offset, Integer limit, String orderByName, String order, List<Long> labelIdList) {

        if (statusSet == null) statusSet = new HashSet<>();
        order = order == null ? "asc" : order;
        Dataset oneById = datasetService.getOneById(datasetId);
        List<DataFileAnnotation> dataFileAnnotationList = new ArrayList<>();
        if (!labelIdList.isEmpty()
                || (DatatypeEnum.AUDIO.getValue().equals(oneById.getDataType())
                && !statusSet.contains(FileTypeEnum.UNFINISHED_FILE.getValue())
                && !statusSet.contains(FileTypeEnum.UNFINISHED.getValue()))
                && !AnnotateTypeEnum.SPEECH_RECOGNITION.getValue().equals(oneById.getAnnotateType())) {
            dataFileAnnotationList = dataFileAnnotationService.getLabelIdByDatasetIdAndVersionId(labelIdList, datasetId, offset, limit, oneById.getCurrentVersionName());
            if (dataFileAnnotationList.isEmpty()) {
                return null;
            }
        }
        List<DatasetVersionFileDTO> datasetVersionFileDTOList = datasetVersionFileMapper.getIdByDatasetIdAndAnnotationStatus(
                datasetId, versionName, statusSet,
                orderByName, offset, limit, order, dataFileAnnotationList, oneById.getAnnotateType());

        for (int i = 0; i < dataFileAnnotationList.size(); i++) {
            for (int j = 0; j < datasetVersionFileDTOList.size(); j++) {
                DatasetVersionFileDTO datasetVersionFileDTO = datasetVersionFileDTOList.get(j);
                DataFileAnnotation dataFileAnnotation = dataFileAnnotationList.get(i);
                if (datasetVersionFileDTO.getId().equals(dataFileAnnotation.getVersionFileId())) {
                    datasetVersionFileDTO.setLabelIdList(Arrays.asList(dataFileAnnotation.getLabelId()));
                    datasetVersionFileDTO.setPrediction(dataFileAnnotation.getPrediction());
                }
            }
        }
        return datasetVersionFileDTOList;
    }

    /**
     * 获取数据集指定版本第一张图片
     *
     * @param datasetId   数据集id
     * @param versionName 版本名称
     * @param status      状态
     * @return DatasetVersionFile 版本首张图片
     */
    @Override
    public DatasetVersionFile getFirstByDatasetIdAndVersionNum(Long datasetId, String versionName, Collection<Integer> status) {
        QueryWrapper<DatasetVersionFile> queryWrapper = buildQueryWrapperWithDatasetIdVersionNameAndStatus(datasetId, versionName, status);
        queryWrapper.orderByAsc("id");
        queryWrapper.last(" limit 1");
        return baseMapper.selectOne(queryWrapper);
    }

    public QueryWrapper<DatasetVersionFile> buildQueryWrapperWithDatasetIdVersionNameAndStatus(Long datasetId, String versionName, Collection<Integer> status) {
        QueryWrapper<DatasetVersionFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dataset_id", datasetId);
        if (!CollectionUtils.isEmpty(status)) {
            queryWrapper.in("annotation_status", status);
        }
        if (StringUtils.isNotEmpty(versionName)) {
            queryWrapper.eq("version_name", versionName);
        }
        queryWrapper.ne("status", MagicNumConstant.ONE);
        return queryWrapper;
    }

    @Override
    public Integer getFileCountByDatasetIdAndAnnotationStatus(Long datasetId, String versionName, Collection<Integer> status) {
        QueryWrapper<DatasetVersionFile> queryWrapper = buildQueryWrapperWithDatasetIdVersionNameAndStatus(datasetId, versionName, status);
        return baseMapper.selectCount(queryWrapper);
    }

    /**
     * 根据版本列表中的文件id获取文件列表
     *
     * @param datasetVersionFiles 版本文件中间表列表
     * @return List<File> 文件列表
     */
    @Override
    public List<File> getFileListByVersionFileList(List<DatasetVersionFile> datasetVersionFiles) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(File::getDatasetId, datasetVersionFiles.get(0).getDatasetId())
                .in(File::getId, datasetVersionFiles.stream().map(DatasetVersionFile::getFileId).collect(Collectors.toList()))
                .eq(File::getFileType, DatatypeEnum.IMAGE)
                .orderByAsc(File::getId);
        return fileService.listFile(wrapper);
    }

    /**
     * 通过数据集和版本获取文件状态
     *
     * @param datasetId   数据集id
     * @param versionName 版本版本名称
     * @return List<File> 文件状态列表
     */
    @Override
    public List<Integer> getFileStatusListByDatasetAndVersion(Long datasetId, String versionName) {
        if (datasetId == null) {
            LogUtil.error(LogEnum.BIZ_DATASET, "datasetId isEmpty");
            return new ArrayList<>();
        }
        return datasetVersionFileMapper.findFileStatusListByDatasetAndVersion(datasetId, versionName);
    }

    /**
     * 版本回退
     *
     * @param dataset 数据集对象
     */
    @Override
    public void rollbackDataset(Dataset dataset) {
        if (StringUtils.isNoneBlank(dataset.getCurrentVersionName()) && isNeedToRollback(dataset)) {
            doRollback(dataset);
        }
    }

    /**
     * 判断当前数据集是否需要回滚
     *
     * @param dataset 需要回滚的数据集
     * @return boolean 数据集是否需要回退
     */
    @Override
    public boolean isNeedToRollback(Dataset dataset) {
        LambdaQueryWrapper<DatasetVersionFile> isChangedQueryWrapper = new LambdaQueryWrapper<DatasetVersionFile>()
                .eq(DatasetVersionFile::getDatasetId, dataset.getId())
                .eq(DatasetVersionFile::getVersionName, dataset.getCurrentVersionName())
                .eq(DatasetVersionFile::getChanged, Constant.CHANGED);
        return baseMapper.selectCount(isChangedQueryWrapper) > 0;
    }


    /**
     * 根据当前数据集和当前版本号修改数据集是否改变
     *
     * @param id          数据集id
     * @param versionName 版本号
     */
    @Override
    public void updateChanged(Long id, String versionName) {
        baseMapper.updateChanged(id, versionName);
    }

    /**
     * 回滚数据集
     *
     * @param dataset 需要回滚的数据集
     * @return boolean 数据集回退是否成功
     */
    public void doRollback(Dataset dataset) {
        //文件状态为删除新增的和标记为改变的
        datasetVersionFileMapper.rollbackFileAndAnnotationStatus(dataset.getId(), dataset.getCurrentVersionName(), Constant.CHANGED);
    }

    /**
     * 获取当前数据集版本的原始文件数量
     *
     * @param dataset 当前数据集
     * @return 原始文件数量
     */
    @Override
    public Integer getSourceFileCount(Dataset dataset) {
        return datasetVersionFileMapper.getSourceFileCount(dataset);
    }

    /**
     * 获取可以增强的文件列表
     *
     * @param datasetId   数据集id
     * @param versionName 版本名称
     * @return List<DatasetVersionFile> 增强文件列表
     */
    @Override
    public List<DatasetVersionFile> getNeedEnhanceFilesByDatasetIdAndVersionName(Long datasetId, String versionName) {
        return baseMapper.getNeedEnhanceFilesByDatasetIdAndVersionName(datasetId, versionName);
    }

    /**
     * 获取文件对应增强文件列表
     *
     * @param datasetId   数据集id
     * @param versionName 版本名称
     * @param fileId      文件id
     * @return List<File> 增强文件列表
     */
    @Override
    public List<File> getEnhanceFileList(Long datasetId, String versionName, Long fileId) {
        return baseMapper.getEnhanceFileList(datasetId, versionName, fileId);
    }

    /**
     * 获取增强文件数量
     *
     * @param datasetId   数据集id
     * @param versionName 版本名称
     * @return Integer    当前版本增强文件数量
     */
    @Override
    public Integer getEnhanceFileCount(Long datasetId, String versionName) {
        return baseMapper.getEnhanceFileCount(datasetId, versionName);
    }

    /**
     * 根据当前数据集版本获取图片的数量
     *
     * @param datasetId   数据集id
     * @param versionName 数据集版本名称
     * @return 当前数据集版本获取图片的数量
     */
    @Override
    public Integer getImageCountsByDatasetIdAndVersionName(Long datasetId, String versionName) {
        LambdaQueryWrapper<DatasetVersionFile> datasetVersionFileQueryWrapper = new LambdaQueryWrapper<>();
        datasetVersionFileQueryWrapper
                .eq(DatasetVersionFile::getDatasetId, datasetId)
                .eq(DatasetVersionFile::getVersionName, versionName);
        return baseMapper.selectCount(datasetVersionFileQueryWrapper);
    }


    /**
     * 分页获取数据集版本文件数据
     *
     * @param offset      偏移量
     * @param pageSize    页容量
     * @param datasetId   数据集ID
     * @param versionName 数据集版本名称
     * @return 数据集版本文件列表
     */
    @Override
    public List<DatasetVersionFile> getPages(int offset, int pageSize, Long datasetId, String versionName) {
        QueryWrapper<DatasetVersionFile> datasetVersionFileQueryWrapper = new QueryWrapper<>();
        datasetVersionFileQueryWrapper.eq("dataset_id", datasetId);
        if (StringUtils.isNotEmpty(versionName)) {
            datasetVersionFileQueryWrapper.eq("version_name", versionName);
        }
        datasetVersionFileQueryWrapper.last("limit " + offset + "," + pageSize);
        return baseMapper.selectList(datasetVersionFileQueryWrapper);
    }


    /**
     * 获取数据集当前版本文件数量
     */
    @Override
    public Integer getFileCountByDatasetIdAndVersion(Long datasetId, String currentVersionName) {
        LambdaQueryWrapper<DatasetVersionFile> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DatasetVersionFile::getDatasetId, datasetId);
        if (currentVersionName == null) {
            queryWrapper.isNull(DatasetVersionFile::getVersionName);
        } else {
            queryWrapper.eq(DatasetVersionFile::getVersionName, currentVersionName);
        }
        queryWrapper.ne(DatasetVersionFile::getStatus, DataStatusEnum.DELETE.getValue());

        return baseMapper.selectCount(queryWrapper);
    }

    /**
     * 获取数据集文件状态统计数据
     *
     * @param datasetId   数据集ID
     * @param versionName 数据集版本名称
     * @return Map 数据集当前版本文件数量
     */
    @Override
    public Map<Integer, Integer> getDatasetVersionFileCount(Long datasetId, String versionName) {
        List<Pair<Integer, Long>> pairList = baseMapper.getDatasetVersionFileCount(datasetId, versionName);
        Map<Integer, Integer> map = new HashMap<>();
        pairList.forEach(pair -> {
            map.put(pair.getKey(), pair.getValue().intValue());
        });
        return map;
    }

    /**
     * 获取数据集文件数量统计数据
     *
     * @param datasetVersion 数据集版本
     * @return 数据集文件统计
     */
    @Override
    public Integer selectDatasetVersionFileCount(DatasetVersion datasetVersion) {
        return baseMapper.selectCount(new LambdaQueryWrapper<DatasetVersionFile>() {{
            eq(DatasetVersionFile::getDatasetId, datasetVersion.getDatasetId());
            eq(DatasetVersionFile::getVersionName, datasetVersion.getVersionName());
            notIn(DatasetVersionFile::getStatus, DataStatusEnum.ADD.getValue(), DataStatusEnum.DELETE.getValue());
        }});
    }

    /**
     * 获取offset
     *
     * @param datasetId 数据集id
     * @param fileId    文件id
     * @param types     数据集类型
     * @return Integer 获取到offset
     */
    @Override
    public Integer getOffset(Long fileId, Long datasetId, Integer[] types, Long[] labelIds) {
        Dataset dataset = datasetService.getOneById(datasetId);
        DatasetVersionFile datasetVersionFile = baseMapper.selectOne(new LambdaQueryWrapper<DatasetVersionFile>() {{
            eq(DatasetVersionFile::getDatasetId, dataset.getId());
            if (StringUtils.isBlank(dataset.getCurrentVersionName())) {
                isNull(DatasetVersionFile::getVersionName);
            } else {
                eq(DatasetVersionFile::getVersionName, datasetService.getOneById(datasetId).getCurrentVersionName());
            }
            eq(DatasetVersionFile::getFileId, fileId);
        }});
        if (datasetVersionFile == null) {
            return 0;
        }
        Set<Integer> annotationStatus = null;
        if (types != null && types.length > 0) {
            annotationStatus = new HashSet<>();
            for (Integer type : types) {
                annotationStatus.addAll(FileTypeEnum.getStatus(type));
            }
        }
        return baseMapper.getOffset(datasetId, annotationStatus, dataset.getCurrentVersionName(), datasetVersionFile.getId(), labelIds);
    }


    /**
     * 条件查询数据集文件表的数据量
     *
     * @param eq 条件
     * @return long 版本文件数量
     */
    @Override
    public long selectCount(LambdaQueryWrapper<DatasetVersionFile> eq) {
        return baseMapper.selectCount(eq);
    }

    /**
     * 获取单个的文件版本信息
     *
     * @param datasetId   数据集ID
     * @param versionName 版本名称
     * @param fileId      文件ID
     * @return 文件版本信息
     */
    @Override
    public DatasetVersionFile getDatasetVersionFile(Long datasetId, String versionName, Long fileId) {
        LambdaQueryWrapper<DatasetVersionFile> lambdaQueryWrapper = new LambdaQueryWrapper<DatasetVersionFile>() {{
            eq(DatasetVersionFile::getDatasetId, datasetId);
            if (StringUtils.isBlank(versionName)) {
                isNull(DatasetVersionFile::getVersionName);
            } else {
                eq(DatasetVersionFile::getVersionName, versionName);
            }
            eq(DatasetVersionFile::getFileId, fileId);
        }};
        return baseMapper.selectOne(lambdaQueryWrapper);
    }

    /**
     * 重新标注操作更新文件状态
     *
     * @param datasetId 数据集ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAnnotating(Long datasetId) {
        Dataset dataset = datasetService.getOneById(datasetId);
        datasetVersionFileMapper.update(
                new DatasetVersionFile() {{
                    setAnnotationStatus(FileStateCodeConstant.NOT_ANNOTATION_FILE_STATE);
                    setChanged(Constant.CHANGED);
                }},
                new UpdateWrapper<DatasetVersionFile>()
                        .lambda()
                        .eq(DatasetVersionFile::getDatasetId, dataset.getId())
                        .eq(dataset.getCurrentVersionName() != null, DatasetVersionFile::getVersionName, dataset.getCurrentVersionName())
        );
    }


    /**
     * 获取数据集版本文件ID
     *
     * @param id                 数据集ID
     * @param currentVersionName 版本名称
     * @param fileIds            文件id
     * @return 获取数据集版本文件
     */
    @Override
    public List<DatasetVersionFile> getVersionFileByDatasetAndFile(Long id, String currentVersionName, Set<Long> fileIds) {
        return datasetVersionFileMapper.selectList(new LambdaQueryWrapper<DatasetVersionFile>() {{
                                                       eq(DatasetVersionFile::getDatasetId, id);
                                                       if (StringUtils.isBlank(currentVersionName)) {
                                                           isNull(DatasetVersionFile::getVersionName);
                                                       } else {
                                                           eq(DatasetVersionFile::getVersionName, currentVersionName);
                                                       }
                                                       in(DatasetVersionFile::getFileId, fileIds);
                                                   }}
        );
    }

    /**
     * 修改文件状态
     *
     * @param datasetVersionFile 数据集版本文件实体
     */
    @Override
    public void updateStatusById(DatasetVersionFile datasetVersionFile) {
        datasetVersionFileMapper.updateAnnotationStatusById(datasetVersionFile.getAnnotationStatus(), datasetVersionFile.getDatasetId(), datasetVersionFile.getId());
    }


    /**
     * 根据数据集分页条件查询文件总数量
     */
    @Override
    public int selectFileListTotalCount(Long datasetId, String currentVersionName, Set<Integer> statusList, List<Long> labelIdList) {
        Set<Integer> objects = new HashSet<>();
        if (statusList.isEmpty()) {
            objects = FileTypeEnum.getStatus(NumberConstant.NUMBER_0);
        } else {
            for (Integer sta : statusList) {
                objects.addAll(FileTypeEnum.getStatus(sta));
            }
        }

        List<Long> versionId = null;
        if (!labelIdList.isEmpty()) {
            versionId = baseMapper.findByDatasetIdAndVersionNameAndStatus(datasetId, currentVersionName, labelIdList);
        }
        return datasetVersionFileMapper.selectFileListTotalCount(datasetId, currentVersionName, objects, versionId);
    }


    /**
     * 根据数据集ID和版本号查询版本文件信息
     *
     * @param datasetId   数据集ID
     * @param versionName 版本名称
     * @return 数据集版本文件列表
     */
    @Override
    public List<DatasetVersionFile> getDatasetVersionFileByDatasetIdAndVersion(Long datasetId, String versionName) {
        return baseMapper.findByDatasetIdAndVersionName(datasetId, versionName);
    }


    /**
     * 备份版本文件数据
     *
     * @param originDataset 原数据集实体
     * @param targetDataset 目标数据集实体
     * @param versionFiles  原版本文件列表
     * @param files         已转换文件列表
     */
    @Override
    public void backupDatasetVersionFileDataByDatasetId(Dataset originDataset, Dataset targetDataset, List<DatasetVersionFile> versionFiles, List<File> files) {
        Map<String, Long> fileNameMap = files.stream().collect(Collectors.toMap(File::getName, File::getId));
        Queue<Long> dataFileIds = generatorKeyUtil.getSequenceByBusinessCode(Constant.DATA_VERSION_FILE, versionFiles.size());
        for (DatasetVersionFile f : versionFiles) {
            f.setId(dataFileIds.poll());
            f.setFileId(fileNameMap.get(f.getFileName()));
            f.setVersionName(DEFAULT_VERSION);
            f.setDatasetId(targetDataset.getId());
            f.setFileName(f.getFileName());
        }
        List<List<DatasetVersionFile>> splitVersionFiles = CollectionUtil.split(versionFiles, MagicNumConstant.FOUR_THOUSAND);
        splitVersionFiles.forEach(splitVersionFile -> baseMapper.insertBatch(splitVersionFile));
    }

    /**
     * 文件id获取版本文件id
     *
     * @param datasetId 数据集id
     * @param fileIds   文件id
     * @return List<Long>       版本文件id
     */
    @Override
    public List<Long> getVersionFileIdsByFileIds(Long datasetId, Collection<Long> fileIds) {
        return baseMapper.getVersionFileIdsByFileIds(datasetId, fileIds);
    }

    /**
     * 获取版本文件id
     *
     * @param datasetId   数据集id
     * @param fileName    文件名称
     * @param versionName 版本名称
     */
    @Override
    public Long getVersionFileIdByFileName(Long datasetId, String fileName, String versionName) {
        return baseMapper.getVersionFileIdByFileName(datasetId, fileName, versionName);
    }

    /**
     * 获取导入文件所需信息
     *
     * @param datasetId 数据集id
     * @return List<FileUploadBO>
     */
    @Override
    public List<FileUploadBO> getFileUploadContent(Long datasetId, List<Long> fileIds) {
        return baseMapper.getFileUploadContent(datasetId, fileIds);
    }

    @Override
    public Long getVersionFileCountByStatusVersionAndLabelId(Long datasetId, Set<Integer> annotationStatus, String versionName, List<Long> labelIds) {
        List<Long> versionId = null;
        if (CollectionUtil.isNotEmpty(labelIds)) {
            versionId = baseMapper.findByDatasetIdAndVersionNameAndStatus(datasetId, versionName, labelIds);
            if (CollectionUtil.isEmpty(versionId)) {
                return 0L;
            }
        }
        int count = datasetVersionFileMapper.selectFileListTotalCount(datasetId, versionName, annotationStatus, versionId);
        return Long.valueOf(count);
    }

    /**
     * 删除旧标注信息
     *
     * @param datasetId 数据集id
     * @param fileId    文件id
     */
    @Override
    public void deleteByFileId(Long datasetId, Long fileId) {
        Dataset dataset = datasetService.getOneById(datasetId);
        Long versionFileId = datasetVersionFileMapper.getVersionFileIdByFileName(datasetId, fileService.get(fileId, datasetId).getName()
                , dataset.getCurrentVersionName());
        QueryWrapper<DataFileAnnotation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DataFileAnnotation::getDatasetId, datasetId).eq(DataFileAnnotation::getVersionFileId, versionFileId);
        dataFileAnnotationServiceImpl.getBaseMapper().delete(queryWrapper);
    }
}