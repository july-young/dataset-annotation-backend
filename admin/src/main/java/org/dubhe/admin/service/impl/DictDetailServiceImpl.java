/**
 * Copyright 2020 Tianshu AI Platform. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =============================================================
 */
package org.dubhe.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dubhe.admin.dao.DictDetailMapper;
import org.dubhe.admin.domain.dto.DictDetailCreateDTO;
import org.dubhe.admin.domain.dto.DictDetailQueryDTO;
import org.dubhe.admin.domain.dto.DictDetailUpdateDTO;
import org.dubhe.admin.domain.entity.DictDetail;
import org.dubhe.admin.service.DictDetailService;
import org.dubhe.admin.service.convert.DictDetailConvert;
import org.dubhe.biz.base.dto.DictDetailDTO;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.db.utils.PageDTO;
import org.dubhe.biz.db.utils.PageUtil;
import org.dubhe.biz.db.utils.WrapperHelp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * @description 字典详情服务 实现类
 * @date 2020-06-01
 */
@Service
public class DictDetailServiceImpl implements DictDetailService {

    @Autowired
    private DictDetailMapper dictDetailMapper;

    @Autowired
    private DictDetailConvert dictDetailConvert;

    /**
     * 分页查询字典详情
     */
    @Override
    public PageDTO page(DictDetailQueryDTO criteria, Page<DictDetail> page) {
        IPage<DictDetail> dictDetails = dictDetailMapper.selectPage(page, WrapperHelp.getWrapper(criteria));
        return PageUtil.toPage(dictDetails, dictDetailConvert::toDto);
    }


    /**
     * 按条件查询字典列表
     */
    @Override
    public List<DictDetailDTO> list(DictDetailQueryDTO criteria) {
        List<DictDetail> list = dictDetailMapper.selectList(WrapperHelp.getWrapper(criteria));
        return dictDetailConvert.toDto(list);
    }


    /**
     * 根据ID查询字典详情
     *
     * @param id 字典详情ID
     * @return org.dubhe.domain.dto.DictDetailDTO 字典详情实例
     */
    @Override
    public DictDetailDTO findById(Long id) {
        DictDetail dictDetail = dictDetailMapper.selectById(id);
        return dictDetailConvert.toDto(dictDetail);
    }

    /**
     * 新增字典性情
     *
     * @param resources 字典详情新增实体
     * @return org.dubhe.domain.dto.DictDetailDTO 字典详情实例
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictDetailDTO create(DictDetailCreateDTO resources) {
        if (!ObjectUtil.isNull(selectByDictIdAndLabel(resources.getDictId(), resources.getLabel()))) {
            throw new BusinessException("字典标签已存在");
        }
        DictDetail dictDetail = DictDetail.builder().build();
        BeanUtils.copyProperties(resources, dictDetail);
        dictDetailMapper.insert(dictDetail);
        return dictDetailConvert.toDto(dictDetail);
    }

    /**
     * 修改字典详情
     *
     * @param resources 字典详情修改实体
     * @return void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DictDetailUpdateDTO resources) {
        DictDetail detail = selectByDictIdAndLabel(resources.getDictId(), resources.getLabel());
        if (detail != null && !detail.getId().equals(resources.getId())) {
            throw new BusinessException("字典标签已存在");
        }
        DictDetail dbDetail = DictDetail.builder().build();
        BeanUtils.copyProperties(resources, dbDetail);
        dictDetailMapper.updateById(dbDetail);
    }

    private DictDetail selectByDictIdAndLabel(Long dictId, String label) {
        LambdaQueryWrapper<DictDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictDetail::getDictId, dictId);
        queryWrapper.eq(DictDetail::getLabel, label);
        queryWrapper.last("limit 1");
        DictDetail detail = dictDetailMapper.selectOne(queryWrapper);
        return detail;
    }

    /**
     * 删除字典详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        dictDetailMapper.deleteBatchIds(ids);
    }

    @Override
    public List<DictDetailDTO> getDictDetailByLabel(String label) {
        if (StringUtils.isEmpty(label)) {
            throw new BusinessException("标签名称不能为空！");
        }
        LambdaQueryWrapper<DictDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DictDetail::getLabel, label);
        List<DictDetail> dictDetails = dictDetailMapper.selectList(lambdaQueryWrapper);
        List<DictDetailDTO> dictDetailVOList = dictDetailConvert.toDto(dictDetails);

        return dictDetailVOList;
    }

}
