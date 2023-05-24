

package org.dubhe.data.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.dubhe.biz.db.utils.PageDTO;
import org.dubhe.biz.file.api.FileStoreApi;
import org.dubhe.biz.file.dto.FileDTO;
import org.dubhe.biz.file.dto.FilePageDTO;
import org.dubhe.biz.file.utils.MinioUtil;
import org.dubhe.biz.permission.annotation.DataPermissionMethod;
import org.dubhe.biz.base.constant.MagicNumConstant;
import org.dubhe.biz.base.constant.NumberConstant;
import org.dubhe.biz.base.constant.SymbolConstant;
import org.dubhe.biz.base.context.DataContext;
import org.dubhe.biz.base.dto.CommonPermissionDataDTO;
import org.dubhe.biz.base.enums.DatasetTypeEnum;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.base.service.UserContextService;
import org.dubhe.biz.db.utils.PageUtil;
import org.dubhe.biz.db.utils.WrapperHelp;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.biz.redis.utils.RedisUtils;
import org.dubhe.data.machine.statemachine.DataStateMachine;
import org.dubhe.cloud.authconfig.utils.JwtUtils;
import org.dubhe.data.constant.*;
import org.dubhe.data.dao.FileMapper;
import org.dubhe.biz.base.vo.ProgressVO;
import org.dubhe.data.domain.bo.FileAnnotationBO;
import org.dubhe.data.domain.bo.TaskSplitBO;
import org.dubhe.data.domain.bo.TextAnnotationBO;
import org.dubhe.data.domain.dto.*;
import org.dubhe.data.domain.entity.*;
import org.dubhe.data.domain.vo.*;
import org.dubhe.data.machine.constant.FileStateCodeConstant;
import org.dubhe.data.machine.enums.FileStateEnum;
import org.dubhe.data.service.*;
import org.dubhe.data.service.store.IStoreService;
import org.dubhe.data.service.store.MinioStoreServiceImpl;
import org.dubhe.data.util.FileUtil;
import org.dubhe.data.util.GeneratorKeyUtil;
import org.dubhe.data.util.TaskUtils;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.dubhe.data.constant.Constant.ABSTRACT_NAME_PREFIX;


