/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年1月15日 下午4:01:15 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.util.LinkedHashMap;

public class Table
{
	public String tableName;
	
	//字段列
	public LinkedHashMap  columns = new LinkedHashMap();
    //索引，组件
	public LinkedHashMap indexs = new LinkedHashMap();
	
	public String table_type;
	public String getTable_type()
	{
		return table_type;
	}

	public void setTable_type(String table_type)
	{
		this.table_type = table_type;
	}

	public Table(String tableName,String strTableType)
	{
		this.tableName = tableName;
		this.table_type=strTableType;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public LinkedHashMap getColumns()
	{
		return columns;
	}

	public void setColumns(LinkedHashMap columns)
	{
		this.columns = columns;
	}
}
