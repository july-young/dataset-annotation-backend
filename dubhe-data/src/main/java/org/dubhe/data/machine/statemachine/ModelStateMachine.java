package org.dubhe.data.machine.statemachine;

import lombok.Data;
import org.dubhe.biz.base.utils.SpringContextHolder;
import org.dubhe.data.statemachine.exception.StateMachineException;
import org.dubhe.data.dao.AutoLabelModelServiceMapper;
import org.dubhe.data.domain.entity.AutoLabelModelService;
import org.dubhe.data.machine.state.AbstractModelState;
import org.dubhe.data.machine.state.specific.moel.ModelState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
public class ModelStateMachine extends AbstractModelState implements Serializable {

    @Autowired
    private ModelState modelState;

    @Autowired
    private AutoLabelModelServiceMapper modelMapper;

    private AbstractModelState memoryModelState;

    public AutoLabelModelService initMemoryDataState(Long primaryKeyId){
        if(primaryKeyId == null){
            throw new StateMachineException("未找到业务ID");
        }
        AutoLabelModelService modelService = modelMapper.selectById(primaryKeyId);
        if(modelService== null || modelService.getStatus()==null){
            throw new StateMachineException("未找到业务数据");
        }
        memoryModelState = SpringContextHolder.getBean("modelState");
        return modelService;
    }

    @Override
    public void startModel(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        memoryModelState.startModel(primaryKeyId);
    }

    @Override
    public void startModelFinish(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        memoryModelState.startModelFinish(primaryKeyId);
    }

    @Override
    public void startModelFail(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        memoryModelState.startModelFail(primaryKeyId);
    }

    @Override
    public void stopModel(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        memoryModelState.stopModel(primaryKeyId);
    }

    @Override
    public void stopModelFinish(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        memoryModelState.stopModelFinish(primaryKeyId);
    }
}
