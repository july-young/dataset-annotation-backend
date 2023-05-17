

package org.dubhe.data.domain.bo;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadBO implements Serializable {

    String fileUrl;

    String fileName;

    Long fileId;

    Long versionFileId;

    String annPath;
}
