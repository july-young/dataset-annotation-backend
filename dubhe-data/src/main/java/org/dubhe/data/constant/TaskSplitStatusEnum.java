

package org.dubhe.data.constant;

import lombok.Getter;

/**
 * @description 任务分片状态

 */
@Getter
public enum TaskSplitStatusEnum {

    /**
     * 进行中
     */
    ING(1, "进行中"),

    /**
     * 已完成
     */
    FINISHED(2, "已完成"),
    ;

    TaskSplitStatusEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private int value;
    private String msg;

}
