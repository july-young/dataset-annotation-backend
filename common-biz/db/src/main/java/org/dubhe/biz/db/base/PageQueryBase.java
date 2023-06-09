

package org.dubhe.biz.db.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description 分页基类

 */
@Data
@Accessors(chain = true)
public class PageQueryBase<T> {

    /**
     * 分页-当前页数
     */
    private Integer current;

    /**
     * 分页-每页展示数
     */
    private Integer size;

    /**
     * 排序字段
     */
    private String sort;

    /**
     * 排序方式,asc | desc
     */
    private String order;

    public Page<T> toPage() {
        Page<T> page = new Page();
        if (this.current != null) {
            page.setCurrent(this.current);
        }
        if (this.size != null && this.size < 2000) {
            page.setSize(this.size);
        }
        return page;
    }

}
