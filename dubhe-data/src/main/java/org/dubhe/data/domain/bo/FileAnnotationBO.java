

package org.dubhe.data.domain.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileAnnotationBO implements Serializable {

    private Long  fileId;

    private String fileUrl;

    private String annotationUrl;

    private String fileName;

    private Integer fileWidth;

    private Integer fileHeight;
}
