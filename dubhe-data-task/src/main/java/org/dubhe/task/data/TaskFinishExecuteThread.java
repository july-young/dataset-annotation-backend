package org.dubhe.task.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.dubhe.biz.base.constant.MagicNumConstant;
import org.dubhe.biz.base.utils.SpringContextHolder;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.biz.redis.utils.RedisUtils;
import org.dubhe.data.util.TaskUtils;
import org.dubhe.task.constant.DataAlgorithmEnum;
import org.dubhe.task.constant.TaskQueueNameEnum;
import org.dubhe.task.execute.AbstractAlgorithmExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class TaskFinishExecuteThread implements Runnable {

    @Autowired
    private TaskUtils taskUtils;

    @Autowired
    private RedisUtils redisUtils;

    private Object object;

    private String detailQueue;

    /**
     * 启动标注任务处理线程
     */
    @PostConstruct
    public void start() {
        Thread thread = new Thread(this, "任务完成任务处理队列");
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                finishExecute(true);
                finishExecute(false);
                TimeUnit.MILLISECONDS.sleep(MagicNumConstant.ONE_THOUSAND);
            } catch (Exception e) {
                LogUtil.error(LogEnum.BIZ_DATASET, "finish algorithm task failed:{}", e);
            }
        }
    }

    private void finishExecute(boolean isSuccess) {
        TaskQueueNameEnum taskQueueNameEnum = isSuccess ? TaskQueueNameEnum.FINISHED_TASK : TaskQueueNameEnum.FAILED_TASK;
        String queueName = TaskQueueNameEnum.getTemplate(taskQueueNameEnum, TaskQueueNameEnum.TaskQueueConfigEnum.ALL);

        JSONObject detail = getDetail(queueName, isSuccess);
        if (detail != null) {
            Integer algorithm = detail.getInteger("algorithm");
            AbstractAlgorithmExecute abstractAlgorithmExecute = (AbstractAlgorithmExecute) SpringContextHolder.getBean(DataAlgorithmEnum.getType(algorithm).getClassName());
            if (isSuccess) {
                abstractAlgorithmExecute.finishMethod(object, detailQueue, detail);
            } else {
                abstractAlgorithmExecute.failMethod(object, detailQueue, detail);
            }
        }
    }

    private JSONObject getDetail(String queueName, boolean isSuccess) {
        object = isSuccess ? taskUtils.getFinishedTask(queueName) : taskUtils.getFailedTask(queueName);

        if (object != null) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(redisUtils.get(object.toString())));
            //获取详情
            String objectString = object.toString();
            StringBuffer sb = new StringBuffer(objectString);
            detailQueue = sb.replace(objectString.lastIndexOf("annotation")
                    , objectString.lastIndexOf("annotation") + "annotation".length(), "detail").toString();
            JSONObject taskDetail = JSON.parseObject(JSON.toJSONString(redisUtils.get(detailQueue)));
            if (taskDetail == null) {
                redisUtils.del(object.toString());
                return null;
            }
            taskDetail.put("object", jsonObject);
            return taskDetail;
        }
        return null;
    }
}
