

package org.dubhe.data.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description 数据集标签类型
 * @date 2020-07-01
 */
public enum DatasetLabelEnum {

    /**
     * 自定义标签
     */
    CUSTOM(0, "自定义标签"),
    /**
     * 自动标注标签
     */
    AUTO(1, "自动标注标签"),
    /**
     * imageNet
     */
    IMAGE_NET(2, "ImageNet"),
    /**
     * MS COCO
     */
    MS_COCO(3, "MS COCO"),
    /**
     * 文本
     */
    TXT(4, "文本");

    DatasetLabelEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    private Integer type;
    private String name;

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    /**
     * 预置标签类型
     */
    public static final List<DatasetLabelEnum> PRESET_LABELS = new ArrayList<DatasetLabelEnum>() {{
        add(DatasetLabelEnum.IMAGE_NET);
        add(DatasetLabelEnum.MS_COCO);
    }};

    /**
     * 获取所有预置标签 web端展示用
     *
     * @return
     */
    public static Map<Integer, String> getPresetLabels() {
        return PRESET_LABELS.stream().collect(Collectors.toMap(datasetLabelEnum -> datasetLabelEnum.getType(), datasetLabelEnum -> datasetLabelEnum.getName()));
    }

}