/**
 * @description 文件信息 服务实现类

 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Autowired
    private DataStateMachine dataStateMachine;

    /**
     * 单个标注任务数量
     */
    @Value("${data.annotation.task.splitSize:16}")
    private Integer taskSplitSize;

    /**
     * 默认标注页面文件列表分页大小
     */
    @Value("${data.file.pageSize:20}")
    private Integer defaultFilePageSize;

    /**
     * 路径名前缀
     */
    @Value("${storage.file-store-root-path:/nfs/}")
    private String prefixPath;

    /**
     * minIO公钥
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    /**
     * minIO私钥
     */
    @Value("${minio.secretKey}")
    private String secretKey;

    /**
     * 加密字符串
     */
    @Value("${minio.url}")
    private String url;

    /**
     * 桶名称
     */
    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * esSearch索引
     */
    @Value("${es.index}")
    private String esIndex;

    /**
     * 文件转换
     */
    @Autowired
    private FileConvert fileConvert;

    /**
     * 文件工具类
     */
    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 任务类
     */
    @Autowired
    @Lazy
    private TaskService taskService;

    /**
     * 文件存储服务实现类
     */
    @Resource(type = MinioStoreServiceImpl.class)
    private IStoreService storeService;

    /**
     * 数据集服务实现类
     */
    @Resource
    @Lazy
    private DatasetService datasetService;

    /**
     * 数据集版本文件服务实现类
     */
    @Resource
    @Lazy
    private DatasetVersionFileService datasetVersionFileService;

    @Autowired
    private TaskUtils taskUtils;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private BulkProcessor bulkProcessor;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 数据集版本文件服务实现类
     */
    @Resource
    @Lazy
    private FileMapper fileMapper;

    @Resource
    private UserContextService contextService;

    @Autowired
    private DatasetLabelService datasetLabelService;

    @Autowired
    private DataFileAnnotationService dataFileAnnotationService;

    @Resource(name = "hostFileStoreApiImpl")
    private FileStoreApi fileStoreApi;

    /**
     * 采样算法完成队列
     */
    private static final String SAMPLE_FINISHED_QUEUE_NAME = "videoSample_finished_queue";

    /**
     * 采样算法失败队列
     */
    private static final String SAMPLE_FAILED_QUEUE_NAME = "videoSample_failed_queue";

    /**
     * 采样算法执行中队列
     */
    private static final String START_SAMPLE_QUEUE = "videoSample_processing_queue";

    /**
     * 采样算法未处理队列
     */
    private static final String SAMPLE_PENDING_QUEUE = "videoSample_task_queue";

    /**
     * 采样算法任务详情
     */
    private static final String DETAIL_NAME = "videoSample_pictures:";

    @Autowired
    private GeneratorKeyUtil generatorKeyUtil;


    /**
     * 文件详情
     *
     * @param fileId 文件ID
     * @return FileVO 文件信息
     */
    @Override
    @DataPermissionMethod(dataType = DatasetTypeEnum.PUBLIC)
    public FileVO get(Long fileId, Long datasetId) {
        File file = fileMapper.selectFile(fileId, datasetId);
        Dataset dataset = datasetService.getOneById(datasetId);
        DatasetVersionFile datasetVersionFile = datasetVersionFileService.getDatasetVersionFile(datasetId, dataset.getCurrentVersionName(), fileId);
        if (file == null) {
            return null;
        }
        FileVO fileVO = fileConvert.toDto(file,
                getAnnotation(file.getDatasetId(), FileUtil.interceptFileNameAndDatasetId(datasetId, file.getName()),
                        datasetVersionFile.getVersionName(), datasetVersionFile.getChanged() == NumberConstant.NUMBER_0));
        if (datasetVersionFile.getChanged() == NumberConstant.NUMBER_0) {//未改变时需要替换标签名称为id
            String annotation = fileVO.getAnnotation();
            if (StringUtils.isNotEmpty(annotation)) {
                List<Label> datasetLabels = datasetLabelService.listLabelByDatasetId(dataset.getId());
                Map<String, Long> labelMaps = new HashMap<>();
                datasetLabels.stream().forEach(label -> {
                    labelMaps.put(label.getName(), label.getId());
                });
                JSONArray jsonArray = JSON.parseArray(annotation);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String categoryIdStr = jsonObject.getString("category_id");
                    if (!NumberUtil.isNumber(categoryIdStr)) {
                        if (labelMaps.containsKey(categoryIdStr)) {
                            jsonObject.put("category_id", labelMaps.get(categoryIdStr));
                        }
                    }
                }
                fileVO.setAnnotation(JSON.toJSONString(jsonArray));
            }
        }
        return fileVO;
    }

    /**
     * 获取标注信息
     *
     * @param datasetId 数据集ID
     * @param fileName  文件名
     * @return String
     */
    public String getAnnotation(Long datasetId, String fileName, String versionName, boolean change) {
        String path = fileUtil.getReadAnnotationAbsPath(datasetId, fileName, versionName, change);
        return storeService.read(path);
    }

    /**
     * 判断视频数据集是否已存在视频
     *
     * @param datasetId 数据集ID
     */
    @Override
    public void isExistVideo(Long datasetId) {
        QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.lambda().eq(File::getDatasetId, datasetId);
        if (getBaseMapper().selectCount(fileQueryWrapper) > MagicNumConstant.ZERO) {
            throw new BusinessException(ErrorEnum.VIDEO_EXIST);
        }
    }

    /**
     * 删除文件
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Long> fileIds) {
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(File::getId, fileIds);
        remove(queryWrapper);
    }

    /**
     * 数据集标注进度
     */
    @Override
    public Map<Long, ProgressVO> listStatistics(List<Dataset> datasets) {
        if (CollectionUtils.isEmpty(datasets)) {
            return Collections.emptyMap();
        }
        Map<Long, ProgressVO> res = new HashMap<>(datasets.size());

        // 封装数据集版本数据
        datasets.forEach(dataset -> {
            Map<Integer, Integer> fileStatusMap = datasetVersionFileService.getDatasetVersionFileCount(dataset.getId(), dataset.getCurrentVersionName());
            ProgressVO progressVO = ProgressVO.builder().build();
            if (fileStatusMap != null) {

                Integer notAnnotation = fileStatusMap.getOrDefault(FileStateCodeConstant.NOT_ANNOTATION_FILE_STATE, 0);
                notAnnotation += fileStatusMap.getOrDefault(FileStateCodeConstant.MANUAL_ANNOTATION_FILE_STATE, 0);
                progressVO.setUnfinished(progressVO.getUnfinished() + notAnnotation);

                Integer autoTagComplete = fileStatusMap.getOrDefault(FileStateCodeConstant.AUTO_TAG_COMPLETE_FILE_STATE, 0);
                progressVO.setAutoFinished(progressVO.getAutoFinished() + autoTagComplete);

                Integer annotationComplete = fileStatusMap.getOrDefault(FileStateCodeConstant.ANNOTATION_COMPLETE_FILE_STATE, 0);
                progressVO.setFinished(progressVO.getFinished() + annotationComplete);

                Integer targetComplete = fileStatusMap.getOrDefault(FileStateCodeConstant.TARGET_COMPLETE_FILE_STATE, 0);
                progressVO.setFinishAutoTrack(progressVO.getFinishAutoTrack() + targetComplete);

                Integer annotationNotDistinguish = fileStatusMap.getOrDefault(FileStateCodeConstant.ANNOTATION_NOT_DISTINGUISH_FILE_STATE, 0);
                progressVO.setAnnotationNotDistinguishFile(progressVO.getAnnotationNotDistinguishFile() + annotationNotDistinguish);

            }
            res.put(dataset.getId(), progressVO);
        });
        return res;
    }


    /**
     * 将整体任务分割
     *
     * @param files 文件集合
     * @param task  任务
     * @return List<TaskSplitBO> 任务集合
     */
    @Override
    public List<TaskSplitBO> split(Collection<File> files, Task task) {
        if (CollectionUtils.isEmpty(files)) {
            return new LinkedList<>();
        }
        LogUtil.info(LogEnum.BIZ_DATASET, "split file. file size:{}", files.size());
        Map<Long, List<File>> groupedFiles = files.stream().collect(Collectors.groupingBy(File::getDatasetId));
        List<TaskSplitBO> ts = groupedFiles.values().stream()
                .flatMap(fs -> CollectionUtil.split(fs, taskSplitSize).stream())
                .map(fs -> TaskSplitBO.from(fs, task)).filter(Objects::nonNull).collect(Collectors.toList());
        LogUtil.info(LogEnum.BIZ_DATASET, "split result. split size:{}", ts.size());
        return ts;
    }

    /**
     * 执行文件更新
     *
     * @param ids            文件ID
     * @param fileStatusEnum 文件状态
     * @return int 更新结果
     */
    public int doUpdate(Collection<Long> ids, FileStateEnum fileStatusEnum) {
        if (CollectionUtils.isEmpty(ids)) {
            return MagicNumConstant.ZERO;
        }
        File newObj = File.builder().status(fileStatusEnum.getCode()).build();
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(File::getId, ids);
        return baseMapper.update(newObj, queryWrapper);
    }

    /**
     * 更新文件状态
     *
     * @param files          文件集合
     * @param fileStatusEnum 文件状态
     * @return int 更新结果
     */
    @Override
    public int update(Collection<File> files, FileStateEnum fileStatusEnum) {
        Collection<Long> ids = toIds(files);
        if (CollectionUtils.isEmpty(files)) {
            return MagicNumConstant.ZERO;
        }
        int count = doUpdate(ids, fileStatusEnum);
        if (count == MagicNumConstant.ZERO) {
            throw new BusinessException(ErrorEnum.DATA_ABSENT_OR_NO_AUTH);
        }
        return count;
    }


    /**
     * 通过文件获取ID
     *
     * @param files file文件
     * @return Collection<Long> 文件ID
     */
    private Collection<Long> toIds(Collection<File> files) {
        if (CollectionUtils.isEmpty(files)) {
            return Collections.emptySet();
        }
        return files.stream().map(File::getId).collect(Collectors.toSet());
    }

    /**
     * 更新文件
     *
     * @param fileId         文件ID
     * @param fileStatusEnum 文件状态
     * @return int 更行结果
     */
    public int update(Long fileId, FileStateEnum fileStatusEnum) {
        File newObj = File.builder()
                .id(fileId)
                .status(fileStatusEnum.getCode())
                .build();
        return baseMapper.updateById(newObj);
    }

    /**
     * 更新文件
     *
     * @param fileId         文件ID
     * @param fileStatusEnum 文件状态
     * @param originStatus   文件状态
     * @return boolean 更行结果
     */
    public boolean update(Long fileId, FileStateEnum fileStatusEnum, FileStateEnum originStatus) {
        if (getById(fileId) == null) {
            return true;
        }
        UpdateWrapper<File> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(File::getId, fileId).eq(File::getStatus, originStatus.getCode())
                .set(File::getId, fileId).set(File::getStatus, fileStatusEnum.getCode());
        return update(updateWrapper);
    }

    /**
     * 保存文件
     *
     * @param fileId 文件ID
     * @param files  file文件
     * @return List<Long> 保存的文件id集合
     */
    @Override
    public List<File> saveFiles(Long fileId, List<FileCreateDTO> files) {
        LogUtil.debug(LogEnum.BIZ_DATASET, "save files start, file size {}", files.size());
        Long start = System.currentTimeMillis();
        Map<String, String> fail = new HashMap<>(files.size());
        List<File> newFiles = new ArrayList<>();
        Long datasetUserId = datasetService.getOneById(fileId).getCreateUserId();
        files.stream().map(file -> FileCreateDTO.toFile(file, fileId, datasetUserId)).forEach(f -> {
            try {
                newFiles.add(f);
            } catch (DuplicateKeyException e) {
                fail.put(f.getName(), "the file already exists");
            }
        });
        if (!CollectionUtils.isEmpty(fail)) {
            throw new BusinessException(ErrorEnum.FILE_EXIST);
        }
        Queue<Long> dataFileIds = generatorKeyUtil.getSequenceByBusinessCode(Constant.DATA_FILE, newFiles.size());
        for (File f : newFiles) {
            f.setId(dataFileIds.poll());
        }
        baseMapper.saveList(newFiles, JwtUtils.getCurUserId(), datasetUserId);
        LogUtil.debug(LogEnum.BIZ_DATASET, "save files end, times {}", (System.currentTimeMillis() - start));
        return newFiles;
    }

    /**
     * 保存视频文件
     *
     * @param fileId 视频文件ID
     * @param files  file文件
     * @param type   文件类型
     * @param pid    文件父ID
     * @param userId 用户ID
     * @return List<File> 文件列表
     */
    @Override
    public List<File> saveVideoFiles(Long fileId, List<FileCreateDTO> files, int type, Long pid, Long userId) {
        List<File> list = new ArrayList<>();
        Long createUserId = datasetService.getOneById(fileId).getCreateUserId();
        files.forEach(fileCreateDTO -> {
            File file = FileCreateDTO.toFile(fileCreateDTO, fileId, type, pid);
            list.add(file);
        });
        Queue<Long> dataFileIds = generatorKeyUtil.getSequenceByBusinessCode(Constant.DATA_FILE, list.size());
        for (File f : list) {
            f.setId(dataFileIds.poll());
        }
        baseMapper.saveList(list, userId, createUserId);
        return list;
    }

    /**
     * 创建查询
     *
     * @param datasetId 数据集ID
     * @param status    状态
     * @return QueryWrapper<File> 查询条件
     */
    public QueryWrapper<File> buildQuery(Long datasetId, Set<Integer> status) {
        FileQueryCriteriaVO criteria = FileQueryCriteriaVO.builder()
                .datasetId(datasetId).order("id ASC").build();
        return WrapperHelp.getWrapper(criteria);
    }

    /**
     * 获取offset
     *
     * @param datasetId 数据集ID
     * @param fileId    文件ID
     * @param type      数据集类型
     * @return Integer 获取到offset
     */
    @Override
    public Integer getOffset(Long fileId, Long datasetId, Integer[] type, Long[] labelIds) {
        Integer offset = datasetVersionFileService.getOffset(fileId, datasetId, type, labelIds);
        return offset == MagicNumConstant.ZERO ? null : offset - MagicNumConstant.ONE;
    }


    /**
     * 文件查询，物体检测标注页面使用
     *
     * @param datasetId 数据集ID
     * @param offset    Offset
     * @param limit     页容量
     * @param page      分页条件
     * @param typeList  数据集类型
     * @return Page<File> 文件查询分页列表
     */
    @Override
    @DataPermissionMethod(dataType = DatasetTypeEnum.PUBLIC)
    public PageDTO<File> listByLimit(Long datasetId, Long offset, Integer limit, Integer page, Set<Integer> typeSet, List<Long> labelIdList) {
        if (page == null) {
            page = MagicNumConstant.ONE;
        }
        if (offset == null) {
            offset = getDefaultOffset();
        }
        if (limit == null) {
            limit = defaultFilePageSize;
        }
        //查询数据集
        Dataset dataset = datasetService.getOneById(datasetId);
        //查询当前数据集下所有的文件(中间表)
        List<DatasetVersionFileDTO> datasetVersionFiles = datasetVersionFileService
                .getListByDatasetIdAndAnnotationStatus(dataset.getId(), dataset.getCurrentVersionName(), typeSet, offset,
                        limit, "id", null, labelIdList);
        if (datasetVersionFiles == null || datasetVersionFiles.isEmpty()) {
            Page<File> filePage = new Page<>();
            filePage.setCurrent(page);
            filePage.setSize(limit);
            filePage.setTotal(NumberConstant.NUMBER_0);
            return PageUtil.toPage(filePage);
        }
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        Set<Long> fileIds = datasetVersionFiles
                .stream()
                .map(DatasetVersionFileDTO::getFileId)
                .collect(Collectors.toSet());

        queryWrapper.in("id", fileIds)
                .eq("dataset_id", dataset.getId());
        List<File> files = baseMapper.selectList(queryWrapper);
        //将所有文件的状态放入
        files.forEach(v -> {
            datasetVersionFiles.forEach(d -> {
                if (v.getId().equals(d.getFileId())) {
                    v.setStatus(d.getAnnotationStatus());
                }
            });
        });
        //文件重排序（按照版本文件排序）
        List<File> fileArrayList = new ArrayList<>();
        datasetVersionFiles.forEach(datasetVersionFileDTO -> {
            files.forEach(file -> {
                if (datasetVersionFileDTO.getFileId().equals(file.getId())) {
                    file.setName(FileUtil.interceptFileNameAndDatasetId(datasetId, file.getName()));
                    fileArrayList.add(file);
                }
            });
        });
        Page<File> pages = new Page<>();
        if (!labelIdList.isEmpty()) {
            Long count = dataFileAnnotationService.selectDetectionCount(datasetId, dataset.getCurrentVersionName(), labelIdList);
            pages.setTotal(count);
        } else {
            int count = datasetVersionFileService.selectFileListTotalCount(dataset.getId(), dataset.getCurrentVersionName(), typeSet, labelIdList);
            pages.setTotal(count);
        }

        pages.setRecords(fileArrayList);
        pages.setSize(limit);
        pages.setCurrent(page);
        return PageUtil.toPage(pages, fileArrayList);
    }

    /**
     * 文件查询
     */
    @Override
    @DataPermissionMethod(dataType = DatasetTypeEnum.PUBLIC)
    public PageDTO<FileVO> listPage(Long datasetId, Page page, FileQueryCriteriaVO queryCriteria) {
        Dataset dataset = datasetService.getOneById(queryCriteria.getDatasetId());
        List<DatasetVersionFileDTO> datasetVersionFiles = commDatasetVersionFiles(datasetId, dataset.getCurrentVersionName(), page, queryCriteria);
        if (datasetVersionFiles == null || datasetVersionFiles.isEmpty()) {
            return PageDTO.EMPTY_PAGE;
        }
        List<File> files = getFileList(datasetVersionFiles, datasetId);
        //将所有文件的状态放入
        files.forEach(v -> {
            datasetVersionFiles.forEach(d -> {
                if (v.getId().equals(d.getFileId())) {
                    v.setStatus(d.getAnnotationStatus());
                }
                d.setVersionName(dataset.getCurrentVersionName());
            });
        });
        //文件重排序（按照版本文件排序）
        List<File> fileArrayList = new ArrayList<>();
        datasetVersionFiles.forEach(v -> {
            files.forEach(f -> {
                if (v.getFileId().equals(f.getId())) {
                    fileArrayList.add(f);
                }
            });
        });
        Map<Long, File> fileListMap = files.stream().collect(Collectors.toMap(File::getId, obj -> obj));
        List<Label> datasetLabels = datasetLabelService.listLabelByDatasetId(dataset.getId());
        Map<String, Long> labelMaps = new HashMap<>();
        datasetLabels.stream().forEach(label -> {
            labelMaps.put(label.getName(), label.getId());
        });
        List<FileVO> vos = datasetVersionFiles.stream().map(versionFile -> {
            FileVO fileVO = FileVO.builder().build();
            if (!Objects.isNull(fileListMap.get(versionFile.getFileId()))) {
                File file = fileListMap.get(versionFile.getFileId());
                BeanUtil.copyProperties(file, fileVO);
                fileVO.setLabelIdList(versionFile.getLabelIdList());
                fileVO.setPrediction(versionFile.getPrediction());
                fileVO.setAnnotation(getAnnotation(datasetId, FileUtil.interceptFileNameAndDatasetId(datasetId, file.getName()), versionFile.getVersionName(), versionFile.getChanged() == NumberConstant.NUMBER_0));
            }
            if (versionFile.getChanged() == NumberConstant.NUMBER_0) {
                String annotation = fileVO.getAnnotation();
                if (StringUtils.isNotEmpty(annotation)) {
                    JSONArray jsonArray = JSON.parseArray(annotation);

                    List<Long> labels = new ArrayList<>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String categoryIdStr = jsonObject.getString("category_id");
                        if (!NumberUtil.isNumber(categoryIdStr)) {
                            if (labelMaps.containsKey(categoryIdStr)) {
                                jsonObject.put("category_id", labelMaps.get(categoryIdStr));
                                labels.add( labelMaps.get(categoryIdStr));
                            }
                        } else {
                            labels.add(jsonObject.getLong("category_id"));
                        }
                    }
                    fileVO.setAnnotation(JSON.toJSONString(jsonArray));
                    fileVO.setLabelIdList(labels);
                }
            }
            return fileVO;
        }).collect(Collectors.toList());
        Page<File> pages = buildPages(page, files, dataset, queryCriteria);
        return PageUtil.toPage(pages, vos);
    }

    /**
     * 获取首个文件
     *
     * @param datasetId 数据集id
     * @param type      数据集类型
     * @return Long 首个文件Id
     */
    @Override
    @DataPermissionMethod(dataType = DatasetTypeEnum.PUBLIC)
    public Long getFirst(Long datasetId, Integer type) {
        Dataset dataset = datasetService.getOneById(datasetId);
        DatasetVersionFile datasetVersionFile = datasetVersionFileService
                .getFirstByDatasetIdAndVersionNum(datasetId, dataset.getCurrentVersionName(), FileTypeEnum.getStatus(type));
        return datasetVersionFile == null ? null : datasetVersionFile.getFileId();
    }


    /**
     * 默认offset
     *
     * @return Long 默认offset
     */
    public Long getDefaultOffset() {
        return MagicNumConstant.ZERO_LONG;
    }

    /**
     * 如果ids为空，则返回空
     *
     * @param fileIds 文件id集合
     * @return Set<File> 文件集合
     */
    @Override
    public Set<File> get(List<Long> fileIds, Long datasetId) {
        if (CollectionUtils.isEmpty(fileIds)) {
            return new HashSet<>();
        }
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dataset_id", datasetId);
        queryWrapper.eq("id", fileIds.get(MagicNumConstant.ZERO));
        File fileOne = baseMapper.selectOne(queryWrapper);
        if (fileOne == null) {
            return new HashSet<>();
        }
        QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.eq("dataset_id", fileOne.getDatasetId());
        fileQueryWrapper.in("id", fileIds);
        return new HashSet(baseMapper.selectList(fileQueryWrapper));
    }

    /**
     * 视频采样任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void videoSample(String finishedQueue, String failedQueue) {
        try {
            Object object = taskUtils.getFinishedTask(finishedQueue);
            if (ObjectUtil.isNotNull(object)) {
                JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(redisUtils.get(object.toString())));
                String datasetIdAndSub = jsonObject.getString("datasetIdAndSub");
                List<String> pictureNames = JSON.parseObject(jsonObject.getString("pictureNames"), ArrayList.class);
                Long datasetId = Long.valueOf(StringUtils.substringBefore(String.valueOf(datasetIdAndSub), ":"));
                QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
                taskQueryWrapper.lambda().eq(Task::getId, Long.valueOf(jsonObject.getString("id")));
                Task task = taskService.selectOne(taskQueryWrapper);
                if (taskService.isStop(task.getId())) {
                    redisUtils.del(object.toString());
                    redisUtils.del(object.toString().replace("annotation", "detail"));
                    return;
                }
                Integer segment = Integer.valueOf(StringUtils.substringAfter(String.valueOf(datasetIdAndSub), ":"));
                if (segment.equals(task.getFinished() + MagicNumConstant.ONE)) {
                    try {
                        videSampleFinished(pictureNames, task);
                    } catch (Exception exception) {
                        LogUtil.error(LogEnum.BIZ_DATASET, "videoFinishedTask exception:{}", exception);
                    }
                    redisUtils.del(object.toString());
                    redisUtils.del(object.toString().replace("annotation", "detail"));
                } else {
                    //再将元素放入队列
                    redisUtils.zAdd(object.toString().replace("task", "finished"), System.currentTimeMillis() / 1000, ("\"" + object.toString() + "\"").getBytes("utf-8"));
                }
            } else {
                TimeUnit.MILLISECONDS.sleep(MagicNumConstant.THREE_THOUSAND);
            }
        } catch (Exception e) {
            LogUtil.error(LogEnum.BIZ_DATASET, "get videoSample finish task failed:{}", e);
        }
        try {
            Object object = taskUtils.getFailedTask(failedQueue);
            if (ObjectUtil.isNotNull(object)) {
                String taskId = object.toString();
                JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(redisUtils.get(taskId)));
                String datasetIdAndSub = jsonObject.getString("datasetIdAndSub");
                videoSampleFailed(datasetIdAndSub);
            }
        } catch (Exception e) {
            LogUtil.error(LogEnum.BIZ_DATASET, "get videoSample failed task failed:{}", e);
        }
    }

    /**
     * 采样失败任务处理
     *
     * @param failedId 采样失败任务ID
     */
    public void videoSampleFailed(String failedId) {
        Long datasetId = Long.valueOf(StringUtils.substringBefore(String.valueOf(failedId), ":"));
        dataStateMachine.samplingFailureEvent(datasetId);
    }

    /**
     * 采样完成任务处理
     *
     * @param picNames 完成后图片名称
     * @param task     采样任务
     */
    public void videSampleFinished(List<String> picNames, Task task) {
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(File::getDatasetId, task.getDatasetId())
                .eq(File::getFileType, MagicNumConstant.ONE)
                .eq(File::getStatus, FileTypeEnum.UNFINISHED.getValue())
                .eq(File::getId, task.getTargetId());
        File file = getBaseMapper().selectOne(queryWrapper);
        saveVideoPic(picNames, file);
        task.setFinished(task.getFinished() + MagicNumConstant.ONE);
        taskService.updateByTaskId(task);
        //单个视频采样完成
        if (task.getTotal().equals(task.getFinished())) {
            file.setStatus(FileStateCodeConstant.AUTO_TAG_COMPLETE_FILE_STATE);
            getBaseMapper().updateFileStatus(file.getDatasetId(), file.getId(), file.getStatus());
            QueryWrapper<File> statusQuery = new QueryWrapper<>();
            statusQuery.lambda().eq(File::getDatasetId, task.getDatasetId())
                    .eq(File::getFileType, MagicNumConstant.ONE)
                    .ne(File::getStatus, FileStateCodeConstant.AUTO_TAG_COMPLETE_FILE_STATE);
            Integer unfinishedNum = baseMapper.selectCount(statusQuery);

            if (unfinishedNum.equals(MagicNumConstant.ZERO)) {
                dataStateMachine.samplingEvent(file.getDatasetId());
            }
        }
    }

    /**
     * 保存采样后文件
     *
     * @param picNames 图片文件名字
     * @param file     视频文件
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveVideoPic(List<String> picNames, File file) {
        Collections.reverse(picNames);
        List<FileCreateDTO> fileCreateDTOS = new ArrayList<>();
        picNames.forEach(picName -> {
            picName = StringUtils.substringAfter(picName, prefixPath);
            FileCreateDTO f = FileCreateDTO.builder()
                    .url(picName)
                    .build();
            fileCreateDTOS.add(f);
        });
        List<File> files = saveVideoFiles(file.getDatasetId(), fileCreateDTOS, DatatypeEnum.IMAGE.getValue(), file.getId(), file.getCreateUserId());
        List<DatasetVersionFile> datasetVersionFiles = new ArrayList<>();
        files.forEach(fileOne -> {
            DatasetVersionFile datasetVersionFile = new DatasetVersionFile(file.getDatasetId(), null, fileOne.getId(), fileOne.getName());
            datasetVersionFiles.add(datasetVersionFile);
        });
        datasetVersionFileService.insertList(datasetVersionFiles);
    }

    /**
     * 批量更新file
     *
     * @param datasetVersionFiles 文件列表
     * @param init                更新结果
     */
    public void updateStatus(List<DatasetVersionFile> datasetVersionFiles, FileStateEnum init) {
        List<Long> fileIds = datasetVersionFiles
                .stream().map(DatasetVersionFile::getFileId)
                .collect(Collectors.toList());
        UpdateWrapper<File> fileUpdateWrapper = new UpdateWrapper();
        fileUpdateWrapper.in("id", fileIds);
        File file = new File();
        file.setStatus(init.getCode());
        baseMapper.update(file, fileUpdateWrapper);
    }


    /**
     * 获取文件对应所有增强文件
     *
     * @param fileId 文件id
     * @return List<File> 文件对应所有增强文件
     */
    @Override
    @DataPermissionMethod(dataType = DatasetTypeEnum.PUBLIC)
    public List<File> getEnhanceFileList(Long fileId, Long datasetId) {
        File file = baseMapper.getOneById(fileId, datasetId);

        if (ObjectUtil.isNull(file)) {
            throw new BusinessException(ErrorEnum.FILE_ABSENT);
        }
        Dataset dataset = datasetService.getOneById(file.getDatasetId());
        if (ObjectUtil.isNull(dataset)) {
            throw new BusinessException(ErrorEnum.DATASET_NOT_EXIST);
        }
        int enhanceFileCount = datasetVersionFileService.getEnhanceFileCount(dataset.getId(), dataset.getCurrentVersionName());
        if (enhanceFileCount > 0) {
            return datasetVersionFileService.getEnhanceFileList(dataset.getId(), dataset.getCurrentVersionName(), fileId);
        }
        return null;
    }

    /**
     * 获取文件详情
     *
     * @param fileId 文件ID
     * @return File 文件详情
     */
    @Override
    public File selectById(Long fileId, Long datasetId) {
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dataset_id", datasetId);
        queryWrapper.eq("id", fileId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 条件搜索获取文件详情
     *
     * @param queryWrapper 查询条件
     * @return 文件详情
     */
    @Override
    public File selectOne(QueryWrapper<File> queryWrapper) {
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 获取文件列表
     *
     * @param wrapper 查询条件
     * @return 文件列表
     */
    @Override
    public List<File> listFile(QueryWrapper<File> wrapper) {
        return list(wrapper);
    }

    /**
     * 批量获取文件列表
     *
     * @param datasetId 数据集ID
     * @param offset    偏移量
     * @param batchSize 批大小
     * @param status    文件标注状态
     * @return 文件列表
     */
    @Override
    public List<File> listBatchFile(Long datasetId, int offset, int batchSize, Collection<Integer> status) {
        try {
            Dataset dataset = datasetService.getOneById(datasetId);
            return baseMapper.selectListOne(datasetId, dataset.getCurrentVersionName(), offset, batchSize, status);
        } catch (Exception e) {
            LogUtil.error(LogEnum.BIZ_DATASET, "get annotation files error {}", e);
            return null;
        }
    }

    /**
     * 判断执行中的采样任务是否过期
     */
    @Override
    public void expireSampleTask() {
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = taskUtils.zGetWithScore(START_SAMPLE_QUEUE);
        typedTuples.forEach(value -> {
            String timestampString = new BigDecimal(StringUtils.substringBefore(value.getScore().toString(), "."))
                    .toPlainString();
            long timestamp = Long.parseLong(timestampString);
            String keyId = JSONObject.parseObject(JSON.toJSONString(value.getValue())).getString("datasetIdKey");
            long timestampNow = System.currentTimeMillis() / 1000;
            if (timestampNow - timestamp > MagicNumConstant.TWO_HUNDRED) {
                LogUtil.info(LogEnum.BIZ_DATASET, "restart videoSample task keyId:{}", keyId);
//                taskUtils.restartTask(keyId, START_SAMPLE_QUEUE, SAMPLE_PENDING_QUEUE, DETAIL_NAME
//                        , JSON.toJSONString(value.getValue()));
            }
        });
    }

    /**
     * 根据版本和数据集ID获取文件url
     *
     * @param datasetId   数据集ID
     * @param versionName 版本名
     * @return List<String> url列表
     */
    @Override
    public List<String> selectUrls(Long datasetId, String versionName) {
        return baseMapper.selectUrls(datasetId, versionName);
    }

    /**
     * 根据version.changed获取文件name列表
     *
     * @param datasetId   数据集ID
     * @param changed     版本文件是否改动
     * @param versionName 版本名称
     * @return List<FileAnnotationBO>   名称列表
     */
    @Override
    public List<FileAnnotationBO> selectFileAnnotations(Long datasetId, Integer changed, String versionName) {
        return baseMapper.selectFileAnnotations(datasetId, changed, versionName);
    }

    /**
     * 公共获取版本文件列表
     *
     * @param datasetId          数据集ID
     * @param currentVersionName 当前版本文件
     * @param page               分页
     * @param queryCriteria      查询实体
     * @return List<DatasetVersionFileDTO> 版本文件列表
     */
    private List<DatasetVersionFileDTO> commDatasetVersionFiles(Long datasetId, String currentVersionName, Page page, FileQueryCriteriaVO queryCriteria) {
        queryCriteria.setDatasetId(datasetId);
        queryCriteria.setFileType(DatatypeEnum.IMAGE.getValue());

        Set<Integer> statusSet = findStatus(queryCriteria.getStatusList(), queryCriteria.getAnnotateStatusList(), queryCriteria.getAnnotateTypeList());

        //根据数据集ID和版本名称以及状态查询出当前数据集的所有文件
        List<DatasetVersionFileDTO> datasetVersionFiles = datasetVersionFileService
                .getListByDatasetIdAndAnnotationStatus(datasetId,
                        currentVersionName,
                        statusSet,
                        (page.getCurrent() - 1) * page.getSize(),
                        (int) page.getSize(),
                        queryCriteria.getSort(),
                        queryCriteria.getOrder(),
                        queryCriteria.getLabelIdList()
                );
        return datasetVersionFiles;
    }

    /**
     * 图像分类筛选状态
     */
    private Set<Integer> findStatus(List<Integer> statusList, List<Integer> annotateStatusList, List<Integer> annotateTypeList) {
        Set<Integer> statusResult = new HashSet<>();
        // 根据有无标注信息参数获取对应文件状态列表
        statusResult.addAll(FileTypeEnum.getStatus(statusList.get(0)));
        // 如果有标注状态，则需要获取标注状态，并和有无标注信息对应状态取交集
        if (!annotateStatusList.isEmpty()) {
            statusResult.retainAll(FileTypeEnum.getStatus(annotateStatusList));
        }
        // 如果有标注方式，则需要获取对应文件状态，并和有无标注信息对应状态取交集
        if (!annotateTypeList.isEmpty()) {
            statusResult.retainAll(FileTypeEnum.getStatus(annotateTypeList));
        }
        // 用于解决当用户选择状态，但是交集为空时会查出所有从而导致条件失效的问题
        statusResult.add(-1);
        return new HashSet(statusResult);
    }

    /**
     * 音频数据集文件查询
     */
    @Override
    public PageDTO<TxtFileVO> audioFilesByPage(Long datasetId, Page page, FileQueryCriteriaVO queryCriteria) {
        //查询数据集
        Dataset dataset = datasetService.getOneById(queryCriteria.getDatasetId());
        if (DatasetTypeEnum.PUBLIC.getValue().equals(dataset.getType())) {
            DataContext.set(CommonPermissionDataDTO.builder().type(true).build());
        }
        List<File> files = new ArrayList<>();
        List<TxtFileVO> vos = new ArrayList<>();
        try {
            List<DatasetVersionFileDTO> datasetVersionFiles = commDatasetVersionFiles(datasetId, dataset.getCurrentVersionName(), page, queryCriteria);
            if (datasetVersionFiles == null || datasetVersionFiles.isEmpty()) {
                return PageDTO.EMPTY_PAGE;
            }
            files = getFileList(datasetVersionFiles, datasetId);
            Map<Long, File> fileListMap = files.stream().collect(Collectors.toMap(File::getId, obj -> obj));
            vos = datasetVersionFiles.stream().map(versionFile -> {
                TxtFileVO fileVO = TxtFileVO.builder().build();
                if (!Objects.isNull(fileListMap.get(versionFile.getFileId()))) {
                    File file = fileListMap.get(versionFile.getFileId());
                    BeanUtil.copyProperties(file, fileVO);
                    fileVO.setPrediction(versionFile.getPrediction());
                    fileVO.setLabelIdList(versionFile.getLabelIdList());
                    fileVO.setAbstractName(Constant.ABSTRACT_NAME_PREFIX + file.getName());
                    String afterPath = StringUtils.substringAfterLast(fileVO.getUrl(), SymbolConstant.SLASH);
                    String beforePath = StringUtils.substringBeforeLast(fileVO.getUrl(), SymbolConstant.SLASH);
                    String newPath = beforePath + SymbolConstant.SLASH + ABSTRACT_NAME_PREFIX + afterPath;
                    fileVO.setAbstractUrl(newPath);
                    fileVO.setStatus(versionFile.getAnnotationStatus());
                    fileVO.setAnnotation(getAnnotation(datasetId, FileUtil.interceptFileNameAndDatasetId(datasetId, file.getName()), versionFile.getVersionName(), versionFile.getChanged() == NumberConstant.NUMBER_0));
                }
                return fileVO;
            }).collect(Collectors.toList());
        } finally {
            if (DatasetTypeEnum.PUBLIC.getValue().equals(dataset.getType())) {
                DataContext.remove();
            }
        }
        Page<File> pages = buildPages(page, files, dataset, queryCriteria);
        return PageUtil.toPage(pages, vos);
    }

    /**
     * 文本数据集文件查询
     */
    @Override
    public PageDTO<TxtFileVO> txtContentByPage(Long datasetId, Page page, FileQueryCriteriaVO fileQueryCriteria) {
        SearchRequest searchRequest = new SearchRequest(esIndex);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = null;
        BoolQueryBuilder boolQueryBuilder = null;
        if (fileQueryCriteria.getAnnotateTypeList() == null || fileQueryCriteria.getAnnotateTypeList().isEmpty()) {
            if (fileQueryCriteria.getStatusList().get(0).equals(FileTypeEnum.UNFINISHED_FILE.getValue())) {
                boolQueryBuilder = QueryBuilders.boolQuery()
                        .must(fileQueryCriteria.getContent() == null ? QueryBuilders.matchAllQuery()
                                : QueryBuilders.matchPhraseQuery("content", fileQueryCriteria.getContent()))
                        .must(QueryBuilders.termQuery("datasetId", fileQueryCriteria.getDatasetId().toString()))
                        .must(QueryBuilders.termsQuery("status"
                                , FileStateCodeConstant.NOT_ANNOTATION_FILE_STATE.toString()
                                , FileStateCodeConstant.ANNOTATION_NOT_DISTINGUISH_FILE_STATE.toString()));
            }
            if (fileQueryCriteria.getStatusList().get(0).equals(FileTypeEnum.FINISHED_FILE.getValue())) {
                boolQueryBuilder = QueryBuilders.boolQuery()
                        .must(fileQueryCriteria.getContent() == null ? QueryBuilders.matchAllQuery()
                                : QueryBuilders.matchPhraseQuery("content", fileQueryCriteria.getContent()))
                        .must(QueryBuilders.termQuery("datasetId", fileQueryCriteria.getDatasetId().toString()))
                        .must(QueryBuilders.termsQuery("status"
                                , FileStateCodeConstant.AUTO_TAG_COMPLETE_FILE_STATE.toString()
                                , FileStateCodeConstant.ANNOTATION_COMPLETE_FILE_STATE.toString()));
            }
            if (fileQueryCriteria.getStatusList().get(0).equals(FileTypeEnum.HAVE_ANNOTATION.getValue())) {
                boolQueryBuilder = QueryBuilders.boolQuery()
                        .must(fileQueryCriteria.getContent() == null ? QueryBuilders.matchAllQuery()
                                : QueryBuilders.matchPhraseQuery("content", fileQueryCriteria.getContent()))
                        .must(QueryBuilders.termQuery("datasetId", fileQueryCriteria.getDatasetId().toString()))
                        .must(QueryBuilders.termsQuery("status"
                                , FileStateCodeConstant.MANUAL_ANNOTATION_FILE_STATE.toString()
                                , FileStateCodeConstant.AUTO_TAG_COMPLETE_FILE_STATE.toString()
                                , FileStateCodeConstant.ANNOTATION_COMPLETE_FILE_STATE.toString()
                                , FileStateCodeConstant.TARGET_COMPLETE_FILE_STATE.toString()));
            }
            if (fileQueryCriteria.getStatusList().get(0).equals(FileTypeEnum.NO_ANNOTATION.getValue())) {
                boolQueryBuilder = QueryBuilders.boolQuery()
                        .must(fileQueryCriteria.getContent() == null ? QueryBuilders.matchAllQuery()
                                : QueryBuilders.matchPhraseQuery("content", fileQueryCriteria.getContent()))
                        .must(QueryBuilders.termQuery("datasetId", fileQueryCriteria.getDatasetId().toString()))
                        .must(QueryBuilders.termsQuery("status"
                                , FileStateCodeConstant.NOT_ANNOTATION_FILE_STATE.toString()
                                , FileStateCodeConstant.ANNOTATION_NOT_DISTINGUISH_FILE_STATE.toString()));
            }
        } else {
            if (fileQueryCriteria.getAnnotateTypeList().size() == MagicNumConstant.ONE) {
                boolQueryBuilder = QueryBuilders.boolQuery()
                        .must(fileQueryCriteria.getContent() == null ? QueryBuilders.matchAllQuery()
                                : QueryBuilders.matchPhraseQuery("content", fileQueryCriteria.getContent()))
                        .must(QueryBuilders.termQuery("datasetId", fileQueryCriteria.getDatasetId().toString()))
                        .must(QueryBuilders.termsQuery("status"
                                , fileQueryCriteria.getAnnotateTypeList().get(0).toString()));
            } else if (fileQueryCriteria.getAnnotateTypeList().size() == MagicNumConstant.TWO) {
                boolQueryBuilder = QueryBuilders.boolQuery()
                        .must(fileQueryCriteria.getContent() == null ? QueryBuilders.matchAllQuery()
                                : QueryBuilders.matchPhraseQuery("content", fileQueryCriteria.getContent()))
                        .must(QueryBuilders.termQuery("datasetId", fileQueryCriteria.getDatasetId().toString()))
                        .must(QueryBuilders.termsQuery("status"
                                , fileQueryCriteria.getAnnotateTypeList().get(0).toString()
                                , fileQueryCriteria.getAnnotateTypeList().get(1).toString()));
            }
        }
        Dataset dataset = datasetService.getOneById(datasetId);
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("versionName", StringUtils.isEmpty(dataset.getCurrentVersionName()) ? "V0000" : dataset.getCurrentVersionName()));
        if (fileQueryCriteria.getLabelIdList() != null) {
            queryBuilder = boolQueryBuilder.must(QueryBuilders.termsQuery("labelId", fileQueryCriteria.getLabelIdList()));
        } else {
            queryBuilder = boolQueryBuilder;
        }
        sourceBuilder.query(queryBuilder);
        sourceBuilder.from((int) (page.getSize() * (page.getCurrent() - 1)));
        sourceBuilder.size((int) page.getSize());
        sourceBuilder.sort(new FieldSortBuilder("updateTime.keyword").order(SortOrder.DESC).unmappedType("long"));
        sourceBuilder.sort(new FieldSortBuilder("createTime.keyword").order(SortOrder.DESC).unmappedType("long"));
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='highlight'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.field("content");
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.trackTotalHits(true);
        searchRequest.source(sourceBuilder);
        List<TxtFileVO> vos = new ArrayList<>();
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = searchResponse.getHits().getHits();
            for (int i = 0; i < hits.length; i++) {
                EsDataFileDTO esDataFileDTO = JSON.parseObject(hits[i].getSourceAsString(), EsDataFileDTO.class);
                StringBuilder highlightContent = new StringBuilder();
                if (fileQueryCriteria.getContent() != null) {
                    Map<String, HighlightField> highlightFields = hits[i].getHighlightFields();
                    Text[] fragments = highlightFields.get("content").getFragments();
                    for (Text text : fragments) {
                        highlightContent.append(text);
                    }
                }
                TxtFileVO txtFileVO = new TxtFileVO();
                txtFileVO.setPrediction(esDataFileDTO.getPrediction());
                txtFileVO.setContent(fileQueryCriteria.getContent() == null ? esDataFileDTO.getContent() : highlightContent.toString());
                txtFileVO.setName(esDataFileDTO.getName());
                txtFileVO.setDatasetId(esDataFileDTO.getDatasetId());
                txtFileVO.setStatus(esDataFileDTO.getStatus());
                txtFileVO.setId(Long.parseLong(hits[i].getId()));
                txtFileVO.setLabelIdList(esDataFileDTO.getLabelIdList());
                txtFileVO.setAnnotation(esDataFileDTO.getAnnotation());
                vos.add(txtFileVO);
            }
            page.setTotal(searchResponse.getHits().getTotalHits().value);
        } catch (IOException e) {
            LogUtil.error(LogEnum.BIZ_DATASET, "search text from es error:{}", e);
        }
        return PageUtil.toPage(page, vos);
    }

    /**
     * 文本状态数量统计
     *
     * @param datasetId               数据集ID
     * @param fileScreenStatSearchDTO 文件查询条件
     * @return ProgressVO 文本状态数量统计
     */
    @Override
    public FileScreenStatVO getFileCountByStatus(Long datasetId, FileScreenStatSearchDTO fileScreenStatSearchDTO) {
        Dataset dataset = datasetService.getOneById(datasetId);
        Set<Integer> statusResult = findStatus(Arrays.asList(fileScreenStatSearchDTO.getAnnotationResult()),
                fileScreenStatSearchDTO.getAnnotationStatus(),
                fileScreenStatSearchDTO.getAnnotationMethod());
        Long haveAnnotation = FileTypeEnum.HAVE_ANNOTATION.getValue() == fileScreenStatSearchDTO.getAnnotationResult()
                ? getFileCount(dataset, statusResult, fileScreenStatSearchDTO.getLabelIds())
                : getFileCount(dataset, FileTypeEnum.getStatus(FileTypeEnum.HAVE_ANNOTATION.getValue()), null);
        Long noAnnotation = FileTypeEnum.NO_ANNOTATION.getValue() == fileScreenStatSearchDTO.getAnnotationResult()
                ? getFileCount(dataset, statusResult, fileScreenStatSearchDTO.getLabelIds())
                : getFileCount(dataset, FileTypeEnum.getStatus(FileTypeEnum.NO_ANNOTATION.getValue()), null);
        return FileScreenStatVO.builder().haveAnnotation(haveAnnotation).noAnnotation(noAnnotation).build();
    }

    /**
     * 获取数据集数量
     *
     * @param dataset    数据集
     * @param fileStatus 查询文件状态
     * @return Long              文本文件数量
     */
    private Long getFileCount(Dataset dataset, Set<Integer> fileStatus, List<Long> labelIds) {
        return datasetVersionFileService.getVersionFileCountByStatusVersionAndLabelId(dataset.getId(), fileStatus, dataset.getCurrentVersionName(), labelIds);
    }

    /**
     * 获取文件列表
     *
     * @param datasetVersionFiles 数据集版本文件列表
     * @param datasetId           数据集ID
     * @return List<File> 文件列表
     */
    private List<File> getFileList(List<DatasetVersionFileDTO> datasetVersionFiles, Long datasetId) {
        Set<Long> set = datasetVersionFiles
                .stream()
                .map(DatasetVersionFileDTO::getFileId)
                .collect(Collectors.toSet());
        QueryWrapper queryWrapper = new QueryWrapper<>()
                .in("id", set)
                .eq("dataset_id", datasetId);
        List<File> files = baseMapper.selectList(queryWrapper);

        return files;
    }


    /**
     * 构建文件列表分页
     *
     * @param page          分页条件
     * @param files         文件列表
     * @param dataset       数据集实体
     * @param queryCriteria 查询条件
     * @return ge<File> 分页结果
     */
    private Page<File> buildPages(Page page, List<File> files, Dataset dataset, FileQueryCriteriaVO queryCriteria) {
        Page<File> pages = new Page<>();
        Set<Integer> statusSet = findStatus(queryCriteria.getStatusList(), queryCriteria.getAnnotateStatusList(), queryCriteria.getAnnotateTypeList());
        pages.setTotal(datasetVersionFileService.selectFileListTotalCount(dataset.getId(),
                dataset.getCurrentVersionName(), statusSet, queryCriteria.getLabelIdList()));
        pages.setRecords(files);
        pages.setSize(page.getSize());
        pages.setCurrent(page.getCurrent());
        return pages;
    }

    /**
     * 获取数据集文件数量
     *
     * @param datasetId 数据集ID
     * @return 数据集文件数量
     */
    @Override
    public int getFileCountByDatasetId(Long datasetId) {
        QueryWrapper<File> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(File::getDeleted, 0).eq(File::getDatasetId, datasetId);
        return baseMapper.selectCount(queryWrapper);
    }

    /**
     * 获取数据集原图文件数量
     *
     * @param datasetId   数据集ID
     * @param versionName 版本名称
     * @return 数据集原图文件数量
     */
    @Override
    public int getOriginalFileCountOfDataset(Long datasetId, String versionName) {
        return fileMapper.getOriginalFileCountOfDataset(datasetId, versionName);
    }

    /**
     * 备份数据集文件数据
     *
     * @param originDataset 原数据集实体
     * @param targetDataset 目标数据集实体
     * @return 文件数据
     */
    @Override
    public List<File> backupFileDataByDatasetId(Dataset originDataset, Dataset targetDataset) {
        Long pid = 0L;
        List<File> fileList = null;
        List<File> files = baseMapper.selectList(new LambdaQueryWrapper<File>().eq(File::getDatasetId, originDataset.getId())
                .ne(File::getFileType, MagicNumConstant.ONE).or().isNull(File::getFileType));
        if (!CollectionUtils.isEmpty(files)) {
            Queue<Long> dataFileIds = generatorKeyUtil.getSequenceByBusinessCode(Constant.DATA_FILE, files.size());
            for (int i = 0; i < files.size(); i++) {
                File f = files.get(i);
                f.setId(dataFileIds.poll());
                if (!Objects.isNull(f.getFileType()) && f.getFileType().compareTo(MagicNumConstant.ONE) == 0) {
                    f.setUrl(f.getUrl().replace(originDataset.getId().toString(), targetDataset.getId().toString()));
                    f.setName(FileUtil.spliceFileNameAndDatasetId(targetDataset.getId(), f.getName()));
                    baseMapper.insert(f);
                    pid = f.getId();
                    files.remove(i);
                }
            }
            Long finalPid = pid;
            fileList = files.stream().map(a -> {
                File file = File.builder()
                        .id(a.getId())
                        .fileType(a.getFileType())
                        .datasetId(targetDataset.getId())
                        .enhanceType(a.getEnhanceType())
                        .frameInterval(a.getFrameInterval())
                        .height(a.getHeight())
                        .name(a.getName())
                        .status(a.getStatus())
                        .pid(finalPid)
                        .originUserId(MagicNumConstant.ZERO_LONG)
                        .width(a.getWidth())
                        .url(a.getUrl().replace(originDataset.getId().toString() + SymbolConstant.SLASH
                                , targetDataset.getId().toString() + SymbolConstant.SLASH))
                        .build();
                file.setCreateUserId(targetDataset.getCreateUserId());
                file.setUpdateUserId(file.getCreateUserId());
                file.setDeleted(false);
                return file;
            }).collect(Collectors.toList());
            List<List<File>> splitFiles = CollectionUtil.split(fileList, MagicNumConstant.FOUR_THOUSAND);
            splitFiles.forEach(splitFile -> baseMapper.insertBatch(splitFile));
        }

        return fileList;

    }

    /**
     * 将文本数据同步至ES
     *
     * @param dataset 数据集
     */
    @Override
    public void transportTextToEs(Dataset dataset, List<Long> fileIdsNotToEs, Boolean ifImport) {
        List<EsTransportDTO> esTransportDTOList = fileMapper.selectTextDataNoTransport(dataset.getId(), fileIdsNotToEs, ifImport);
        if (ifImport != null && ifImport) {
            List<TextAnnotationBO> textAnnotationBOS = fileMapper.selectTextAnnotation(dataset.getId(), fileIdsNotToEs);
            Map<Long, List<TextAnnotationBO>> annotationGroup = textAnnotationBOS.stream().collect(Collectors.groupingBy(TextAnnotationBO::getId));
            esTransportDTOList.stream().forEach(esTransportDTO -> {
                List<TextAnnotationBO> annotationsById = annotationGroup.get(esTransportDTO.getId());
                List<Long> labelIds = annotationsById.stream().map(TextAnnotationBO::getLabelId).collect(Collectors.toList());
                esTransportDTO.setLabelId(labelIds.toArray(new Long[labelIds.size()]));
                JSONArray annotations = new JSONArray();
                annotationsById.forEach(annotation -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("category_id", annotation.getLabelId());
                    jsonObject.put("prediction", annotation.getPrediction());
                    annotations.add(jsonObject);
                });
                esTransportDTO.setAnnotation(annotations.toJSONString());
            });
        }
        esTransportDTOList.forEach(esTransportDTO -> {
            FileInputStream fileInputStream = null;
            InputStreamReader reader = null;
            BufferedReader bufferedReader = null;
            try {
                String url = prefixPath + esTransportDTO.getUrl();
                fileInputStream = new FileInputStream(url);
                reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                bufferedReader = new BufferedReader(reader);
                StringBuffer testContent = new StringBuffer();
                String tempContent;
                while ((tempContent = bufferedReader.readLine()) != null) {
                    testContent.append(tempContent);
                }
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("content", testContent.toString());
                jsonMap.put("name", esTransportDTO.getFileName());
                jsonMap.put("status", esTransportDTO.getAnnotationStatus().toString());
                jsonMap.put("datasetId", dataset.getId().toString());
                jsonMap.put("createUserId", esTransportDTO.getCreateUserId() == null ? null : esTransportDTO.getCreateUserId().toString());
                jsonMap.put("createTime", esTransportDTO.getCreateTime().toString());
                jsonMap.put("updateUserId", esTransportDTO.getUpdateUserId() == null ? null : esTransportDTO.getUpdateUserId().toString());
                jsonMap.put("updateTime", esTransportDTO.getUpdateTime().toString());
                jsonMap.put("fileType", esTransportDTO.getFileType() == null ? null : esTransportDTO.getFileType().toString());
                jsonMap.put("enhanceType", esTransportDTO.getEnhanceType() == null ? null : esTransportDTO.getEnhanceType().toString());
                jsonMap.put("originUserId", esTransportDTO.getOriginUserId().toString());
                jsonMap.put("prediction", esTransportDTO.getPrediction() == null ? null : esTransportDTO.getPrediction().toString());
                jsonMap.put("labelId", esTransportDTO.getLabelId() == null ? null : esTransportDTO.getLabelId());
                jsonMap.put("annotation", esTransportDTO.getAnnotation() == null ? null : esTransportDTO.getAnnotation());
                jsonMap.put("versionName", StringUtils.isEmpty(dataset.getCurrentVersionName()) ? "V0000" : dataset.getCurrentVersionName());
                IndexRequest request = new IndexRequest(esIndex);
                request.source(jsonMap);
                request.id(esTransportDTO.getId().toString());
                bulkProcessor.add(request);
                LogUtil.info(LogEnum.BIZ_DATASET, "transport one text to es:{}", esTransportDTO.getUrl());
            } catch (Exception e) {
                LogUtil.error(LogEnum.BIZ_DATASET, "transport text to es error:{}", e);
            } finally {
                try {
                    fileInputStream.close();
                    reader.close();
                    bufferedReader.close();
                } catch (Exception e) {
                    LogUtil.error(LogEnum.BIZ_DATASET, "transport text to es error:{}", e);
                }
            }
        });
        bulkProcessor.flush();
        List<Long> fileIds = new ArrayList<>();
        esTransportDTOList.forEach(esTransportDTO -> fileIds.add(esTransportDTO.getId()));
        fileMapper.updateEsStatus(dataset.getId(), fileIds);
    }

    /**
     * 还原es_transport状态
     *
     * @param datasetId 数据集ID
     * @param fileId    文件ID
     */
    @Override
    public void recoverEsStatus(Long datasetId, Long fileId) {
        fileMapper.recoverEsStatus(datasetId, fileId);
    }

    /**
     * 删除es中数据
     */
    @Override
    public void deleteEsData(List<Long> fileIds) {
        for (Long fileId : fileIds) {
            DeleteRequest deleteRequest = new DeleteRequest(esIndex, fileId.toString());
            deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            try {
                DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
                ReplicationResponse.ShardInfo shardInfo = delete.getShardInfo();
                if (shardInfo.getFailed() > 0) {
                    throw new BusinessException(ErrorEnum.ES_DATA_DELETE_ERROR);
                }
            } catch (IOException e) {
                LogUtil.error(LogEnum.BIZ_DATASET, "delete es data error:{}", e);
                throw new BusinessException(ErrorEnum.ES_DATA_DELETE_ERROR);
            }
        }

    }

    /**
     * 文本数据集csv导入
     * 1.文本地址写入到datafile表
     * 2.生成一条任务数据
     *
     * @param datasetCsvImportDTO 导入信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tableImport(DatasetCsvImportDTO datasetCsvImportDTO) {
        Dataset dataset = datasetService.getOneById(datasetCsvImportDTO.getDatasetId());
        File file = File.builder().build().setName(datasetCsvImportDTO.getFileName())
                .setStatus(FileTypeEnum.UNFINISHED.getValue())
                .setDatasetId(datasetCsvImportDTO.getDatasetId())
                .setUrl(datasetCsvImportDTO.getFilePath())
                .setFileType(LabelGroupTypeEnum.TABLE.getValue())
                .setPid(0L)
                .setOriginUserId(contextService.getCurUserId())
                .setExcludeHeader(datasetCsvImportDTO.getExcludeHeader() == null ? true : datasetCsvImportDTO.getExcludeHeader());
        Queue<Long> dataFileIds = generatorKeyUtil.getSequenceByBusinessCode(Constant.DATA_FILE, 1);
        file.setId(dataFileIds.poll());
        baseMapper.saveList(Arrays.asList(file), contextService.getCurUserId(), dataset.getCreateUserId());
        Task task = Task.builder().build()
                .setDatasetId(datasetCsvImportDTO.getDatasetId())
                .setCreateUserId(contextService.getCurUserId())
                .setLabels("")
                .setMergeColumn(StringUtils.join(datasetCsvImportDTO.getMergeColumn(), ','))
                .setFiles(Strings.join(Arrays.asList(file.getId()), ','))
                .setType(DataTaskTypeEnum.CSV_IMPORT.getValue());
        taskService.createTask(task);
        dataStateMachine.tableImportEvent(datasetCsvImportDTO.getDatasetId());
    }

    /**
     * 获取文件列表
     *
     * @param datasetId 数据集ID
     * @param prefix    匹配前缀
     * @param recursive 是否递归
     * @return List<FileListDTO> 文件列表
     */
    @Override
    public List<FileDTO> fileList(Long datasetId, String prefix, boolean recursive, String versionName, boolean isVersionFile) {
        if (StringUtils.isEmpty(prefix)) {
            if (isVersionFile) {
                if (StringUtils.isEmpty(versionName)) {
                    versionName = datasetService.getOneById(datasetId).getCurrentVersionName();
                }
                prefix = "dataset/" + datasetId + "/versionFile/" + versionName + "/";
            } else {
                prefix = datasetService.getOneById(datasetId).getUri() + "/";
            }
        }
        return minioUtil.fileList(bucketName, prefix, recursive);
    }

    /**
     * 分页获取文件列表
     *
     * @param filePageDTO 文件查询和响应实体
     */
    @Override
    public void filePage(FilePageDTO filePageDTO, Long datasetId) {
        Dataset dataset = datasetService.getOneById(datasetId);
        filePageDTO.setFilePath(prefixPath + bucketName + StrUtil.SLASH + dataset.getUri() + filePageDTO.getFilePath());
        fileStoreApi.filterFilePageWithPath(filePageDTO);
        for (FileDTO fileDto : filePageDTO.getRows()) {
            fileDto.setPath(fileDto.getPath().replaceFirst(filePageDTO.getFilePath(), ""));
        }
    }

    @Override
    public List<FileAnnotationBO> listByDatasetIdAndVersionName(Long datasetId, String versionName) {
        return baseMapper.listByDatasetIdAndVersionName(datasetId, versionName);
    }
}