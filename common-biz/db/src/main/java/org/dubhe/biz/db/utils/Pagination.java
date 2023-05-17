package org.dubhe.biz.db.utils;

import lombok.Data;

@Data
public class Pagination {

    private Long current;

    private Long size;

    private Long total;
}