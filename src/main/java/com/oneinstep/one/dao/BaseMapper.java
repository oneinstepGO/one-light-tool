package com.hst.bss.light.dao;

import com.aicai.appmodel.page.DataPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static com.hst.bss.light.dao.PageScrollQuery.OrderEnum.ASC;
import static com.hst.bss.light.dao.PageScrollQuery.OrderEnum.DESC;

/**
 * mapper 基类
 * 基于mybatis，提供一些通用增删改查、分页、滚动分页、乐观锁等能力
 * D: 数据表对应的实体对象  Q: 查询参数
 *
 * DAO 层代码自动生成教程 见 <link>https://wiki.hszq8.com/pages/viewpage.action?pageId=28669922</link>
 * @author aaron.shaw
 * @date 2023-03-14 15:21
 **/
public interface BaseMapper<D, Q> {

    /**
     * 保存数据，只保存非NULL字段
     *
     * @param d 要插入的记录
     * @return 影响行数
     */
    int insertSelective(D d);

    /**
     * 根据 主键ID 查询数据
     *
     * @param id 主键ID
     * @return 数据
     */
    D selectByPrimaryKey(Long id);

    /**
     * 使用乐观锁根据 ID 和 VERSION 更新数据，只更新参数中非NULL字段
     *
     * @param d 更新数据，id,version字段必传
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(D d);

    /**
     * 按唯一条件查询一条唯一记录 or null
     *
     * @param q 查询条件 按该条件只能查询出一条数据，否则查出多条数据会报错
     * @return 唯一数据 or null
     */
    D queryUnique(@Param("query") Q q);

    /**
     * 按条件查询数据列表
     *
     * @param q 查询条件
     * @return 数据列表
     */
    List<D> listByQuery(@Param("query") Q q);

    /**
     * 按查询条件和滚动分页参数 记录数据列表
     *
     * @param q         查询条件
     * @param pageQuery 滚动分页参数
     * @return 数据列表
     */
    List<D> listScrollByQuery(@Param("query") Q q, @Param("pageQuery") PageScrollQuery<?> pageQuery);

    /**
     * 分页 查询数据列表
     *
     * @param page 分页条件
     * @param q    查询条件
     * @return 数据列表
     */
    default DataPage<D> pageByQuery(DataPage<?> page, Q q) {
        return PageConverter.convert(page, () -> listByQuery(q));
    }

    /**
     * 滚动分页 查询数据列表
     *
     * @param q         查询条件
     * @param pageQuery 滚动分页参数
     * @return 记录列表
     */
    default List<D> pageWithScroll(@Param("query") Q q, @Param("pageQuery") PageScrollQuery<?> pageQuery) {
        if (ASC == pageQuery.getOrder()) {
            if (pageQuery.getLastMaxValue() == null) {
                throw new IllegalArgumentException("Scroll Page error: lastMaxValue in pageQuery must not be null.");
            }
        } else if (DESC == pageQuery.getOrder()) {
            if (pageQuery.getLastMinValue() == null) {
                throw new IllegalArgumentException("Scroll Page error: lastMinValue in pageQuery must not be null.");
            }
        } else {
            throw new IllegalArgumentException("Scroll Page error: order in pageQuery must be ASC or DESC.");
        }
        return PageConverter.convertWithFirstPage(() -> listScrollByQuery(q, pageQuery), pageQuery);
    }

}
