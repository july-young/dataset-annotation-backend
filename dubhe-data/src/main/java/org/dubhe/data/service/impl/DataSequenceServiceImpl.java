

package org.dubhe.data.service.impl;

import org.dubhe.biz.base.exception.DataSequenceException;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.data.dao.DataSequenceMapper;
import org.dubhe.data.domain.entity.DataSequence;
import org.dubhe.data.service.DataSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @description 获取序列服务接口实现

 */
@Service
public class DataSequenceServiceImpl implements DataSequenceService {

    @Autowired
    private DataSequenceMapper dataSequenceMapper;

    @Override
    public DataSequence getSequence(String businessCode) {
        return dataSequenceMapper.selectByBusiness(businessCode);
    }

    /**
     * 修改步长
     *
     * @param businessCode 业务编码
     * @return
     */
    @Override
    public int updateSequenceStart(String businessCode) {
        return dataSequenceMapper.updateStartByBusinessCode(businessCode);
    }

    /**
     * 检查表是否存在
     *
     * @param tableName 表名
     * @return  是否存在标识
     */
    @Override
    public boolean checkTableExist(String tableName) {
        try {
            dataSequenceMapper.checkTableExist(tableName);
            return true;
        }catch (Exception e){
            LogUtil.info(LogEnum.DATA_SEQUENCE,"表不存在");
            return false;
        }
    }

    /**
     * 创建表
     *
     * @param tableName 表名
     */
    @Override
    public void createTable(String tableName) {
        String oldTableName = tableName.substring(0,tableName.lastIndexOf("_"));
        dataSequenceMapper.createNewTable(tableName,oldTableName);
    }

    /**
     * 扩容可用数量
     *
     * @param businessCode 业务编码
     * @return DataSequence 数据ID序列
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataSequence expansionUsedNumber(String businessCode) {
        DataSequence dataSequenceNew = getSequence(businessCode);
        if (dataSequenceNew == null || dataSequenceNew.getStart() == null || dataSequenceNew.getStep() == null) {
            throw new DataSequenceException("配置出错，请检查data_sequence表配置");
        }
        updateSequenceStart(businessCode);
        LogUtil.info(LogEnum.BIZ_DATASET, "expansion used number: {}", dataSequenceNew.toString());
        return dataSequenceNew;
    }

}
