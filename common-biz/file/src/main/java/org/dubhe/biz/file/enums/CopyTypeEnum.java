

package org.dubhe.biz.file.enums;

/**
 * @description 文件复制类型枚举
 * @date 2021-01-21
 */
public enum CopyTypeEnum {

    COPY_FILE(0,"拷贝文件"),
    COPY_DIR(1,"拷贝文件夹内文件")
    ;

    private Integer key;

    private String value;

    CopyTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
