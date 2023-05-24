package org.dubhe.data.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.dubhe.biz.permission.annotation.DataPermissionMethod;
import org.dubhe.biz.base.constant.MagicNumConstant;
import org.dubhe.biz.base.constant.NumberConstant;
import org.dubhe.biz.base.constant.ResponseCode;
import org.dubhe.biz.base.enums.OperationTypeEnum;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.base.utils.StringUtils;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.data.machine.statemachine.DataStateMachine;
import org.dubhe.data.machine.statemachine.FileStateMachine;
import org.dubhe.data.constant.*;
import org.dubhe.data.domain.bo.FileBO;
import org.dubhe.data.domain.bo.TaskSplitBO;
import org.dubhe.data.domain.dto.*;
import org.dubhe.data.domain.entity.*;
import org.dubhe.data.machine.constant.FileStateCodeConstant;
import org.dubhe.data.machine.enums.FileStateEnum;
import org.dubhe.data.service.*;
import org.dubhe.data.service.store.IStoreService;
import org.dubhe.data.service.store.MinioStoreServiceImpl;
import org.dubhe.data.util.GeneratorKeyUtil;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

/**
 * @description 标注service

 */
@Service
public class AnnotationServiceImpl implements AnnotationService {

    @Autowired
    private FileStateMachine fileStateMachine;

    @Autowired
    private DataStateMachine dataStateMachine;

    /**
     * esSearch索引
     */
    @Value("${es.index}")
    private String esIndex;

    /**
     * 文件信息服务
     */
    @Autowired
    private FileService fileService;

    /**
     * 任务服务类
     */
    @Autowired
    private TaskService taskService;

    /**
     * 数据集服务类
     */
    @Autowired
    private DatasetService datasetService;

    /**
     * 文件存储服务类
     */
    @Resource(type = MinioStoreServiceImpl.class)
    private IStoreService storeService;

    /**
     * 版本文件服务类
     */
    @Autowired
    private DatasetVersionFileService datasetVersionFileService;

    @Autowired
    private GeneratorKeyUtil generatorKeyUtil;

    @Autowired
    private DatasetLabelService datasetLabelService;

    /**
     * 标注任务队列
     */
    static PriorityBlockingQueue<TaskSplitBO> queue;

    /**
     * 自动标注任务
     */
    private ConcurrentHashMap<String, TaskSplitBO> autoAnnotating;

    /**
     * 版本服务类
     */
    @Autowired
    private DatasetVersionService datasetVersionService;

    /**
     * 数据文件标注服务
     */
    @Autowired
    private DataFileAnnotationService dataFileAnnotationService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 目标跟踪任务
     */
    private ConcurrentHashSet<Long> tracking;

    /**
     * 文件工具类
     */
    @Autowired
    private org.dubhe.data.util.FileUtil fileUtil;

    /**
     * 队列长度
     */
    public static final int QUEUE_SIZE = MagicNumConstant.FIFTY;

