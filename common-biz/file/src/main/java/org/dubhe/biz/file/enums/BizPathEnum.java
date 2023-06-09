

package org.dubhe.biz.file.enums;

import org.dubhe.biz.base.enums.BizEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 业务NFS路径枚举

 */
public enum BizPathEnum {
    /**
     * 模型开发 路径命名
     */
    NOTEBOOK(BizEnum.NOTEBOOK, "notebook"),
    /**
     * 算法管理 路径命名
     */
    ALGORITHM(BizEnum.ALGORITHM, "algorithm-manage"),
    /**
     * 模型管理 路径命名
     */
    MODEL(BizEnum.MODEL, "model"),
    /**
     * 模型优化 路径命名
     */
    MODEL_OPT(BizEnum.MODEL_OPT, "model-opt"),

    /**
     * 度量管理 路径命名
     */
    MEASURE(BizEnum.MEASURE, "exported-metrics");

    BizPathEnum(BizEnum bizEnum, String bizPath) {
        this.bizEnum = bizEnum;
        this.bizPath = bizPath;
    }

    /**
     * 业务模块
     */
    private BizEnum bizEnum;
    /**
     * 业务模块路径
     */
    private String bizPath;


    private static final Map<Integer, BizPathEnum> RESOURCE_ENUM_MAP = new HashMap<Integer, BizPathEnum>() {
        {
            for (BizPathEnum enums : BizPathEnum.values()) {
                put(enums.getCreateResource(), enums);
            }
        }
    };

    /**
     * 根据createResource获取BizEnum
     *
     * @param createResource
     * @return
     */
    public static BizPathEnum getByCreateResource(int createResource) {
        return RESOURCE_ENUM_MAP.get(createResource);
    }


    public String getBizName() {
        return bizEnum == null ? null : bizEnum.getBizName();
    }

    public Integer getCreateResource() {
        return bizEnum == null ? null : bizEnum.getCreateResource();
    }

    public String getBizPath() {
        return bizPath;
    }

    public BizEnum getBizEnum() {
        return bizEnum;
    }

    public String getBizCode() {
        return bizEnum == null ? null : bizEnum.getBizCode();
    }
}
