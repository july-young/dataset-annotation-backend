package org.dubhe.task.execute.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.data.constant.FileTypeEnum;
import org.dubhe.data.domain.bo.TaskSplitBO;
import org.dubhe.data.domain.dto.AnnotationInfoCreateDTO;
import org.dubhe.data.domain.dto.BatchAnnotationInfoCreateDTO;
import org.dubhe.data.service.AnnotationService;
import org.dubhe.data.service.TaskService;
import org.dubhe.task.execute.AbstractAlgorithmExecute;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DependsOn("springContextHolder")
@Component
public class TextClassificationQueueExecute extends AbstractAlgorithmExecute {

    @Autowired
    @Lazy
    private TaskService taskService;

    @Autowired
    private AnnotationService annotationService;

    /**
     * esSearch索引
     */
    @Value("${es.index}")
    private String esIndex;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void finishExecute(JSONObject taskDetail) {
        TaskSplitBO taskSplitBO = JSON.parseObject(JSON.toJSONString(taskDetail), TaskSplitBO.class);
        BatchAnnotationInfoCreateDTO batchAnnotationInfoCreateDTO =annotationService.buildBatchAnnotationInfoCreateDTO(taskDetail);

        Map<Long, AnnotationInfoCreateDTO> res = annotationService.doFinishAuto(taskSplitBO, batchAnnotationInfoCreateDTO.toMap());
        List<AnnotationInfoCreateDTO> list = new ArrayList<>(res.values());
        list.forEach(annotationInfoCreateDTO -> {
            UpdateRequest updateRequest = new UpdateRequest(esIndex,"_doc"
                    ,annotationInfoCreateDTO.getId().toString());
            JSONArray annotations = JSONArray.parseArray(annotationInfoCreateDTO.getAnnotation());
            List<String> labelIds = annotations.stream().map(json -> {
                return JSONObject.parseObject(json.toString()).getString("category_id");
            }).collect(Collectors.toList());
            JSONObject esJsonObject = new JSONObject();
            esJsonObject.put("labelId",labelIds);
            esJsonObject.put("prediction",annotations.getJSONObject(0).getString("score"));
            esJsonObject.put("annotation",annotationInfoCreateDTO.getAnnotation());
            if(annotationInfoCreateDTO.getAnnotation().isEmpty()){
                esJsonObject.put("status", String.valueOf(FileTypeEnum.ANNOTATION_NOT_DISTINGUISH_FILE.getValue()));
            } else {
                esJsonObject.put("status", String.valueOf(FileTypeEnum.AUTO_FINISHED.getValue()));
            }
            updateRequest.doc(esJsonObject, XContentType.JSON);
            try {
                restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                LogUtil.error(LogEnum.BIZ_DATASET, "update es data error:{}", e);
            }
        });
    }

    @Override
    public boolean checkStop(Object object, String queueName, JSONObject taskDetail) {
        Long taskId = taskDetail.getLong("taskId");
        return taskService.isStop(taskId);
    }
}
