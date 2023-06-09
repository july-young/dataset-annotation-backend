
package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dubhe.biz.db.entity.BaseEntity;

import javax.validation.constraints.NotNull;

/**
 * @description 资源规格实体类
 * @date 2021-05-27
 */
@Data
@Accessors(chain = true)
@TableName("resource_specs")
public class ResourceSpecs extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {Update.class})
    private Long id;

    /**
     * 规格名称
     */
    @TableField(value = "specs_name")
    private String specsName;

    /**
     * 规格类型(0为CPU, 1为GPU)
     */
    @TableField(value = "resources_pool_type")
    private Boolean resourcesPoolType;

    /**
     * 所属业务场景
     */
    @TableField(value = "module")
    private Integer module;

    /**
     * CPU数量,单位：m(毫核)
     */
    @TableField(value = "cpu_num")
    private Integer cpuNum;

    /**
     * GPU数量，单位：核
     */
    @TableField(value = "gpu_num")
    private Integer gpuNum;

    /**
     * 内存大小，单位：Mi
     */
    @TableField(value = "mem_num")
    private Integer memNum;

    /**
     * 工作空间的存储配额，单位：Mi
     */
    @TableField(value = "workspace_request")
    private Integer workspaceRequest;

}