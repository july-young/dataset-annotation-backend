

package org.dubhe.biz.base.enums;

import lombok.Getter;

/**
 * @description 算法枚举类

 */
@Getter
public enum AlgorithmSourceEnum {

    /**
     * MINE 算法来源  我的算法
     */
    MINE(1, "MINE"),
    /**
     * PRE  算法来源  预置算法
     */
    PRE(2,"PRE");

    private Integer status;

    private String message;

    AlgorithmSourceEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
