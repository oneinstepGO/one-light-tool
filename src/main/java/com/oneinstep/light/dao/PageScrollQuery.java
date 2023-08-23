package com.oneinstep.light.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 滚动分页查询参数
 * T 排序字段类型，必须实现 Comparable接口
 *
 * 
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageScrollQuery<T extends Comparable> {

    /**
     * 按ID 升序 还是 降序
     */
    private OrderEnum order;

    /**
     * 排序字段 默认按ID排序
     * 注意排序字段最好是有序唯一字段，否则可能因为pageSize和条件不当导致
     * 部分数据查询不到
     */
    private String orderBy;

    /**
     * 每页数据大小
     */
    private Integer pageSize;

    /**
     * 滚动分页查询中 按某字段升序排序 上一次查询出的最大值
     */
    private T lastMaxValue;

    /**
     * 滚动分页查询中 按某字段降序排序 上一次查询出的最小值
     */
    private T lastMinValue;

    /**
     * 排序方向
     */
    public enum OrderEnum {
        /**
         * 升序
         */
        ASC,
        /**
         * 降序
         */
        DESC
    }

}
