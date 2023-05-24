package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.biz.db.entity.BaseEntity;

import java.io.Serializable;

/**
 * @description 菜单实体

 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("menu")
public class Menu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3100515433018008777L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @TableField(value = "sort")
    private Long sort = 999L;

    @TableField(value = "path")
    private String path;

    @TableField(value = "component")
    private String component;

    /**
     * 类型，目录、菜单、按钮
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 权限
     */
    @TableField(value = "permission")
    private String permission;

    @TableField(value = "component_name")
    private String componentName;

    @TableField(value = "icon")
    private String icon;

    /**
     * 布局类型
     */
    @TableField(value = "layout")
    private String layout;

    @TableField(value = "cache")
    private Boolean cache;

    @TableField(value = "hidden")
    private Boolean hidden;

    /**
     * 上级菜单ID
     */
    @TableField(value = "pid")
    private Long pid;

    @TableField(value = "deleted",fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted=false;

    /**
     * 回到上一级
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String backTo;

    /**
     * 扩展配置
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String extConfig;


    public @interface Update {
    }
}
