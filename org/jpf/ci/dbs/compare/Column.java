/** 
 * @author 鍚村钩绂� 
 * E-mail:wupf@asiainfo.com 
 * @version 鍒涘缓鏃堕棿锛�2015骞�1鏈�15鏃� 涓嬪崍4:01:00 
 * 绫昏鏄� 
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
