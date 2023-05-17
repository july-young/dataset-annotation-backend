package org.dubhe.data.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dubhe.biz.base.constant.Permissions;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.data.constant.Constant;
import org.dubhe.data.constant.DatasetLabelEnum;
import org.dubhe.data.domain.dto.LabelDTO;
import org.dubhe.data.domain.dto.LabelDeleteDTO;
import org.dubhe.data.domain.dto.LabelUpdateDTO;
import org.dubhe.data.domain.entity.Label;
import org.dubhe.data.service.DatasetService;
import org.dubhe.data.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "数据处理：标签管理")
@RestController
@RequestMapping(Constant.MODULE_URL_PREFIX + "/datasets")
public class LabelController {

    @Autowired
    private LabelService labelService;
    @Autowired
    private DatasetService datasetService;

    @ApiOperation(value = "标签创建")
    @PostMapping(value = "/{datasetId}/labels")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody create(@RequestBody Label label, @PathVariable(name = "datasetId") Long datasetId) {
        datasetService.saveLabel(label, datasetId);
        return new DataResponseBody();
    }

    @ApiOperation(value = "标签查询")
    @GetMapping(value = "/{datasetId}/labels")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<List<LabelDTO>> query(@PathVariable(name = "datasetId") Long datasetId) {
        List<LabelDTO> labelDTOList = labelService.list(datasetId);
        return new DataResponseBody(labelDTOList);
    }

    @ApiOperation(value = "根据类型获取预置标签集合")
    @GetMapping(value = "/labels/auto/{labelGroupType}")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<List<Label>> listSupportAutoByType(@PathVariable(value = "labelGroupType") Integer labelGroupType) {
        List<Label> labelList = labelService.listSupportAutoByType(labelGroupType);
        return new DataResponseBody(labelList);
    }

    @ApiOperation(value = "获取预置标签类型")
    @GetMapping(value = "/presetLabels")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody getPresetLabels() {
        return new DataResponseBody(DatasetLabelEnum.getPresetLabels());
    }

    @ApiOperation(value = "获取coco预置标签")
    @GetMapping(value = "/pubLabels")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody getPubLabels(Integer labelGroupType) {
        List<Label> labelList = labelService.getPubLabels(labelGroupType);
        return new DataResponseBody(labelList);
    }

    @ApiOperation(value = "根据标签组类型获取标签数据")
    @GetMapping(value = "/labels/{labelGroupType}")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody query(@PathVariable(name = "labelGroupType") Integer labelGroupType) {
        List<Label> labelList = labelService.findByLabelGroupType(labelGroupType);
        return new DataResponseBody(labelList);
    }

    @ApiOperation(value = "删除标签")
    @DeleteMapping(value = "/labels")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody delete(@Validated @RequestBody LabelDeleteDTO labelDeleteDTO) {
        labelService.delete(labelDeleteDTO);
        return new DataResponseBody();
    }

    @ApiOperation(value = "标签修改")
    @PutMapping(value = "/labels")
    @PreAuthorize(Permissions.DATA)
    public DataResponseBody<Boolean> update(@Validated @RequestBody LabelUpdateDTO labelUpdateDTO) {
        boolean update = labelService.update(labelUpdateDTO);
        return new DataResponseBody(update);
    }
}
