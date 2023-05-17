package org.dubhe.admin.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.service.DictService;
import org.dubhe.biz.base.constant.Permissions;
import org.dubhe.biz.base.dto.DeleteDTO;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.dataresponse.factory.DataResponseFactory;
import org.dubhe.biz.db.utils.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @description 字典管理 控制器
 */
@Api(tags = "系统：字典管理")
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService dictService;


    @ApiOperation("导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize(Permissions.DICT_DOWNLOAD)
    public void download(HttpServletResponse response, DictQueryDTO criteria) throws IOException {
        dictService.download(dictService.list(criteria), response);
    }

    @ApiOperation("查询字典")
    @GetMapping(value = "/all")
    @PreAuthorize(Permissions.DICT)
    public DataResponseBody<List<DictDTO>> all() {
        List<DictDTO> dictDTOS = dictService.list(new DictQueryDTO());
        return new DataResponseBody(dictDTOS);
    }

    @ApiOperation("查询字典")
    @GetMapping
    @PreAuthorize(Permissions.DICT)
    public DataResponseBody<PageDTO<DictDTO>> page(DictQueryDTO resources, Page page) {
        PageDTO<DictDTO> pageDTO = dictService.page(resources, page);
        return new DataResponseBody(pageDTO);
    }

    @ApiOperation("新增字典")
    @PostMapping
    @PreAuthorize(Permissions.DICT_CREATE)
    public DataResponseBody create(@Valid @RequestBody DictCreateDTO resources) {
        return new DataResponseBody(dictService.create(resources));
    }

    @ApiOperation("修改字典")
    @PutMapping
    @PreAuthorize(Permissions.DICT_EDIT)
    public DataResponseBody update(@Valid @RequestBody DictUpdateDTO resources) {
        dictService.update(resources);
        return new DataResponseBody();
    }

    @ApiOperation("批量删除字典")
    @DeleteMapping
    @PreAuthorize(Permissions.DICT_DELETE)
    public DataResponseBody delete(@RequestBody DeleteDTO dto) {
        dictService.deleteAll(dto.getIds());
        return new DataResponseBody();
    }

    @ApiOperation("根据名称查询字典详情")
    @GetMapping(value = "/{name}")
    public DataResponseBody getDict(@PathVariable String name) {
        return DataResponseFactory.success(dictService.findByName(name));
    }
}
