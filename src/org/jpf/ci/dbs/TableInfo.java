/** 
* @author 吴平福 
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年3月2日 下午3:56:57 
* 类说明 
*/ 

package org.jpf.ci.dbs;

/**
 * 
 */
public class TableInfo
{
	String strParentTable;
	String strSubTable;
	String strDbName;
	
	/**
	 * 
	 */
	public TableInfo(String strDbName, String  strParentTable, String strSubTable)
	{
		// TODO Auto-generated constructor stub
		this.strParentTable=strParentTable;
		this.strDbName=strDbName;
		this.strSubTable=strSubTable;
	}

}
