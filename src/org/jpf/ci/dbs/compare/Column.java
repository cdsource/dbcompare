/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年2月8日 下午10:09:38 
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
	
    private String comment;
    
	public String getNullable()
	{
		return Nullable;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public void setNullable(String nullable)
	{
		Nullable = nullable;
	}


	public Column(String columnName, String dataType, String Nullable,String columncomment)
	{
		this.columnName = columnName;
		this.dataType = dataType;
		this.Nullable = Nullable;
		this.comment=columncomment;
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
