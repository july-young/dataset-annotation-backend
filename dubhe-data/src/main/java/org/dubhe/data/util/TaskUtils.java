

package org.dubhe.data.util;

import org.dubhe.biz.base.constant.MagicNumConstant;
import org.dubhe.biz.base.utils.StringUtils;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

/**
 * @description 任务处理工具类

 */
@Component
public class TaskUtils {

    @Autowired
    private RedisTemplate  redisTemplate;

    @Autowired
    private StringRedisTemplate  stringRedisTemplate;
    /**
     * 获取zset中的数据和时间
     *
     * @param key redis中key
     * @return Set<ZSetOperations.TypedTuple < Object>> 返回zset中数据
     */
    public Set<ZSetOperations.TypedTuple<Object>> zGetWithScore(String key) {
        try {
            return redisTemplate.opsForZSet().rangeWithScores(key, Long.MIN_VALUE, Long.MAX_VALUE);
        } catch (Exception e) {
            LogUtil.error(LogEnum.BIZ_DATASET, "RedisUtils rangeWithScores key {} error:{}", key, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 添加任务到redis
     *
     * @param queueName    任务队列名称
     * @param taskDetails  任务详情
     * @param score        分数
     * @return 任务是否存放成功
     */
    public Boolean addTask(String queueName, String taskDetails, String detailKey,int score) {
        DefaultRedisScript<Boolean> addTaskScript = new DefaultRedisScript<>();
        addTaskScript.setResultType(Boolean.class);
        addTaskScript.setLocation(new ClassPathResource("addTask.lua"));
        try {
            return stringRedisTemplate.execute(addTaskScript, Collections.singletonList(detailKey)
                    , queueName, taskDetails, score);
        } catch (Exception e) {
            LogUtil.error(LogEnum.BIZ_DATASET, "RedisUtils addTask error:{}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 任务完成
     *
     * @param queueName 队列名
     * @param keyId     keyId
     * @param taskType  任务类型
     * @param taskKey   任务key
     * @return boolean 是否完成
     */
    public Boolean finishedTask(String queueName, String keyId, String taskType, String taskKey) {
        DefaultRedisScript<Boolean> finishedTaskScript = new DefaultRedisScript<>();
        finishedTaskScript.setResultType(Boolean.class);
        finishedTaskScript.setLocation(new ClassPathResource("finishedTask.lua"));
        try {
            return stringRedisTemplate.execute(finishedTaskScript, Collections.singletonList(queueName)
                    , keyId, taskType, taskKey);
        } catch (Exception e) {
            LogUtil.error(LogEnum.BIZ_DATASET, "RedisUtils finishedTask error:{}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取完成任务
     *
     *
     */
    public String getFinishedTask(String queueName) {
        DefaultRedisScript<String> finishedTaskScript = new DefaultRedisScript<>();
        finishedTaskScript.setResultType(String.class);
        finishedTaskScript.setLocation(new ClassPathResource("getFinishedTask.lua"));
        try {
            return stringRedisTemplate.execute(finishedTaskScript,  Collections.singletonList(queueName));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取失败任务
     *
     *
     */
    public String getFailedTask(String queueName) {
        DefaultRedisScript<String> failedTaskScript = new DefaultRedisScript<>();
        failedTaskScript.setResultType(String.class);
        failedTaskScript.setLocation(new ClassPathResource("getFailedTask.lua"));
        try {
            return stringRedisTemplate.execute(failedTaskScript,Collections.singletonList(queueName));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 重启任务
     *
     * @param keyId           keyId
     * @param processingName  执行中名称
     * @param unprocessedName 未执行名称
     * @param detailName      详情名
     * @param datasetIdKey    数据集ID对应key
     * @return boolean 重启任务是否成功
     */
    public Boolean restartTask(String keyId, String processingName, String unprocessedName, String detailName, String datasetIdKey) {
        DefaultRedisScript<Boolean> restartTaskScript = new DefaultRedisScript<>();
        restartTaskScript.setResultType(Boolean.class);
        restartTaskScript.setLocation(new ClassPathResource("restartTask.lua"));
        try {
            return stringRedisTemplate.execute(restartTaskScript, Collections.singletonList(keyId),
                    processingName, unprocessedName, detailName, datasetIdKey);
        } catch (Exception e) {
            LogUtil.error(LogEnum.BIZ_DATASET, "RedisUtils restartTask error:{}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将zSet数据放入缓存
     *
     * @param key key
     * @return Boolean 放入数据到缓存是否成功
     */
    public Boolean zAdd(String key, Object value, Long zScore) {
        try {
            return redisTemplate.opsForZSet().add(key, value, zScore);
        } catch (Exception e) {
            LogUtil.error(LogEnum.SYS_ERR, "RedisUtils zSet key {} value {} error:{}", key, value, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 移除指定key
     *
     * @param key    key
     * @param member 所移除成员
     * @return Object 删除指定key
     */
    public Object zrem(String key, String member) {
        try {
            return redisTemplate.opsForZSet().remove(key, member);
        } catch (Exception e) {
            LogUtil.error(LogEnum.SYS_ERR, "RedisUtils zrem key {} member {} error:{}", key, member, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 任务重启
     *
     * @param startQueue   算法执行中队列
     * @param pendingQueue 算法未执行队列
     */
    public void restartTask(String startQueue, String pendingQueue) {
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = zGetWithScore(startQueue);
        typedTuples.forEach(value -> {
            String timestampString = new BigDecimal(StringUtils.substringBefore(value.getScore().toString(),"."))
                    .toPlainString();
            long timestamp = Long.parseLong(timestampString);
            String keyId = value.getValue().toString().replaceAll("\"", "");
            long timestampNow = System.currentTimeMillis() / 1000;
            if (timestampNow - timestamp > MagicNumConstant.TWO_HUNDRED) {
                LogUtil.info(LogEnum.BIZ_DATASET, "restart {} task keyId:{}", startQueue, keyId);
            }
        });
    }
}
