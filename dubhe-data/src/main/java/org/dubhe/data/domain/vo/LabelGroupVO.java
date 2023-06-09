

package org.dubhe.data.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.data.domain.entity.Label;
import org.dubhe.data.domain.entity.LabelGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 标签组详情

 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelGroupVO implements Serializable {

    /**
     * 标签组ID
     */
    private Long id;

    /**
     * 标签组名称
     */
    private String name;
    /**
     * 标签组类型：0: private 私有标签组,  1:public 公开标签组
     */
    private Integer type;

    /**
     * 标签组描述
     */
    private String remark;

    /**
     * 操作类型 1:Json编辑器操作类型 2:自定义操作类型 3:导入操作类型
     */
    private Integer operateType;

    /**
     * 标签列表
     */
    private List<LabelVO> labels;

    /**
     * 标签组类型:0:视觉,1:文本
     */
    private Integer labelGroupType;

    /**
     * 获取标签组及标签
     *
     * @param labelGroup     标签组
     * @param labels         标签
     * @return LabelGroupVO 标签组和标签
     */
    public static LabelGroupVO from(LabelGroup labelGroup,List<Label> labels) {
        LabelGroupVO LabelGroupVO = new LabelGroupVO();
        LabelGroupVO.setId(labelGroup.getId());
        LabelGroupVO.setName(labelGroup.getName());
        LabelGroupVO.setRemark(labelGroup.getRemark());
        List<LabelVO> labelVOS = new ArrayList<>();
        for(Label label : labels){
           LabelVO labelVO = LabelVO.builder()
                    .id(label.getId())
                    .name(label.getName())
                    .color(label.getColor())
                    .build();
            labelVOS.add(labelVO);
        }
        LabelGroupVO.setLabels(labelVOS);
        return LabelGroupVO;
    }
}
