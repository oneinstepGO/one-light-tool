package com.oneinstep.light.dao.mybatis.typehandler; /**
 * Copyright 2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.time.OffsetTime;

/**
 * @author Tomas Rohovsky
 */
public class OffsetTimeTypeHandler extends BaseTypeHandler<OffsetTime> {

    private static OffsetTime getOffsetTime(Time time) {
        if (time != null) {
            return time.toLocalTime().atOffset(OffsetTime.now().getOffset());
        }
        return null;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, OffsetTime parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setTime(i, Time.valueOf(parameter.toLocalTime()));
    }

    @Override
    public OffsetTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Time time = rs.getTime(columnName);
        return getOffsetTime(time);
    }

    @Override
    public OffsetTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Time time = rs.getTime(columnIndex);
        return getOffsetTime(time);
    }

    @Override
    public OffsetTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Time time = cs.getTime(columnIndex);
        return getOffsetTime(time);
    }
}
