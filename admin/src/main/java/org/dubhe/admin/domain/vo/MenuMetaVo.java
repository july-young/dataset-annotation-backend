package org.dubhe.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 菜单元数据

 */
@Data
@AllArgsConstructor
public class MenuMetaVo implements Serializable {

    private static final long serialVersionUID = 4641840267582701511L;
    private String title;

    private String icon;

    private String layout;

    private Boolean noCache;
}
