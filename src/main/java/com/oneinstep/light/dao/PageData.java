package com.oneinstep.light.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 分页对象
 */
@Getter
@Setter
@ToString
public class PageData<T> {
    private Integer pageNo;
    private Integer pageSize;
    private Long totalCount;
    private Boolean needTotalCount;
    private Boolean needData;
    private String order;
    private String orderBy;
    private List<T> dataList;
}
