

package org.dubhe.data.domain.bo;

import lombok.*;
import org.dubhe.data.domain.entity.File;

import java.io.Serializable;

/**
 * @description 文件Bo

 */
@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileBO implements Serializable {

    private Long id;
    private String name;
    private Long datasetId;
    private String url;

    /**
     * 把File对象转换为FileBO对象
     *
     * @param file 文件对象
     * @return 转换后的fileBO
     */
    public static FileBO from(File file) {
        FileBO fileBO = new FileBO();
        fileBO.setName(file.getName());
        fileBO.setId(file.getId());
        fileBO.setDatasetId(file.getDatasetId());
        fileBO.setUrl(file.getUrl());
        return fileBO;
    }

}
