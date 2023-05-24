

package org.dubhe.data.constant;

import lombok.Getter;

/**
 * @description 数据集解压状态枚举类

 */
@Getter
public enum DatasetDecompressStateEnum {

    /**
     * 未解压
     */
    NOT_DECOMPRESSED(0, "未解压"),
    /**
     * 解压中
     */
    DECOMPRESSING(1, "解压中"),
    /**
     * 解压完成
     */
    DECOMPRESS_COMPLETE(2, "解压完成"),
    /**
     * 解压失败
     */
    DECOMPRESS_FAIL(3, "解压失败")
    ;

    private Integer value;
    private String msg;

    DatasetDecompressStateEnum(Integer value, String msg) {
        this.value = value;
        this.msg = msg;
    }

}
