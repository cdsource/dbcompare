/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年1月15日 下午4:01:00 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

/**
 * 
 */
public class Column
{
	private String columnName;
	private String dataType;
	//ORACLE
	//private int length;
	//MYSQL
    private String Nullable;
	
	public String getNullable()
	{
		return Nullable;
	}

	public void setNullable(String nullable)
	{
		Nullable = nullable;
	}


	public Column(String columnName, String dataType, String Nullable)
	{
		this.columnName = columnName;
		this.dataType = dataType;
		this.Nullable = Nullable;
	}
	public String getColumnName()
	{
		return columnName;
	}

	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	public String getDataType()
	{
		return dataType;
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}


}
