package org.dubhe.admin.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dubhe.admin.service.RecycleTaskService;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.dataresponse.factory.DataResponseFactory;
import org.dubhe.recycle.domain.dto.RecycleTaskDeleteDTO;
import org.dubhe.recycle.domain.dto.RecycleTaskQueryDTO;
import org.dubhe.recycle.enums.RecycleModuleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "系统:回收任务")
@RestController
@RequestMapping("/recycleTask")
public class RecycleTaskController {

    @Autowired
    private RecycleTaskService recycleTaskService;

    @ApiOperation("查询回收任务列表")
    @GetMapping
    public DataResponseBody getRecycleTaskList(RecycleTaskQueryDTO recycleTaskQueryDTO) {
        return DataResponseFactory.success(recycleTaskService.getRecycleTasks(recycleTaskQueryDTO));
    }

    @ApiOperation("（批量）立即删除")
    @DeleteMapping
    public DataResponseBody recycleTaskResources(@Validated @RequestBody RecycleTaskDeleteDTO recycleTaskDeleteDTO) {
        for (long taskId:recycleTaskDeleteDTO.getRecycleTaskIdList()){
            recycleTaskService.recycleTaskResources(taskId);
        }
        return DataResponseFactory.successWithMsg("资源删除中");
    }

    @ApiOperation("获取模块代号，名称映射")
    @GetMapping("/recycleModuleMap")
    public DataResponseBody recycleModuleMap() {
        return DataResponseFactory.success(RecycleModuleEnum.RECYCLE_MODULE_MAP);
    }

    @ApiOperation("立即还原")
    @PutMapping
    public DataResponseBody restore(@RequestParam(required = true) long taskId) {
        recycleTaskService.restore(taskId);
        return DataResponseFactory.successWithMsg("还原成功");
    }


    @ApiOperation("实时删除完整路径无效文件")
    @DeleteMapping("/delTemp")
    public DataResponseBody delTempInvalidResources(@RequestParam(required = true) String sourcePath) {
        recycleTaskService.delTempInvalidResources(sourcePath);
        return DataResponseFactory.successWithMsg("删除临时目录文件成功");
    }

}
