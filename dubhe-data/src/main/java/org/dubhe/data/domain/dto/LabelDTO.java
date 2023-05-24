

package org.dubhe.data.domain.dto;

import lombok.Data;
import org.dubhe.data.domain.entity.Label;

import java.io.Serializable;

/**
 * @description 标签DTO

 */
@Data
public class LabelDTO  extends Label implements Serializable {


    /**
     * 标签组ID
     */
    private Long labelGroupId;
}
