package org.dubhe.data.constant;

import lombok.Getter;

/**
 * @description 任务状态
 * @date 2020-04-10
 */
@Getter
public enum TaskStatusEnum {

    UN_ASSIGN(0, "待分配"),
    ASSIGNING(1, "分配中"),
    RUNNING(2, "进行中"),
    FINISHED(3, "已完成"),
    FAILED(4, "失败"),
    ;

    TaskStatusEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private int value;
    private String msg;

}