    /**
     * 跟踪数量
     */
    public static final int TRACKING_SIZE = MagicNumConstant.FIVE;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        queue = new PriorityBlockingQueue<>(QUEUE_SIZE, Comparator.comparingInt(TaskSplitBO::getPriority).reversed());
        autoAnnotating = new ConcurrentHashMap<>(MagicNumConstant.SIXTEEN);
        tracking = new ConcurrentHashSet<>(MagicNumConstant.SIXTEEN);
    }

    /**
     * 标注保存(分类批量)
     *
     * @param batchAnnotationInfoCreateDTO 标注信息
     * @return int 标注修改的数量
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @DataPermissionMethod
    public void save(Long datasetId, BatchAnnotationInfoCreateDTO batchAnnotationInfoCreateDTO) {
        for (AnnotationInfoCreateDTO annotationInfoCreateDTO : batchAnnotationInfoCreateDTO.getAnnotations()) {
            save(datasetId, annotationInfoCreateDTO);
        }
    }

    /**
     * 标注保存实现
     *
     * @param annotationInfoCreateDTO 标注信息
     * @return int 标注修改的数量
     */
    @Override
    public void save(Long datasetId, AnnotationInfoCreateDTO annotationInfoCreateDTO) {
        Dataset dataset = datasetService.getOneById(datasetId);
        datasetService.checkPublic(dataset, OperationTypeEnum.UPDATE);
        //判断数据集是否在发布中
        if (!StringUtils.isBlank(dataset.getCurrentVersionName())) {
            if (datasetVersionService.getDatasetVersionSourceVersion(dataset).getDataConversion().equals(NumberConstant.NUMBER_4)) {
                throw new BusinessException(ErrorEnum.DATASET_PUBLISH_ERROR);
            }
        }
        datasetVersionFileService.deleteByFileId(datasetId, annotationInfoCreateDTO.getId());
        annotationInfoCreateDTO.setDatasetId(datasetId);
        annotationInfoCreateDTO.setCurrentVersionName(dataset.getCurrentVersionName());
        annotationInfoCreateDTO.setDataType(dataset.getDataType());
        doSave(annotationInfoCreateDTO);
        saveDatasetFileAnnotationsByImage(annotationInfoCreateDTO);
        //改变文件的状态为标注完成
        fileStateMachine.saveCompleteEvent(new DatasetVersionFile() {{
            setDatasetId(dataset.getId());
            setFileId(annotationInfoCreateDTO.getId());
            setVersionName(dataset.getCurrentVersionName());
        }});
        //改变数据集的状态为标注完成
        dataStateMachine.finishManualEvent(dataset);
    }

    /**
     * 标注保存(单个)
     *
     * @param annotationInfoCreateDTO 标注信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @DataPermissionMethod
    public void save(Long fileId, Long datasetId, AnnotationInfoCreateDTO annotationInfoCreateDTO) {
        Dataset dataset = datasetService.getOneById(datasetId);
        if (dataset == null) {
            throw new BusinessException(ErrorEnum.DATASET_NOT_EXIST);
        }
        datasetService.checkPublic(dataset, OperationTypeEnum.UPDATE);
        //判断数据集是否在发布中
        if (!StringUtils.isBlank(dataset.getCurrentVersionName())) {
            if (datasetVersionService.getDatasetVersionSourceVersion(dataset).getDataConversion().equals(NumberConstant.NUMBER_4)) {
                throw new BusinessException(ErrorEnum.DATASET_PUBLISH_ERROR);
            }
        }
        annotationInfoCreateDTO.setId(fileId);
        annotationInfoCreateDTO.setDatasetId(datasetId);
        annotationInfoCreateDTO.setCurrentVersionName(dataset.getCurrentVersionName());
        annotationInfoCreateDTO.setDataType(dataset.getDataType());
        doSave(annotationInfoCreateDTO);
        saveDatasetFileAnnotationsByImage(annotationInfoCreateDTO);
        //改变数据集的状态为标注中
        dataStateMachine.manualAnnotationSaveEvent(dataset);
        //将文件改为标注中的状态
        DatasetVersionFile datasetVersionFile = new DatasetVersionFile();
        datasetVersionFile.setDatasetId(dataset.getId());
        datasetVersionFile.setFileId(fileId);
        datasetVersionFile.setVersionName(dataset.getCurrentVersionName());

        fileStateMachine.manualAnnotationSaveEvent(datasetVersionFile);
    }

    /**
     * 标注文件保存
     *
     * @param annotationInfoCreateDTO 标注信息
     */
    private void doSave(AnnotationInfoCreateDTO annotationInfoCreateDTO) {

        if (annotationInfoCreateDTO == null || annotationInfoCreateDTO.getAnnotation() == null
                || annotationInfoCreateDTO.getId() == null) {
            LogUtil.warn(LogEnum.BIZ_DATASET, "annotation info invalid. annotation:{}", annotationInfoCreateDTO);
            return;
        }
        QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper
                .eq("id", annotationInfoCreateDTO.getId()).eq("dataset_id", annotationInfoCreateDTO.getDatasetId());
        File fileOne = fileService.selectOne(fileQueryWrapper);
        if (fileOne == null) {
            LogUtil.warn(LogEnum.BIZ_DATASET, ErrorEnum.FILE_ABSENT.getMsg() + "fileId is" + annotationInfoCreateDTO.getId());
            throw new BusinessException(ErrorEnum.FILE_ABSENT);
        }
        datasetService.autoAnnotatingCheck(fileOne);
        String filePath = fileUtil.getWriteAnnotationAbsPath(fileOne.getDatasetId(), fileOne.getName());
        String annotation = annotationInfoCreateDTO.getAnnotation();
        storeService.write(filePath, annotation);
    }

    /**
     * 保存数据集文件标注信息
     *
     * @param annotationInfoCreateDTO 标注详情实体
     */
    private void saveDatasetFileAnnotations(AnnotationInfoCreateDTO annotationInfoCreateDTO) {
        List<AnnotationDTO> annotationDTOS = JSONObject.parseArray(annotationInfoCreateDTO.getAnnotation(), AnnotationDTO.class);
        Long datasetId = annotationInfoCreateDTO.getDatasetId();
        DatasetVersionFile datasetVersionFile = datasetVersionFileService.getDatasetVersionFile(
                datasetId, annotationInfoCreateDTO.getCurrentVersionName(), annotationInfoCreateDTO.getId());
        if (datasetVersionFile == null) {
            throw new BusinessException(ErrorEnum.DATASET_VERSION_FILE_IS_ERROR);
        }
        if (!CollectionUtil.isEmpty(annotationDTOS)) {
            Long versionFileId = datasetVersionFile.getId();
            List<Long> fileLabelIds = annotationDTOS.stream().map(a -> a.getCategoryId()).collect(Collectors.toList());
            List<Long> dbLabelIds = dataFileAnnotationService.findInfoByVersionId(datasetId, versionFileId);
            if (!CollectionUtil.isEmpty(dbLabelIds)) {
                dataFileAnnotationService.deleteAnnotationFileByVersionIdAndLabelIds(datasetId, versionFileId, dbLabelIds);
            }
            dataFileAnnotationService.insertAnnotationFileByVersionIdAndLabelIds(datasetId, versionFileId, fileLabelIds, datasetVersionFile.getFileName());
            //改变文件的状态为标注完成
            fileStateMachine.saveCompleteEvent(datasetVersionFile);
        } else {
            DatasetVersionFile datasetVersionFileSave = new DatasetVersionFile();

            datasetVersionFileSave.setId(datasetVersionFile.getId());
            datasetVersionFileSave.setAnnotationStatus(FileStateEnum.NOT_ANNOTATION_FILE_STATE.getCode());

            datasetVersionFileService.updateStatusById(datasetVersionFileSave);
            dataFileAnnotationService.deleteBatch(datasetId, Arrays.asList(datasetVersionFile.getId()));
        }
    }

    /**
     * 标注完成
     *
     * @param annotationInfoCreateDTO 标注信息
     * @param fileId                  文件id
     * @return int 标注完成的数量
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @DataPermissionMethod
    public void finishManual(Long fileId, Long datasetId, AnnotationInfoCreateDTO annotationInfoCreateDTO) {
        annotationInfoCreateDTO.setDatasetId(datasetId);
        Dataset dataset = datasetService.getOneById(datasetId);
        datasetService.checkPublic(dataset, OperationTypeEnum.UPDATE);
        //判断数据集是否在发布中
        if (!StringUtils.isBlank(dataset.getCurrentVersionName())) {
            Integer dataConversion = datasetVersionService.getDatasetVersionSourceVersion(dataset).getDataConversion();
            if (ConversionStatusEnum.PUBLISHING.getValue().equals(dataConversion)) {
                throw new BusinessException(ErrorEnum.DATASET_PUBLISH_ERROR);
            }
        }
        datasetVersionFileService.deleteByFileId(datasetId, fileId);
        annotationInfoCreateDTO.setId(fileId);
        annotationInfoCreateDTO.setDataType(dataset.getDataType());
        annotationInfoCreateDTO.setCurrentVersionName(dataset.getCurrentVersionName());
        doSave(annotationInfoCreateDTO);
        //解析文本标注Json串将数据保存到DB
        if (AnnotateTypeEnum.TEXT_CLASSIFICATION.getValue().equals(dataset.getAnnotateType())
                || DatatypeEnum.IMAGE.getValue().equals(annotationInfoCreateDTO.getDataType())
                || DatatypeEnum.VIDEO.getValue().equals(annotationInfoCreateDTO.getDataType())
                || AnnotateTypeEnum.AUDIO_CLASSIFY.getValue().equals(dataset.getAnnotateType())) {
            saveDatasetFileAnnotations(annotationInfoCreateDTO);
        }

        if (DatatypeEnum.IMAGE.getValue().equals(annotationInfoCreateDTO.getDataType())) {
            DatasetVersionFile datasetVersionFile = new DatasetVersionFile();
            datasetVersionFile.setDatasetId(dataset.getId());
            datasetVersionFile.setFileId(fileId);
            datasetVersionFile.setVersionName(dataset.getCurrentVersionName());
            //改变文件的状态为标注完成
            fileStateMachine.saveCompleteEvent(datasetVersionFile);
        }

        if (AnnotateTypeEnum.TEXT_SEGMENTATION.getValue().equals(dataset.getAnnotateType())
                || AnnotateTypeEnum.NAMED_ENTITY_RECOGNITION.getValue().equals(dataset.getAnnotateType())
                || AnnotateTypeEnum.SPEECH_RECOGNITION.getValue().equals(dataset.getAnnotateType())
        ) {
            List<AnnotationDTO> annotationDTOS = JSONObject.parseArray(annotationInfoCreateDTO.getAnnotation(), AnnotationDTO.class);
            if (!CollectionUtil.isEmpty(annotationDTOS)) {
                //改变文件的状态为标注完成
                DatasetVersionFile datasetVersionFile = new DatasetVersionFile();
                datasetVersionFile.setDatasetId(annotationInfoCreateDTO.getDatasetId());
                datasetVersionFile.setFileId(annotationInfoCreateDTO.getId());
                datasetVersionFile.setVersionName(annotationInfoCreateDTO.getCurrentVersionName());

                fileStateMachine.saveCompleteEvent(datasetVersionFile);
            } else {
                DatasetVersionFile datasetVersionFile = datasetVersionFileService.getDatasetVersionFile(
                        datasetId, annotationInfoCreateDTO.getCurrentVersionName(), annotationInfoCreateDTO.getId());
                DatasetVersionFile versionFile = DatasetVersionFile.builder().id(datasetVersionFile.getId())
                        .datasetId(datasetVersionFile.getDatasetId())
                        .annotationStatus(FileStateEnum.NOT_ANNOTATION_FILE_STATE.getCode()).build();
                datasetVersionFileService.updateStatusById(versionFile);
                dataFileAnnotationService.deleteBatch(datasetId, Arrays.asList(datasetVersionFile.getId()));
            }
        }
        //改变数据集的状态为标注完成
        dataStateMachine.finishManualEvent(dataset);
        fileService.recoverEsStatus(datasetId, fileId);
        if (DatatypeEnum.TEXT.getValue().equals(dataset.getDataType())
                || DatatypeEnum.TABLE.getValue().equals(dataset.getDataType())
                || AnnotateTypeEnum.TEXT_SEGMENTATION.getValue().equals(dataset.getAnnotateType())
                || AnnotateTypeEnum.NAMED_ENTITY_RECOGNITION.getValue().equals(dataset.getAnnotateType())) {
            UpdateRequest updateRequest = new UpdateRequest(esIndex, "_doc", fileId.toString());
            JSONObject esJsonObject = new JSONObject();
            if (annotationInfoCreateDTO.getAnnotation() == null) {
                esJsonObject.put("labelId", null);
                esJsonObject.put("prediction", null);
                esJsonObject.put("annotation", null);
                esJsonObject.put("status", String.valueOf(FileTypeEnum.UNFINISHED.getValue()));
            } else {
                JSONArray jsonArray = JSONArray.parseArray(annotationInfoCreateDTO.getAnnotation());
                List<String> labelIds = jsonArray.stream().map(json -> {
                    return JSONObject.parseObject(json.toString()).getString("category_id");
                }).collect(Collectors.toList());
                esJsonObject.put("labelId", labelIds);
                esJsonObject.put("prediction", jsonArray.getJSONObject(0).getString("score"));
                esJsonObject.put("status", String.valueOf(FileTypeEnum.FINISHED.getValue()));
                esJsonObject.put("annotation", annotationInfoCreateDTO.getAnnotation());
            }
            updateRequest.doc(esJsonObject, XContentType.JSON);
            updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            try {
                restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                LogUtil.error(LogEnum.BIZ_DATASET, "update es data error:{}", e);
            }
        }
    }

    /**
     * 重新自动标注
     *
     * @param annotationDeleteDTO 标注清除条件
     * @return boolean 清除标注是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @DataPermissionMethod
    public void reAuto(AnnotationDeleteDTO annotationDeleteDTO) {
        Dataset dataset = datasetService.getOneById(annotationDeleteDTO.getDatasetId());
        if (!Objects.isNull(dataset)) {
            verificationAnnotationCondition(dataset.getAnnotateType());
        }
        //判断数据集是否在发布中
        if (!StringUtils.isBlank(dataset.getCurrentVersionName())) {
            Integer dataConversion = datasetVersionService.getDatasetVersionSourceVersion(dataset).getDataConversion();
            if (ConversionStatusEnum.PUBLISHING.getValue().equals(dataConversion)) {
                throw new BusinessException(ErrorEnum.DATASET_PUBLISH_ERROR);
            }
        }
        //改数据集相关状态
        dataStateMachine.deleteAnnotatingEvent(annotationDeleteDTO.getDatasetId());

        //根据当前数据集ID修改Changed字段为改变
        datasetVersionFileService.updateChanged(annotationDeleteDTO.getDatasetId(), dataset.getCurrentVersionName());
    }

    /**
     * 标注文件删除
     *
     * @param files 文件set
     */
    public void delete(Set<File> files) {
        files.forEach(this::delete);
    }

    /**
     * 标注文件删除
     *
     * @param file 文件
     */
    public void delete(File file) {
        if (file == null) {
            return;
        }
        String filePath = fileUtil.getWriteAnnotationAbsPath(file.getDatasetId(), file.getName());
        storeService.delete(filePath);
        LogUtil.info(LogEnum.BIZ_DATASET, "delete file. file:{}", filePath);
    }

    /**
     * 获取任务map
     *
     * @return Map<String, TaskSplitBO> 当前正在进行中的任务(已经发送给算法的)
     */
    @Override
    public Map<String, TaskSplitBO> getTaskPool() {
        return autoAnnotating;
    }

    /**
     * 完成自动标注
     *
     * @param taskId                       子任务id
     * @param batchAnnotationInfoCreateDTO 标注信息
     * @return boolean 标注任务完成return true 失败抛出异常
     */
    @Override
    public boolean finishAuto(String taskId, BatchAnnotationInfoCreateDTO batchAnnotationInfoCreateDTO) {
        LogUtil.info(LogEnum.BIZ_DATASET, "finishAuto log is:" + taskId, batchAnnotationInfoCreateDTO);
        TaskSplitBO taskSplitBO = autoAnnotating.get(taskId);
        if (taskSplitBO == null) {
            throw new BusinessException(ErrorEnum.TASK_SPLIT_ABSENT);
        }
        doFinishAuto(taskSplitBO, batchAnnotationInfoCreateDTO.toMap());
        return true;
    }

    /**
     * 查询需要目标跟踪的数据集
     *
     * @return List<Dataset> 需要目标跟踪的数据集
     */
    public List<Dataset> queryDatasetsToBeTracked() {
        //读所有数据集
        QueryWrapper<Dataset> datasetQueryWrapper = new QueryWrapper<>();
        datasetQueryWrapper.lambda()
                .eq(Dataset::getDataType, DatatypeEnum.VIDEO.getValue())
                .in(Dataset::getStatus, Constant.AUTO_TRACK_NEED_STATUS);
        return datasetService.queryList(datasetQueryWrapper);
    }

    /**
     * 根据当前版本和状态查询文件
     *
     * @param dataset 数据集
     * @return Map<Long, List < DatasetVersionFile>> 根据当前版本和状态查询文件列表
     */
    @Override
    public Map<Long, List<DatasetVersionFile>> queryFileAccordingToCurrentVersionAndStatus(Dataset dataset) {
        Map<Long, List<DatasetVersionFile>> fileMap = new HashMap<>(MagicNumConstant.SIXTEEN);
        //根据数据集读数据集版本文件中间表
        List<DatasetVersionFile> fileList = filterFilesThatNeedToBeTracked(dataset.getId(), dataset.getCurrentVersionName());
        if (fileList != null) {
            fileMap.put(dataset.getId(), fileList);
        }
        return fileMap;
    }

    /**
     * 筛选需要跟踪的文件
     *
     * @param datasetId   数据集id
     * @param versionName 版本名称
     * @return List<DatasetVersionFile> 版本文件列表
     */
    public List<DatasetVersionFile> filterFilesThatNeedToBeTracked(Long datasetId, String versionName) {
        List<DatasetVersionFile> versionFiles = datasetVersionFileService.getFilesByDatasetIdAndVersionName(datasetId, versionName);
        long size = versionFiles.stream().filter(f ->
                !FileStateCodeConstant.AUTO_TAG_COMPLETE_FILE_STATE.equals(f.getAnnotationStatus()) || FileStateCodeConstant.ANNOTATION_COMPLETE_FILE_STATE.equals(f.getAnnotationStatus())).count();
        return size == versionFiles.size() ? versionFiles : null;
    }

    /**
     * 完成自动标注
     *
     * @param taskSplit 标注任务
     * @param resMap    标注文件保存条件
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<Long, AnnotationInfoCreateDTO> doFinishAuto(TaskSplitBO
                                                                   taskSplit, Map<Long, AnnotationInfoCreateDTO> resMap) {
        LogUtil.info(LogEnum.BIZ_DATASET, "finish auto. ts:{}, resMap:{}", taskSplit, resMap);
        //图片状态变更为自动标注完成
        Dataset dataset = datasetService.getOneById(taskSplit.getDatasetId());
        //保存标注信息
        List<Label> labels = datasetLabelService.listLabelByDatasetId(dataset.getId());
        List<Label> uniqueLabels = labels.stream().sorted(Comparator.comparing(Label::getName)).collect(Collectors.toList());
        Map<String, Long> labelNameMap = uniqueLabels.stream().collect(Collectors.toMap(Label::getName, Label::getId, (o, n) -> n));
        taskSplit.getFiles().forEach(fileBO -> {
            AnnotationInfoCreateDTO annotationInfo = resMap.get(fileBO.getId());
            if (annotationInfo == null) {
                return;
            }
            JSONArray jsonArray = JSONObject.parseObject(annotationInfo.getAnnotation(), JSONArray.class);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObject.put("category_id", labelNameMap.get(jsonObject.getString("category_id")));
            }
            storeService.write(fileUtil.getAnnotationAbsPath(taskSplit.getDatasetId(), fileBO.getName()), jsonArray.toJSONString());
            annotationInfo.setAnnotation(jsonArray.toJSONString());
            resMap.put(fileBO.getId(), annotationInfo);
        });
        taskSplit.setVersionName(dataset.getCurrentVersionName());
        List<DatasetVersionFile> versionFiles = datasetVersionFileService.getVersionFileByDatasetAndFile(dataset.getId(), dataset.getCurrentVersionName(), resMap.keySet());
        //清空之前的数据库标注信息
        List<Long> versionFileIds = versionFiles.stream().map(DatasetVersionFile::getId).collect(Collectors.toList());
        dataFileAnnotationService.deleteBatch(dataset.getId(), versionFileIds);
        //写入标签关系
        if (!CollectionUtils.isEmpty(resMap)) {
            List<DataFileAnnotation> dataFileAnnotations = new ArrayList<>();
            versionFiles.forEach(versionFile -> {
                List<Long> dbLabelIds = dataFileAnnotationService.findInfoByVersionId(dataset.getId(), versionFile.getId());
                if (!CollectionUtil.isEmpty(dbLabelIds)) {
                    dataFileAnnotationService.deleteAnnotationFileByVersionIdAndLabelIds(dataset.getId(), versionFile.getId(), dbLabelIds);
                }
                List<AnnotationDTO> annotationDTOS = JSONObject.parseArray(resMap.get(versionFile.getFileId()).getAnnotation(), AnnotationDTO.class);
                if (!CollectionUtils.isEmpty(annotationDTOS)) {
                    if (AnnotateTypeEnum.CLASSIFICATION.getValue().equals(dataset.getAnnotateType())
                            || AnnotateTypeEnum.TEXT_CLASSIFICATION.getValue().equals(dataset.getAnnotateType())) {
                        AnnotationDTO annotationDTO = annotationDTOS.stream().max(Comparator.comparingDouble(AnnotationDTO::getScore)).get();
                        dataFileAnnotations.add(new DataFileAnnotation(dataset.getId(), annotationDTO.getCategoryId(), versionFile.getId(), annotationDTOS.get(0).getScore(), versionFile.getFileName()));
                    }
                    if (AnnotateTypeEnum.OBJECT_DETECTION.getValue().equals(dataset.getAnnotateType())
                            || AnnotateTypeEnum.OBJECT_TRACK.getValue().equals(dataset.getAnnotateType())
                            || AnnotateTypeEnum.SEMANTIC_CUP.getValue().equals(dataset.getAnnotateType())) {
                        annotationDTOS.forEach(annotationDTO -> {
                            dataFileAnnotations.add(new DataFileAnnotation(dataset.getId(), annotationDTO.getCategoryId(), versionFile.getId(), annotationDTO.getScore(), versionFile.getFileName()));
                        });
                    }
                }
            });
            if (!CollectionUtils.isEmpty(dataFileAnnotations)) {
                Queue<Long> dataFileAnnotationIds = generatorKeyUtil.getSequenceByBusinessCode(Constant.DATA_FILE_ANNOTATION, dataFileAnnotations.size());
                for (DataFileAnnotation dataFileAnnotation : dataFileAnnotations) {
                    dataFileAnnotation.setId(dataFileAnnotationIds.poll());
                    dataFileAnnotation.setStatus(DatasetStatusEnum.INIT.getValue());
                    dataFileAnnotation.setInvariable(DatasetLabelEnum.CUSTOM.getType());
                }
                dataFileAnnotationService.insertDataFileBatch(dataFileAnnotations);
            }
        }
        Set<Long> annotationInfoIsNotEmpty = resMap.keySet().stream().filter(k -> !JSON.parseArray(resMap.get(k).getAnnotation()).isEmpty()).collect(Collectors.toSet());

        //嵌入状态机（改变文件状态，标记文件状态被改变）->改变有标注数据的文件
        if (!annotationInfoIsNotEmpty.isEmpty()) {
            fileStateMachine.doFinishAutoAnnotationBatchEvent(annotationInfoIsNotEmpty, taskSplit.getDatasetId(), taskSplit.getVersionName());
        }
        Set<Long> annotationInfoIsEmpty = resMap.keySet().stream().filter(k -> JSON.parseArray(resMap.get(k).getAnnotation()).isEmpty()).collect(Collectors.toSet());

        //嵌入状态机（改变文件状态，标记文件状态被改变）->改变无标注数据的文件
        if (!annotationInfoIsEmpty.isEmpty()) {
            fileStateMachine.doFinishAutoAnnotationInfoIsEmptyBatchEvent(annotationInfoIsEmpty, taskSplit.getDatasetId(), taskSplit.getVersionName());
        }
        if (DatasetStatusEnum.SAMPLING.getValue().equals(taskSplit.getAnnotateType())) {
            List<FileBO> fileBOS = taskSplit.getFiles();
            fileBOS.forEach(fileBO ->
                    fileService.recoverEsStatus(taskSplit.getDatasetId(), fileBO.getId())
            );
        }
        //任务加文件数量
        taskService.finishFile(taskSplit.getTaskId(), taskSplit.getFiles().size(), dataset);
        return resMap;
    }


    /**
     * 完成目标跟踪
     *
     * @param datasetId          数据集id
     * @param autoTrackCreateDTO 自动跟踪结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void finishAutoTrack(Long datasetId, AutoTrackCreateDTO autoTrackCreateDTO) {
        if (!ResponseCode.SUCCESS.equals(autoTrackCreateDTO.getCode())) {
            LogUtil.info(LogEnum.BIZ_DATASET, "auto track is error" + autoTrackCreateDTO.getMsg());
            return;
        }
        LogUtil.info(LogEnum.BIZ_DATASET, "target tracking success modify status");
        Dataset dataset = datasetService.getOneById(datasetId);
        if (dataset == null) {
            LogUtil.error(LogEnum.BIZ_DATASET, "datasetId can't null");
        } else if (!DatatypeEnum.VIDEO.getValue().equals(dataset.getDataType())) {
            LogUtil.error(LogEnum.BIZ_DATASET, "wrong dataset type, not video. dataset:{}", datasetId);
        } else {
            //嵌入状态机（目标跟踪中—>目标跟踪完成）
            dataStateMachine.targetCompleteEvent(dataset);
            //嵌入状态机（自动标注完成->目标跟踪完成）
            fileStateMachine.doFinishAutoTrackEvent(dataset);
            tracking.remove(datasetId);
            LogUtil.info(LogEnum.BIZ_DATASET, "target tracking is complete dataset:{}", datasetId);
        }
        LogUtil.info(LogEnum.BIZ_DATASET, "exception update of target tracking algorithm callback. dataset:{}", datasetId);
    }

    /**
     * 重新目标跟踪
     *
     * @param datasetId
     */
    @Override
    public void track(Long datasetId, Long modelServiceId) {

    }

    /**
     * 重新自动标注更新文件状态
     *
     * @param datasetId 数据集ID
     */
    @Override
    public void deleteAnnotating(Long datasetId) {
        datasetVersionFileService.deleteAnnotating(datasetId);
    }

    @Override
    public void finishAnnotation(JSONObject taskDetail) {
        TaskSplitBO taskSplitBO = JSON.parseObject(JSON.toJSONString(taskDetail), TaskSplitBO.class);
        BatchAnnotationInfoCreateDTO batchAnnotationInfoCreateDTO = buildBatchAnnotationInfoCreateDTO(taskDetail);
        doFinishAuto(taskSplitBO, batchAnnotationInfoCreateDTO.toMap());
    }

    public BatchAnnotationInfoCreateDTO buildBatchAnnotationInfoCreateDTO(JSONObject taskDetail){
        JSONObject jsonObject = JSON.parseObject(taskDetail.get("object").toString(), JSONObject.class);
        JSONArray jsonArray = jsonObject.getJSONArray("annotations");
        List<AnnotationInfoCreateDTO> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(JSON.toJavaObject(jsonArray.getJSONObject(i), AnnotationInfoCreateDTO.class));
        }
        BatchAnnotationInfoCreateDTO batchAnnotationInfoCreateDTO = new BatchAnnotationInfoCreateDTO();
        batchAnnotationInfoCreateDTO.setAnnotations(list);

        return batchAnnotationInfoCreateDTO;
    }

    /**
     * 通过标注类型验证自动标注条件
     *
     * @param annotationType 自动标注类型
     */
    private void verificationAnnotationCondition(Integer annotationType) {
        if (AnnotateTypeEnum.SEMANTIC_CUP.getValue().compareTo(annotationType) == 0) {
            throw new BusinessException(AnnotateTypeEnum.SEMANTIC_CUP.getMsg() + ErrorEnum.DATASET_NOT_ANNOTATION);
        }
    }

    /**
     * 保存数据集文件标注信息
     *
     * @param annotationInfoCreateDTO 标注详情实体
     */
    private void saveDatasetFileAnnotationsByImage(AnnotationInfoCreateDTO annotationInfoCreateDTO) {

        List<AnnotationDTO> annotationDTOS = JSONObject.parseArray(annotationInfoCreateDTO.getAnnotation(), AnnotationDTO.class);
        if (CollectionUtil.isEmpty(annotationDTOS)) {
            return;
        }
        Long datasetId = annotationInfoCreateDTO.getDatasetId();
        DatasetVersionFile datasetVersionFile = datasetVersionFileService.getDatasetVersionFile(
                datasetId, annotationInfoCreateDTO.getCurrentVersionName(), annotationInfoCreateDTO.getId());
        if (Objects.isNull(datasetVersionFile)) {
            throw new BusinessException(ErrorEnum.DATASET_VERSION_FILE_IS_ERROR);
        }
        Long versionFileId = datasetVersionFile.getId();
        List<Long> fileLabelIds = annotationDTOS.stream().map(a -> a.getCategoryId()).collect(Collectors.toList());
        List<Long> dbLabelIds = dataFileAnnotationService.findInfoByVersionId(datasetId, versionFileId);
        if (!CollectionUtil.isEmpty(dbLabelIds)) {
            dataFileAnnotationService.deleteAnnotationFileByVersionIdAndLabelIds(datasetId, versionFileId, dbLabelIds);
        }
        dataFileAnnotationService.insertAnnotationFileByVersionIdAndLabelIds(datasetId, versionFileId, fileLabelIds, datasetVersionFile.getFileName());
    }

    /**
     * 清除es中的标注信息
     *
     * @param datasetId 数据集id
     */
    @Override
    public void deleteEsData(Long datasetId) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("datasetId", datasetId.toString()));
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(esIndex);
        updateByQueryRequest.setRefresh(true).setScript(new Script("ctx._source['status']='101'"))
                .setQuery(boolQueryBuilder);
        try {
            restHighLevelClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            LogUtil.info(LogEnum.BIZ_DATASET, "delete es annotation error:", e);
        }
    }
}