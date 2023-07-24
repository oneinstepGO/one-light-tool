package com.hst.bss.light.dao;

import com.aicai.appmodel.page.DataPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 分页查询转换工具
 * PageHelper 会拦截查询SQL，为其动态增加一些分页SQL语句和排序SQL语句。
 *
 * @author qiang.li
 * @date 2023/3/8 11:08
 */
public class PageConverter {

    /**
     * 普通分页转换
     *
     * @param sourcePage 查询返回的结果对象
     * @param listQuery  aicai 分页模型
     * @return 分页对象
     */
    public static <T> DataPage<T> convert(DataPage<?> sourcePage, ListQueryExecute<T> listQuery) {
        DataPage<T> targetPage = copyWithoutData(sourcePage);
        // aicai appmode包里面orderBy字段拆分成了两个orderBy 和 order
        String orderBy;
        if (!StringUtils.isAnyEmpty(targetPage.getOrderBy(), targetPage.getOrder())) {
            orderBy = StringUtils.join(targetPage.getOrderBy(), StringUtils.SPACE, targetPage.getOrder());
        } else {
            orderBy = targetPage.getOrderBy();
        }
        //PageHelper工具分页
        Page<T> page = PageMethod.startPage(targetPage.getPageNo(), targetPage.getPageSize(), orderBy);
        List<T> query = listQuery.query();
        PageInfo<T> pageInfo = page.toPageInfo();
        targetPage.setDataList(query);
        if (targetPage.isNeedTotalCount()) {
            targetPage.setTotalCount(pageInfo.getTotal());
        }
        return targetPage;
    }

    /**
     * 分页只取第一页，不取总数，解决深度分页问题
     *
     * @param listQuery mybatis查询执行器
     * @param pageQuery 滚动分页参数
     * @param <T>       元素对象
     * @return 数据列表
     */
    public static <T> List<T> convertWithFirstPage(ListQueryExecute<T> listQuery, PageScrollQuery<?> pageQuery) {
        Integer pageSize = pageQuery.getPageSize();
        if (pageSize == null || pageSize < 1) {
            throw new IllegalArgumentException("Page error: pageSize must be greater than 0");
        }

        String orderBy = StringUtils.join(StringUtils.isBlank(pageQuery.getOrderBy()) ? "ID" : pageQuery.getOrderBy(),
                StringUtils.SPACE, pageQuery.getOrder().name());
        //取第一页，不取总数；
        PageMethod.startPage(1, pageSize, false).setOrderBy(orderBy);
        return listQuery.query();
    }


    /**
     * copy分页对象，不拷贝数据
     *
     * @param sourcePage 源拷贝对象
     * @param <S>        源数据类型
     * @param <T>        结果数据类型
     * @return 拷贝结果
     */
    public static <S, T> DataPage<T> copyWithoutData(DataPage<S> sourcePage) {
        DataPage<T> targetPage = new DataPage<>();
        if (sourcePage != null) {
            targetPage.setPageNo(sourcePage.getPageNo());
            targetPage.setPageSize(sourcePage.getPageSize());
            targetPage.setTotalCount(sourcePage.getTotalCount());
            targetPage.setNeedData(sourcePage.isNeedData());
            targetPage.setNeedTotalCount(sourcePage.isNeedTotalCount());
            targetPage.setOrder(sourcePage.getOrder());
            targetPage.setOrderBy(sourcePage.getOrderBy());
        }
        return targetPage;
    }

    /**
     * 分页查询中的list数据查询执行
     */
    @FunctionalInterface
    public interface ListQueryExecute<R> {

        /**
         * 拿查询结果
         *
         * @return 数据列表
         */
        List<R> query();

    }

}
