

package org.dubhe.biz.base.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.biz.base.constant.MagicNumConstant;

import java.io.Serializable;

/**
 * @description 数据集状态

 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProgressVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Builder.Default
    private Long finished = MagicNumConstant.ZERO_LONG;
    @Builder.Default
    private Long unfinished = MagicNumConstant.ZERO_LONG;
    @Builder.Default
    private Long autoFinished = MagicNumConstant.ZERO_LONG;
    @Builder.Default
    private Long finishAutoTrack = MagicNumConstant.ZERO_LONG;
    @Builder.Default
    private Long annotationNotDistinguishFile = MagicNumConstant.ZERO_LONG;
    @Builder.Default
    private Long haveAnnotation = MagicNumConstant.ZERO_LONG;
    @Builder.Default
    private Long noAnnotation = MagicNumConstant.ZERO_LONG;

}