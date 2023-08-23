package ${domain.packageName};

import lombok.Data;

<#list tableClass.importList as fieldType>${"\n"}import ${fieldType};</#list>

/**
*
* ${tableClass.remark!}
* ${tableClass.tableName}
*
* @author ${author!}
* @date ${.now?string('yyyy-MM-dd HH:mm')}
*/
@Data
public class ${tableClass.shortClassName} {

<#list tableClass.allFields as field>
    /**
    * ${field.remark!}
    */
    private ${field.shortTypeName} ${field.fieldName};
</#list>

}
