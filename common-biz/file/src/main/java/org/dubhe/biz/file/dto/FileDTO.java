

package org.dubhe.biz.file.dto;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @description 文件详情
 * @date 2021-05-07
 */
@Builder
@Data
public class FileDTO implements Serializable {

    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 文件最近一次修改时间
     */
    private Date lastModified;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 是否文件夹
     */
    private boolean dir;

}
