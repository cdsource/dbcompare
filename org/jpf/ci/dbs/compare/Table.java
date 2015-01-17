/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年1月15日 下午4:01:15 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.util.HashMap;

public class Table
{
	public String tableName;
	
	//字段列
	public HashMap columns = new HashMap();
    //索引，组件
	public HashMap indexs = new HashMap();
	
	public Table(String tableName)
	{
		this.tableName = tableName;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public HashMap getColumns()
	{
		return columns;
	}

	public void setColumns(HashMap columns)
	{
		this.columns = columns;
	}
}
