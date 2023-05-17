package org.dubhe.admin.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dubhe.admin.service.MinIoService;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "系统：minio服务")
@RestController
@RequestMapping("/minio")
public class MinIoController {

    @Autowired
    private MinIoService minIoService;

    @ApiOperation("获取MinIO相关信息")
    @GetMapping(value = "/info")
    public DataResponseBody<Map<String, String>> getMinIOInfo() {
        Map<String, String> minIOInfo = minIoService.getMinIOInfo();
        return new DataResponseBody(minIOInfo);
    }

}
