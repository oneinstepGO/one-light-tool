package ${mapperInterface.packageName};

import com.oneinstep.light.dao.BaseMapper;
import ${tableClass.fullClassName};

/**
*
* 针对表【${tableClass.tableName}<#if tableClass.remark?has_content>(${tableClass.remark!})</#if>】的数据库操作Mapper
*
* @author ${author!}
* @see ${tableClass.fullClassName}
* @date ${.now?string('yyyy-MM-dd HH:mm')}
*/
public interface ${mapperInterface.fileName} extends BaseMapper<${tableClass.shortClassName}, ReplaceThisWithYourQueryClassType> {

}
