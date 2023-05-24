package org.dubhe.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 菜单VO

 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVo implements Serializable {

    private static final long serialVersionUID = 7145999097655311261L;
    private String name;

    private String path;

    private Boolean hidden;

    private String component;

    private MenuMetaVo meta;

    private List<MenuVo> children;
}
