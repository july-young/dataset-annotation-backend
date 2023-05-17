

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @description 批量上传文件
 * @date 2020-04-29
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BatchFile dto", description = "文件批量上传信息")
public class BatchFileCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "文件不能为空")
    private List<FileCreateDTO> files;

    Boolean ifImport;
}
