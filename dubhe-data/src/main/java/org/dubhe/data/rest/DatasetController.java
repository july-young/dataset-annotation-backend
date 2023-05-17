package org.dubhe.data.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dubhe.biz.base.constant.Permissions;
import org.dubhe.biz.base.dto.DeleteDTO;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.base.vo.DatasetVO;
import org.dubhe.biz.base.vo.ProgressVO;
import org.dubhe.biz.db.utils.PageDTO;
import org.dubhe.data.constant.Constant;
import org.dubhe.data.domain.dto.*;
import org.dubhe.data.domain.entity.Dataset;
import org.dubhe.data.domain.vo.DatasetCountVO;
import org.dubhe.data.domain.vo.DatasetQueryDTO;
import org.dubhe.data.domain.vo.IsImportVO;
import org.dubhe.data.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * @description 数据集管理
 * @date 2020-04-10
 */
@Api(tags = "数据处理：数据集管理")
@RestController
@RequestMapping(Constant.MODULE_URL_PREFIX + "/datasets")
public class DatasetController {

    @Autowired
    private DatasetService datasetService;

    @ApiOperation(value = "数据集创建")
    @PostMapping
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody create(@Validated(DatasetCreateDTO.Create.class) @RequestBody DatasetCreateDTO datasetCreateDTO) {
        Long id = datasetService.create(datasetCreateDTO);
        return new DataResponseBody(id);
    }

    @ApiOperation(value = "数据集查询")
    @GetMapping
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<PageDTO<DatasetVO>> page(Page page, DatasetQueryDTO datasetQueryDTO) {
        PageDTO<DatasetVO> pageDTO = datasetService.page(page, datasetQueryDTO);
        return new DataResponseBody(pageDTO);
    }

    @ApiOperation(value = "数据集详情")
    @GetMapping(value = "/{datasetId}")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<DatasetVO> get(@PathVariable(name = "datasetId") Long datasetId) {
        DatasetVO datasetVO = datasetService.get(datasetId);
        return new DataResponseBody(datasetVO);
    }

    @ApiOperation(value = "数据集进度")
    @GetMapping(value = "/progress")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<Map<Long, ProgressVO>> progress(@RequestParam List<Long> datasetIds) {
        Map<Long, ProgressVO> progressVOMap = datasetService.progress(datasetIds);
        return new DataResponseBody(progressVOMap);
    }

    @ApiOperation(value = "数据集修改")
    @PutMapping(value = "/{datasetId}")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody update(@PathVariable(name = "datasetId") Long datasetId,
                                   @Validated @RequestBody DatasetCreateDTO datasetCreateDTO) {
        boolean update = datasetService.update(datasetCreateDTO, datasetId);
        return new DataResponseBody(update);
    }

    @ApiOperation(value = "数据集删除", notes = "数据集下的文件会同时被删除")
    @DeleteMapping
    @PreAuthorize(Permissions.DATA_DELETE)
    public DataResponseBody delete(@Validated @RequestBody DeleteDTO deleteDTO) {
        datasetService.delete(deleteDTO.getIds());
        return new DataResponseBody();
    }

    @ApiOperation(value = "数据集下载", notes = "压缩数据集并下载")
    @GetMapping(value = "/{datasetId}/download")
    @PreAuthorize(Permissions.DATA)
    public void download(@PathVariable(name = "datasetId") Long datasetId, HttpServletResponse httpServletResponse) {
        datasetService.download(datasetId, httpServletResponse);
    }

    @ApiOperation(value = "数据集查询(有版本)")
    @GetMapping(value = "/versions/filter")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<PageDTO<Dataset>> queryConfirmDatasetVersion(Page page, DatasetIsVersionDTO datasetIsVersionDTO) {
        PageDTO<Dataset>  pageDTO = datasetService.dataVersionListVO(page, datasetIsVersionDTO);
        return new DataResponseBody(pageDTO);
    }

    @ApiOperation(value = "数据集增强")
    @PostMapping(value = "/enhance")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody enhance(@Validated @RequestBody DatasetEnhanceRequestDTO datasetEnhanceRequestDTO) {
        datasetService.enhance(datasetEnhanceRequestDTO);
        return new DataResponseBody();
    }

    @ApiOperation(value = "查询公共和个人数据集的数量")
    @GetMapping(value = "/count")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody queryDatasetsCount() {
        DatasetCountVO datasetCountVO = datasetService.queryDatasetsCount();
        return new DataResponseBody(datasetCountVO);
    }

    @ApiOperation(value = "查询数据集状态")
    @GetMapping(value = "/status")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody determineIfTheDatasetIsAnImport(@RequestParam List<Long> datasetIds) {
        Map<Long, IsImportVO> longIsImportVOMap = datasetService.determineIfTheDatasetIsAnImport(datasetIds);
        return new DataResponseBody(longIsImportVOMap);
    }

    @ApiOperation(value = "导入用户自定义数据集")
    @PostMapping(value = "/custom")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody importDataset(@RequestBody DatasetCustomCreateDTO datasetCustomCreateDTO) {
        Long datasetId = datasetService.importDataset(datasetCustomCreateDTO);
        return new DataResponseBody(datasetId);
    }

    @ApiOperation(value = "数据集置顶")
    @GetMapping(value = "/{datasetId}/top")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody topDataset(@PathVariable(name = "datasetId") Long datasetId) {
        datasetService.topDataset(datasetId);
        return new DataResponseBody();
    }

    @ApiOperation(value = "普通数据集转预置")
    @PostMapping(value = "/convertPreset")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody convertPreset(@RequestBody DatasetConvertPresetDTO datasetConvertPresetDTO) {
        datasetService.convertPreset(datasetConvertPresetDTO);
        return new DataResponseBody();
    }


    @ApiOperation(value = "根据数据集ID查询数据集是否转换信息")
    @GetMapping(value = "/getConvertInfoByDatasetId")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody getConvertInfoByDatasetId(@RequestParam(value = "datasetId") Long datasetId) {
        Boolean converted = datasetService.getConvertInfoByDatasetId(datasetId);
        return new DataResponseBody(converted);
    }

    @ApiOperation("获取预置数据集列表")
    @GetMapping(value = "/getPresetDataset")
    public DataResponseBody getPresetDataset() {
        datasetService.getPresetDataset();
        return new DataResponseBody();
    }

    @ApiOperation(value = "任务停止")
    @PutMapping(value = "/task/{datasetId}/stop")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody taskStop(@PathVariable(name = "datasetId") Long datasetId) {
        datasetService.taskStop(datasetId);
        return new DataResponseBody();
    }

    @ApiOperation(value = "ofRecord停止")
    @PutMapping(value = "/ofRecord/{datasetId}/stop")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody ofRecordStop(@PathVariable(name = "datasetId") Long datasetId,@RequestParam(name="version")String version) {
        datasetService.ofRecordStop(datasetId, version);
        return new DataResponseBody();
    }

    @ApiOperation("获取指定名称预置数据集(远程调用)")
    @GetMapping(value = "/getPresetDatasetByName")
    public DataResponseBody<DatasetVO> getPresetDatasetByName(@RequestParam String datasetName) {
        DatasetVO datasetVO = datasetService.getPresetDatasetByName(datasetName);
        return new DataResponseBody(datasetVO);
    }
}
