
package org.dubhe.recycle.enums;

import org.dubhe.biz.base.constant.MagicNumConstant;
import org.dubhe.biz.base.exception.BusinessException;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.dubhe.biz.base.constant.ApplicationNameConst.*;
/**
 * @description 垃圾回收模块枚举

 */
public enum RecycleModuleEnum {


    BIZ_DATASET(2, "数据集管理",SERVER_DATA);

    private Integer value;

    private String desc;

    /**
     * 模块服务名
     */
    private String server;

    RecycleModuleEnum(Integer value, String desc,String server) {
        this.value = value;
        this.desc = desc;
        this.server = server;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 获取服务名
     * @param value 模块代号
     * @return 服务
     */
    public static String getServer(int value){
        for (RecycleModuleEnum recycleModuleEnum:RecycleModuleEnum.values()){
            if (value == recycleModuleEnum.getValue()){
                if (recycleModuleEnum.server == null){
                    throw new BusinessException(recycleModuleEnum.desc + " 服务未配置服务名！");
                }
                return recycleModuleEnum.server;
            }
        }
        throw new BusinessException(value+" 服务不存在！");
    }

    /**
     * 模块代号，模块名称 映射
     */
    public static final Map<Integer,String> RECYCLE_MODULE_MAP;
    static {
        RECYCLE_MODULE_MAP = new LinkedHashMap<>(MagicNumConstant.SIXTEEN);
        for (RecycleModuleEnum recycleModuleEnum:RecycleModuleEnum.values()){
            RECYCLE_MODULE_MAP.put(recycleModuleEnum.value,recycleModuleEnum.desc);
        }
    }

}
