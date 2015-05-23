/** 
* @author 吴平福 
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年5月20日 下午1:55:23 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

/**
 * 
 */
public class ExecSqlInfo
{
	private String strSql;
	private int iType;
	private String strTable;
	public String getStrTable()
	{
		return strTable;
	}
	public void setStrTable(String strTable)
	{
		this.strTable = strTable;
	}
	public String getStrSql()
	{
		return strSql;
	}
	public void setStrSql(String strSql)
	{
		this.strSql = strSql;
	}
	public int getiType()
	{
		return iType;
	}
	public void setiType(int iType)
	{
		this.iType = iType;
	}
	/**
	 * 
	 */
	public ExecSqlInfo()
	{
		// TODO Auto-generated constructor stub
	}

}
