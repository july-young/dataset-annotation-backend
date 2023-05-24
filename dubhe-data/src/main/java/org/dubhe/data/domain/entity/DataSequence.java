

package org.dubhe.data.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 序列表

 */
@Data
@TableName("data_sequence")
public class DataSequence {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "business_code")
    private String businessCode;

    @TableField(value = "start")
    private Long start;

    @TableField(value = "step")
    private Long step;

}