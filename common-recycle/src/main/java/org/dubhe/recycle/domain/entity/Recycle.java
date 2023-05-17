package org.dubhe.recycle.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dubhe.biz.db.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * @description 垃圾回收主表
 * @date 2021-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "recycle")
public class Recycle extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 回收模块
     */
    @TableField(value = "recycle_module")
    private Integer recycleModule;

    /**
     * 回收延迟时间,以天为单位
     */
    @TableField(value = "recycle_delay_date")
    private Timestamp recycleDelayDate;

    /**
     * 回收定制化方式
     */
    @TableField(value = "recycle_custom")
    private String recycleCustom;

    /**
     * 回收状态
     */
    @TableField(value = "recycle_status")
    private Integer recycleStatus;

    /**
     * 回收说明
     */
    @TableField(value = "recycle_note")
    private String recycleNote;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 回收响应信息
     */
    @TableField(value = "recycle_response")
    private String recycleResponse;

    /**
     * 还原定制化方式
     */
    @TableField(value = "restore_custom")
    private String restoreCustom;

}
