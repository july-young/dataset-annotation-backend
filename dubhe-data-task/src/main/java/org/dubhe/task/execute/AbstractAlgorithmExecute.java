
package org.dubhe.task.execute;

import com.alibaba.fastjson.JSONObject;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.biz.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractAlgorithmExecute {

    @Autowired
    private RedisUtils redisUtils;

    public final void finishMethod(Object object, String queueName,JSONObject taskDetail){
        try{
            if(!checkStop(object, queueName,taskDetail)){
                finishExecute(taskDetail);
            }
            deleteRedisKey(object,queueName);
        } catch (Exception e){
            LogUtil.error(LogEnum.BIZ_DATASET, "execute finish task failed:{}", e);
        }
    }

    public final void failMethod(Object object, String queueName,JSONObject failDetail){
        try {
            if(!checkStop(object, queueName, failDetail)){
                failExecute(failDetail);
            }
            deleteRedisKey(object,queueName);
        } catch (Exception e){
            LogUtil.error(LogEnum.BIZ_DATASET, "execute failed task failed:{}", e);
        }
    }

    public abstract void finishExecute(JSONObject taskDetail);

    public void failExecute(JSONObject failDetail){
    }

    public boolean checkStop(Object object, String queueName,JSONObject taskDetail){
        return false;
    }

    public void deleteRedisKey(Object object,String detailQueue) throws Exception{
        redisUtils.del(object.toString());
        redisUtils.del(detailQueue);
    }
}
