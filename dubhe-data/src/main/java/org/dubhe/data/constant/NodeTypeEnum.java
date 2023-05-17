

package org.dubhe.data.constant;

import lombok.Getter;

@Getter
public enum NodeTypeEnum {


    CPU(0, "cpu"),

    GPU(1, "gpu"),
    ;

    NodeTypeEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private int value;
    private String msg;
}
