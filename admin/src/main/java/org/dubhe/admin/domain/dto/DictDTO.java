package org.dubhe.admin.domain.dto;

import lombok.Data;
import org.dubhe.biz.base.dto.DictDetailDTO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @description 字典DTO
 */
@Data
public class DictDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    private String name;

    private String remark;

    private List<DictDetailDTO> dictDetails;

    private Timestamp createTime;
}
