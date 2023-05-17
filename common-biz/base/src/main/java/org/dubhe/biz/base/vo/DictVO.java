package org.dubhe.biz.base.vo;

import lombok.Data;
import org.dubhe.biz.base.dto.DictDetailDTO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class DictVO implements Serializable {

    private static final long serialVersionUID = -1176729960392375726L;
    private Long id;

    private String name;

    private String remark;

    private List<DictDetailDTO> dictDetails;

    private Timestamp createTime;
}
