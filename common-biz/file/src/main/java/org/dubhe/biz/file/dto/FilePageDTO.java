

package org.dubhe.biz.file.dto;

import lombok.Data;
import java.util.List;

/**
 * @description 文件分页查询相应实体
 * @date 2021-06-16
 */
@Data
public class FilePageDTO {

    /**
     * 查询路径
     */
    private String filePath;
    /**
     * 页码
     */
    private int pageNum;
    /**
     * 页容量
     */
    private int pageSize;
    /**
     * 记录数
     */
    private Long total;
    /**
     * 页集合
     */
    private List<FileDTO> rows;

}
