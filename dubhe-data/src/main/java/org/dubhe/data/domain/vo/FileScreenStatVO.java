

package org.dubhe.data.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王伟
 * @date 2022年07月12日 13:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileScreenStatVO {

    private Long haveAnnotation;
    private Long noAnnotation;

}
