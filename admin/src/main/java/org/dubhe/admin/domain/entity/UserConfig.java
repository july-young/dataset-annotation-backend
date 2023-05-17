
package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dubhe.biz.db.entity.BaseEntity;

/**
 * @description 用户配置实体
 * @date 2021-06-30
 */
@Data
@TableName("user_config")
@Accessors(chain = true)
public class UserConfig extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableId(value = "user_id")
    private Long userId;

    @TableId(value = "notebook_delay_delete_time")
    private Integer notebookDelayDeleteTime;

    @TableId(value = "cpu_limit")
    private Integer cpuLimit;

    @TableId(value = "memory_limit")
    private Integer memoryLimit;

    @TableId(value = "gpu_limit")
    private Integer gpuLimit;
}
