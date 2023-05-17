

package org.dubhe.data.machine.enums;

import org.dubhe.data.constant.AutoLabelModelServiceStatusEnum;

/**
 * 模型标注
 */
public enum ModelServiceStateEnum {

    STARTING(AutoLabelModelServiceStatusEnum.STARTING.getValue(), "startingModelServiceState","启动中"),

    RUNNING(AutoLabelModelServiceStatusEnum.RUNNING.getValue(), "runningModelServiceState", "运行中"),

    START_FAILED(AutoLabelModelServiceStatusEnum.START_FAILED.getValue(), "startFailedModelServiceState", "启动失败"),

    STOPING(AutoLabelModelServiceStatusEnum.STOPPING.getValue(),"stopingModelServiceState", "停止中"),

    STOPED(AutoLabelModelServiceStatusEnum.STOPPED.getValue(), "stopedModelServiceState","已停止");

    /**
     * 编码
     */
    private Integer code;
    /**
     * 状态机
     */
    private String stateMachine;
    /**
     * 描述
     */
    private String description;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStateMachine() {
        return stateMachine;
    }

    public void setStateMachine(String stateMachine) {
        this.stateMachine = stateMachine;
    }

    ModelServiceStateEnum(Integer code, String stateMachine , String description) {
        this.code = code;
        this.stateMachine = stateMachine;
        this.description = description;
    }
}
