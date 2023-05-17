

package org.dubhe.biz.db.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.*;
import java.util.function.Function;

/**
 * @description  分页工具
 * @date 2020-03-13
 */
public class PageUtil extends cn.hutool.core.util.PageUtil {

    /**
     * List 分页
     */
    public static List toPage(int page, int size, List list) {
        int fromIndex = page * size;
        int toIndex = page * size + size;
        if (fromIndex > list.size()) {
            return new ArrayList();
        } else if (toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }

    /**
     * Page 数据处理，预防redis反序列化报错
     * @return
     */
    public static PageDTO toPage(IPage page) {
        return toPage(page, page.getRecords());
    }

    /**
     * 自定义分页
     */
    public static PageDTO toPage(IPage page, Function<? super List, List> function) {
        return toPage(page,function.apply(page.getRecords()));
    }

    /**
     * 自定义分页
     * @return
     */
    public static <T>  PageDTO<T> toPage(IPage page, List<T> data) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setResult(data);
        pageDTO.setPage(buildPagination(page));
        return pageDTO;
    }



    private static Pagination buildPagination(IPage page) {
        Pagination pagination = new Pagination();
        pagination.setCurrent(page.getCurrent());
        pagination.setSize(page.getSize());
        pagination.setTotal( page.getTotal());
        return pagination;
    }
}
