<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperInterface.packageName}.${baseInfo.fileName}">

    <resultMap id="BaseResultMap" type="${tableClass.fullClassName}">
        <#list tableClass.pkFields as field>
            <id property="${field.fieldName}" column="${field.columnName}" jdbcType="${field.jdbcType}"/>
        </#list>
        <#list tableClass.baseFields as field>
            <result property="${field.fieldName}" column="${field.columnName}" jdbcType="${field.jdbcType}"/>
        </#list>
    </resultMap>

    <sql id="Base_Column_List">
        <#list tableClass.allFields as field>${field.columnName}<#sep>,<#if field_index%3==2>${"\n        "}</#if></#list>
    </sql>

    <!-- 根据主键查询数据 -->
    <select id="selectByPrimaryKey" parameterType="java.lang.Long" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableClass.tableName}
        where<#list tableClass.pkFields as field> ${field.columnName} = ${'#'}{${field.fieldName}, jdbcType=${field.jdbcType}} <#if field_has_next>AND</#if></#list>
    </select>

    <!-- 查询一条数据 -->
    <select id="queryUnique" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableClass.tableName}
        <where>
            <include refid="where_query_list_condition"/>
        </where>
    </select>

    <!-- 保存数据 -->
    <insert id="insertSelective" keyColumn="ID" keyProperty="id" useGeneratedKeys="true"
            parameterType="${tableClass.fullClassName}">
        insert into ${tableClass.tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
            ID,
            <#list tableClass.baseBlobFields as field>
                <if test="${field.fieldName} != null">${field.columnName},</if>
            </#list>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            SEQ_${tableClass.tableName}.nextval,
            <#list tableClass.baseBlobFields as field>
                <if test="${field.fieldName} != null">${'#'}{${field.fieldName}, jdbcType=${field.jdbcType}},</if>
            </#list>
        </trim>
    </insert>

    <!-- 带乐观锁的更新，只更新非NULL 字段 ，id和version必传 -->
    <update id="updateByPrimaryKeySelective" parameterType="${tableClass.fullClassName}">
        update ${tableClass.tableName}
        <set>
            <#list tableClass.baseBlobFields as field>
                <#if field.columnName != "VERSION">
                    <if test="${field.fieldName} != null">
                        ${field.columnName} = ${'#'}{${field.fieldName}, jdbcType=${field.jdbcType}},
                    </if>
                </#if>
            </#list>
            VERSION = VERSION + 1
        </set>
        where ID = ${'#'}{id, jdbcType=DECIMAL}
        AND VERSION = ${'#'}{version}
    </update>

    <!-- 查询条件 -->
    <sql id="where_query_list_condition">
        <!-- 需要自己编写 例如-->
        <!--
        <if test="query.fieldName != null">
            AND COLUMN_NAME = ${'#'}{query.fieldName}
        </if>
         -->
    </sql>

    <!-- 滚动分页条件 -->
    <sql id="page_scroll_condition">
        <if test="pageQuery != null">
            <if test="pageQuery.lastMaxValue != null">
                ID > ${'#'}{pageQuery.lastMaxValue, jdbcType=DECIMAL}
            </if>
            <if test="pageQuery.lastMinValue != null">
                ID &lt; ${'#'}{pageQuery.lastMinValue, jdbcType=DECIMAL}
            </if>
        </if>
    </sql>

    <!-- 查询列表 -->
    <select id="listByQuery" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableClass.tableName}
        <where>
            <include refid="where_query_list_condition"/>
        </where>
    </select>

    <!-- 滚动查询 -->
    <select id="listScrollByQuery" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableClass.tableName}
        <where>
            <include refid="page_scroll_condition"/>
            <include refid="where_query_list_condition"/>
        </where>
    </select>

</mapper>
