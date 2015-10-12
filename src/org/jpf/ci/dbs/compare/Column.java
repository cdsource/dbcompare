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
	private int dataLength=0;
    public int getDataLength() {
		return dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	private String ColumnDefault="";
    
    private String Extra="";
    
    private int ordinal_position;
    
    private String CHARACTER_SET_NAME="";
    private String COLLATION_NAME="";
	public String getCOLLATION_NAME()
	{
		return COLLATION_NAME;
	}

	public void setCOLLATION_NAME(String cOLLATION_NAME)
	{
		COLLATION_NAME = cOLLATION_NAME;
	}

	public String getCHARACTER_SET_NAME()
	{
		return CHARACTER_SET_NAME;
	}

	public void setCHARACTER_SET_NAME(String cHARACTER_SET_NAME)
	{
		CHARACTER_SET_NAME = cHARACTER_SET_NAME;
	}

	public int getOrdinal_position()
	{
		return ordinal_position;
	}

	public void setOrdinal_position(int ordinal_position)
	{
		this.ordinal_position = ordinal_position;
	}

	public String getExtra()
	{
		return Extra;
	}

	public void setExtra(String extra)
	{
		Extra = extra;
	}

	public String getNullable()
	{
		return Nullable;
	}

	public String getColumnDefault()
	{
		return ColumnDefault;
	}

	public void setColumnDefault(final String ColumnDefault)
	{
		this.ColumnDefault = ColumnDefault;
	}

	public void setNullable(String nullable)
	{
		Nullable = nullable;
	}


	public Column(String columnName, String dataType, String Nullable,final String ColumnDefault)
	{
		this.columnName = columnName;
		this.dataType = dataType;
		this.Nullable = Nullable;
		this.ColumnDefault=ColumnDefault;
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
