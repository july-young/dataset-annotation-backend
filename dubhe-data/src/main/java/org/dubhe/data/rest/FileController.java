package org.dubhe.data.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dubhe.biz.base.constant.Permissions;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.db.utils.PageDTO;
import org.dubhe.biz.db.utils.PageUtil;
import org.dubhe.biz.file.dto.FilePageDTO;
import org.dubhe.biz.file.dto.MinioDownloadDTO;
import org.dubhe.biz.file.utils.MinioUtil;
import org.dubhe.biz.file.utils.MinioWebTokenBody;
import org.dubhe.data.constant.Constant;
import org.dubhe.data.domain.dto.BatchFileCreateDTO;
import org.dubhe.data.domain.dto.DatasetCsvImportDTO;
import org.dubhe.data.domain.dto.FileDeleteDTO;
import org.dubhe.data.domain.dto.FileScreenStatSearchDTO;
import org.dubhe.data.domain.entity.File;
import org.dubhe.data.domain.vo.FileQueryCriteriaVO;
import org.dubhe.data.domain.vo.FileVO;
import org.dubhe.data.domain.vo.TxtFileVO;
import org.dubhe.data.service.DatasetService;
import org.dubhe.data.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


/**
 * @description 文件管理
 * @date 2020-04-10
 */
@Api(tags = "数据处理：文件管理")
@RestController
@RequestMapping(Constant.MODULE_URL_PREFIX + "/datasets")
public class FileController {

    /**
     * 文件服务实现类
     */
    @Autowired
    private FileService fileService;

    /**
     * 数据集服务实现类
     */
    @Autowired
    private DatasetService datasetService;

    /**
     * minIO端操作
     */
    @Autowired
    private MinioWebTokenBody minioWebTokenBody;

    /**
     * minIO桶名
     */
    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 失效时间
     */
    @Value("${minio.presignedUrlExpiryTime}")
    private Integer expiry;

    /**
     * minIO工具类
     */
    @Autowired
    private MinioUtil minioUtil;

    @ApiOperation(value = "文件提交")
    @PostMapping(value = "/{datasetId}/files")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody upload(@PathVariable(name = "datasetId") Long datasetId, @Validated @RequestBody BatchFileCreateDTO batchFileCreateDTO) {
        datasetService.uploadFiles(datasetId, batchFileCreateDTO);
        return new DataResponseBody();
    }

    @ApiOperation(value = "视频提交")
    @PostMapping(value = "/{datasetId}/video")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody uploadVideo(@PathVariable(name = "datasetId") Long datasetId, @Validated @RequestBody BatchFileCreateDTO batchFileCreateDTO) {
        datasetService.uploadVideo(datasetId, batchFileCreateDTO);
        return new DataResponseBody();
    }

    @ApiOperation(value = "文件详情", notes = "状态: 101-未标注, 102-手动标注中, 103-自动标注完成, 104-标注完成, 105-标注未识别, 201-目标跟踪完成")
    @GetMapping(value = "/files/{datasetId}/{fileId}/info")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<FileVO> get(@PathVariable(name = "fileId") Long fileId, @PathVariable(name = "datasetId") Long datasetId) {
        FileVO fileVO = fileService.get(fileId, datasetId);
        return new DataResponseBody(fileVO);
    }

    @ApiOperation(value = "文件查询", notes = "状态: 101-未标注, 102-手动标注中, 103-自动标注完成, 104-标注完成, 105-标注未识别, 201-目标跟踪完成")
    @GetMapping(value = "/{datasetId}/files")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<FileVO> query(@PathVariable(name = "datasetId") Long datasetId, Page page, FileQueryCriteriaVO fileQueryCriteriaVO) {
        PageDTO<FileVO> pageDTO = fileService.listPage(datasetId, page, fileQueryCriteriaVO);
        return new DataResponseBody(pageDTO);
    }


    @ApiOperation(value = "音频数据集文件查询", notes = "状态: 101-未标注, 102-手动标注中, 103-自动标注完成, 104-标注完成, 105-标注未识别")
    @GetMapping(value = "/{datasetId}/files/audio")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<TxtFileVO> audioFilesByPage(@PathVariable(name = "datasetId") Long datasetId, Page page, FileQueryCriteriaVO fileQueryCriteria) {
        PageDTO<TxtFileVO> pageDTO = fileService.audioFilesByPage(datasetId, page, fileQueryCriteria);
        return new DataResponseBody(pageDTO);
    }

    @ApiOperation(value = "文本数据集内容查询", notes = "状态: 101-未标注, 102-手动标注中, 103-自动标注完成, 104-标注完成, 105-标注未识别")
    @GetMapping(value = "/{datasetId}/files/content")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<TxtFileVO> txtContentByPage(@PathVariable(name = "datasetId") Long datasetId, Page page, FileQueryCriteriaVO fileQueryCriteria) {
        PageDTO<TxtFileVO> pageDTO = fileService.txtContentByPage(datasetId, page, fileQueryCriteria);
        return new DataResponseBody(pageDTO);
    }

