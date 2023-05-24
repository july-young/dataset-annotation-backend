

package org.dubhe.data.service;

import org.dubhe.data.domain.entity.DataSequence;

/**
 * @description 获取序列服务接口

 */
public interface DataSequenceService {
    /**
     * 根据业务编码获取序列号
     * @param businessCode    业务编码
     * @return DataSequence   序列实体
     */
    DataSequence getSequence(String businessCode);
    /**
     * 根据业务编码更新起点
     * @param businessCode  业务编码
     * @return DataSequence 序列实体
     */
    int updateSequenceStart(String businessCode);

    /**
     * 检查表是否存在
     * @param tableName 表名
     * @return boolean  是否存在标识
     */
    boolean checkTableExist(String tableName);

    /**
     * 执行存储过程创建表
     * @param tableId 表名
     */
    void createTable(String tableId);

    /**
     * 扩容可用数量
     *
     * @param businessCode 业务编码
     * @return DataSequence 数据ID序列
     */
    DataSequence expansionUsedNumber(String businessCode);
}
