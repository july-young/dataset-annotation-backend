package org.dubhe.data.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dubhe.biz.base.constant.Permissions;
import org.dubhe.biz.base.dto.DeleteDTO;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.base.vo.LabelGroupBaseVO;
import org.dubhe.biz.db.utils.PageDTO;
import org.dubhe.data.constant.Constant;
import org.dubhe.data.domain.dto.GroupConvertPresetDTO;
import org.dubhe.data.domain.dto.LabelGroupCopyDTO;
import org.dubhe.data.domain.dto.LabelGroupCreateDTO;
import org.dubhe.data.domain.dto.LabelGroupDeleteDTO;
import org.dubhe.data.domain.dto.LabelGroupImportDTO;
import org.dubhe.data.domain.dto.LabelGroupQueryDTO;
import org.dubhe.data.domain.entity.LabelGroup;
import org.dubhe.data.domain.vo.LabelGroupQueryVO;
import org.dubhe.data.domain.vo.LabelGroupVO;
import org.dubhe.data.service.LabelGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;


@Api(tags = "数据处理：标签组管理")
@RestController
@RequestMapping(Constant.MODULE_URL_PREFIX)
public class LabelGroupController {

    @Autowired
    private LabelGroupService labelGroupService;

    @ApiOperation(value = "标签组创建")
    @PostMapping(value = "/labelGroup")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody create(@Validated @RequestBody LabelGroupCreateDTO labelGroupCreateDTO) {
        Long labelGroupId = labelGroupService.creatLabelGroup(labelGroupCreateDTO);
        return new DataResponseBody(labelGroupId);
    }

    @ApiOperation(value = "标签组分页列表")
    @GetMapping(value = "/labelGroup/query")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<LabelGroupQueryVO> query(Page page, LabelGroupQueryVO labelGroupQueryVO) {
        PageDTO<LabelGroupQueryVO> pageDTO = labelGroupService.page(page, labelGroupQueryVO);
        return new DataResponseBody(pageDTO);
    }

    @ApiOperation(value = "标签组详情")
    @GetMapping(value = "/labelGroup/{labelGroupId}")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<LabelGroupVO> get(@PathVariable(name = "labelGroupId") Long labelGroupId) {
        LabelGroupVO labelGroupVO = labelGroupService.get(labelGroupId);
        return new DataResponseBody(labelGroupVO);
    }

    @ApiOperation(value = "标签组列表")
    @GetMapping(value = "/labelGroup/getList")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<List<LabelGroup>> query(@Validated LabelGroupQueryDTO labelGroupQueryDTO) {
        List<LabelGroup> list = labelGroupService.getList(labelGroupQueryDTO);
        return new DataResponseBody(list);

    }

    @ApiOperation(value = "标签组编辑")
    @PutMapping(value = "/labelGroup/{labelGroupId}")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody update(@PathVariable(name = "labelGroupId") Long labelGroupId, @Validated @RequestBody LabelGroupCreateDTO labelGroupCreateDTO) {
        labelGroupService.update(labelGroupId, labelGroupCreateDTO);
        return new DataResponseBody();
    }

    @ApiOperation(value = "标签组删除", notes = "删除标签组及标签组下的标签")
    @DeleteMapping(value = "/labelGroup")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody delete(@Validated @RequestBody DeleteDTO deleteDTO) {
        labelGroupService.delete(deleteDTO);
        return new DataResponseBody();
    }

    @ApiOperation(value = "标签组导入")
    @PostMapping(value = "/labelGroup/import")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody importLabelGroup(@RequestParam(value = "file", required = false) MultipartFile file,
                                             LabelGroupImportDTO labelGroupImportDTO) {
        Long labelGroupId = labelGroupService.importLabelGroup(labelGroupImportDTO, file);
        return new DataResponseBody(labelGroupId);
    }


    @ApiOperation(value = "标签组复制")
    @PostMapping(value = "/labelGroup/copy")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody copy(@Validated @RequestBody LabelGroupCopyDTO labelGroupCopyDTO) {
        labelGroupService.copy(labelGroupCopyDTO);
        return new DataResponseBody();
    }


    @ApiOperation(value = "普通标签组转预置")
    @PostMapping(value = "labelGroup/convertPreset")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody convertPreset(@RequestBody GroupConvertPresetDTO groupConvertPresetDTO) {
        labelGroupService.convertPreset(groupConvertPresetDTO);
        return new DataResponseBody();
    }

    @ApiOperation(value = "标签组集合详情(远程调用)")
    @PostMapping(value = "/labelGroup/queryLabelGroupList")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<List<LabelGroupBaseVO>> queryLabelGroupList(@RequestBody Set<Long> labelGroupIds) {
        List<LabelGroupBaseVO> labelGroupBaseVOS = labelGroupService.queryLabelGroupList(labelGroupIds);
        return new DataResponseBody(labelGroupBaseVOS);
    }

}
