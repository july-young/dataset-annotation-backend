package org.dubhe.biz.db.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class PageDTO<T> {

    private List<T> result;

    private Pagination page;

    public static final PageDTO EMPTY_PAGE = empty();

    private static PageDTO empty() {
        PageDTO pageDTO = new PageDTO();
        Pagination pagination = new Pagination();
        pagination.setTotal(0L);
        pagination.setSize(0L);
        pagination.setCurrent(0L);
        pageDTO.setResult(new ArrayList<>());
        pageDTO.setPage(pagination);
        return pageDTO;
    }

}

