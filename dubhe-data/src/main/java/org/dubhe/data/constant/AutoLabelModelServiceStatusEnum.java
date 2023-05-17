

package org.dubhe.data.constant;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AutoLabelModelServiceStatusEnum {

    STARTING(101, "启动中"),

    RUNNING(102, "运行中"),

    START_FAILED(103, "启动失败"),

    STOPPING(104, "停止中"),

    STOPPED(105, "已停止")
    ;

    AutoLabelModelServiceStatusEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private int value;

    private String msg;

}
