package org.dubhe.admin.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dubhe.biz.base.dto.DeleteDTO;
import org.dubhe.admin.domain.dto.DictDetailCreateDTO;
import org.dubhe.admin.domain.dto.DictDetailQueryDTO;
import org.dubhe.admin.domain.dto.DictDetailUpdateDTO;
import org.dubhe.admin.service.DictDetailService;
import org.dubhe.biz.base.constant.Permissions;
import org.dubhe.biz.base.dto.DictDetailDTO;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @description 字典详情管理 控制器
 */
@Api(tags = "系统：字典详情管理")
@RestController
@RequestMapping("/dictDetail")
public class DictDetailController {

    @Autowired
    private DictDetailService dictDetailService;

    @ApiOperation("分页查询字典详情")
    @GetMapping
    @PreAuthorize(Permissions.DICT)
    public DataResponseBody page(DictDetailQueryDTO resources, Page page) {
        return new DataResponseBody(dictDetailService.page(resources, page));
    }


    @ApiOperation("新增字典详情")
    @PostMapping
    @PreAuthorize(Permissions.DICT_DETAIL_CREATE)
    public DataResponseBody create(@Valid @RequestBody DictDetailCreateDTO createDTO) {
        return new DataResponseBody(dictDetailService.create(createDTO));
    }


    @ApiOperation("修改字典详情")
    @PutMapping
    @PreAuthorize(Permissions.DICT_DETAIL_EDIT)
    public DataResponseBody update(@Valid @RequestBody DictDetailUpdateDTO updateDTO) {
        dictDetailService.update(updateDTO);
        return new DataResponseBody();
    }

    @ApiOperation("删除字典详情")
    @DeleteMapping
    @PreAuthorize(Permissions.DICT_DETAIL_DELETE)
    public DataResponseBody delete(@Valid @RequestBody DeleteDTO deleteDTO) {
        dictDetailService.delete(deleteDTO.getIds());
        return new DataResponseBody();
    }

    @ApiOperation("根据名称查询字典详情")
    @GetMapping("/getDictDetails")
    public DataResponseBody<List<DictDetailDTO>> getDictDetailByLabel( String name ) {
        List<DictDetailDTO> dictDetailVOList = dictDetailService.getDictDetailByLabel(name);
        return new DataResponseBody(dictDetailVOList);
    }

}
