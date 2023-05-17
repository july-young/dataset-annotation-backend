

package org.dubhe.data.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @description 分页工具
 * @date 2020-04-10
 */
public class PageUtil {

    /**
     * 构建分页
     *
     * @param current 当前页码
     * @param size    总页数
     * @param <T>     当前页数据
     * @return        Page
     */
    public static <T> Page<T> build(Integer current, Integer size) {
        Page<T> page = new Page<>();
        if (current != null) {
            page.setCurrent(current);
        }
        if (size != null) {
            page.setSize(size);
        }
        return page;
    }

}