    @ApiOperation(value = "文件查询，物体检测标注页面使用", notes = "状态: 101-未标注, 102-手动标注中, 103-自动标注完成, 104-标注完成, 105-标注未识别, 201-目标跟踪完成")
    @GetMapping(value = "/{datasetId}/files/detection")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<PageDTO<File>> query(@PathVariable(name = "datasetId") Long datasetId,
                                                 @RequestParam(required = false) Long offset,
                                                 @RequestParam(required = false) Integer limit,
                                                 @RequestParam(required = false) Integer[] type,
                                                 @RequestParam(required = false) Integer page,
                                                 @RequestParam(required = false) Long[] labelId) {
        Set<Integer> typeSet = type == null ? new HashSet() : Sets.newHashSet(type);
        List<Long> labelIdList = labelId == null ? new ArrayList() : Arrays.asList(labelId);
        PageDTO<File> pageDTO = fileService.listByLimit(datasetId, offset, limit, page, typeSet, labelIdList);
        return new DataResponseBody(pageDTO);
    }

    @ApiOperation(value = "获取文件的offset，物体检测标注页面使用")
    @GetMapping(value = "/{datasetId}/files/{fileId}/offset")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<Integer> getOffset(@PathVariable(name = "fileId") Long fileId,
                                               @RequestParam(required = false) Integer[] type,
                                               @RequestParam(required = false) Long[] labelId,
                                               @PathVariable(name = "datasetId") Long datasetId) {
        Integer offset = fileService.getOffset(fileId, datasetId, type, labelId);
        return new DataResponseBody(offset);
    }

    @ApiOperation(value = "获取当前数据集的第一个文件id，物体检测标注页面使用")
    @GetMapping(value = "/{datasetId}/files/first")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<Long> getFirstId(@PathVariable(name = "datasetId") Long datasetId,
                                             @RequestParam(required = false) Integer type) {
        Long first = fileService.getFirst(datasetId, type);
        return new DataResponseBody(first);
    }

    @ApiOperation(value = "文件删除", notes = "删除文件或数据集下的所有文件,不删除dataset.数据集正在自动标注中的文件不允许删除")
    @DeleteMapping(value = "/files")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody delete(@Validated @RequestBody FileDeleteDTO fileDeleteDTO) {
        datasetService.delete(fileDeleteDTO.getDatasetIds());
        fileService.delete(fileDeleteDTO.getFileIds());
        return new DataResponseBody();
    }

    @ApiOperation("MinIO下载压缩包参数")
    @GetMapping(value = "/zip")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<MinioDownloadDTO> downloadFile(@RequestParam String prefix, @RequestParam List<String> objects, @RequestParam String zipName) {
        MinioDownloadDTO minioDownloadDTO = minioWebTokenBody.getDownloadParam(bucketName, prefix, objects, zipName);
        return new DataResponseBody(minioDownloadDTO);
    }

    @ApiOperation("MinIO生成put请求的上传路径")
    @GetMapping(value = "/minio/url/put")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<String> getEncryptedPutUrl(@RequestParam String objectName) {
        String encryptedPutUrl = minioUtil.getEncryptedPutUrl(bucketName, objectName, expiry);
        return new DataResponseBody(encryptedPutUrl);
    }

    @ApiOperation("MinIO生成put请求的上传路径列表")
    @PostMapping(value = "/minio/getUrls")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody getEncryptedPutUrls(@RequestBody String objectNames) {
        return new DataResponseBody(minioUtil.getEncryptedPutUrls(bucketName, objectNames, expiry));
    }


    @ApiOperation("获取文件对应增强文件列表")
    @GetMapping(value = "/{datasetId}/{fileId}/enhanceFileList")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<List<File>> getEnhanceFileList(@PathVariable(value = "fileId") Long fileId, @PathVariable(value = "datasetId") Long datasetId) {
        List<File> enhanceFileList = fileService.getEnhanceFileList(fileId, datasetId);
        return new DataResponseBody(enhanceFileList);
    }

    @ApiOperation("文本状态数量统计")
    @GetMapping(value = "/{datasetId}/count")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody getFileCountByStatus(@PathVariable(value = "datasetId") Long datasetId, @Validated FileScreenStatSearchDTO fileScreenStatSearchDTO) {
        return new DataResponseBody(fileService.getFileCountByStatus(datasetId, fileScreenStatSearchDTO));
    }

    @ApiOperation("文本数据集csv/xlsx导入")
    @PostMapping(value = "/tableImport")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody tableImport(@RequestBody DatasetCsvImportDTO datasetCsvImportDTO) {
        fileService.tableImport(datasetCsvImportDTO);
        return new DataResponseBody();
    }

    @ApiOperation("根据指定前缀获取文件/文件夹列表, 支持递归以及非递归模式(默认递归模式)")
    @GetMapping(value = "/{datasetId}/files/fileList")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody fileList(@PathVariable(value = "datasetId") Long datasetId, @RequestParam(required = false) String prefix,
                                     @RequestParam(defaultValue = "false", required = false) boolean recursive,
                                     @RequestParam(required = false) String versionName,
                                     @RequestParam(required = false) boolean isVersionFile) {
        return new DataResponseBody(fileService.fileList(datasetId, prefix, recursive, versionName, isVersionFile));
    }

    @ApiOperation("分页获取文件列表")
    @PostMapping(value = "/{datasetId}/files/filePage")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody filePage(@PathVariable(value = "datasetId") Long datasetId, @RequestBody FilePageDTO filePageDTO) {
        fileService.filePage(filePageDTO, datasetId);
        return new DataResponseBody(filePageDTO);
    }

}
