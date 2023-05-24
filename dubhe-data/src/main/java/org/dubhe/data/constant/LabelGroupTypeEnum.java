
package org.dubhe.data.constant;

import lombok.Getter;

/**
 * @description 标签组数据类型

 */
@Getter
public enum LabelGroupTypeEnum {

    /**
     * 视觉
     */
    VISUAL(0, "视觉"),
    /**
     * 文本
     */
    TXT(1, "文本"),
    /**
     * 表格
     */
    TABLE(2, "表格"),
    /**
     * 音频
     */
    AUDIO(4, "音频"),
    /**
     * 点云
     */
    POINT_CLOUD(5,"点云");

    LabelGroupTypeEnum(Integer value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private Integer value;
    private String msg;


    /**
     * 标签组类型转换
     *
     * @param datatypeEnum 数据类型
     * @return 标签组类型
     */
    public static LabelGroupTypeEnum convertGroup(DatatypeEnum datatypeEnum){
        LabelGroupTypeEnum labelGroupTypeEnum;
        switch (datatypeEnum){
            case TEXT:
                labelGroupTypeEnum = LabelGroupTypeEnum.TXT;
                break;
            case TABLE:
                labelGroupTypeEnum = LabelGroupTypeEnum.TABLE;
                break;
            case AUDIO:
                labelGroupTypeEnum = LabelGroupTypeEnum.AUDIO;
                break;
            case POINT_CLOUD:
                labelGroupTypeEnum = LabelGroupTypeEnum.POINT_CLOUD;
                break;
            default:
                labelGroupTypeEnum = LabelGroupTypeEnum.VISUAL;
                break;
        }
        return labelGroupTypeEnum;
    }

}
