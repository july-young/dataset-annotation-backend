

package org.dubhe.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 字典查询转换DTO
 * @date 2020-06-01
 */
@Data
public class DictSmallQueryDTO implements Serializable {
    private static final long serialVersionUID = 5825111154262768118L;
    private Long id;
    private String name;
}
