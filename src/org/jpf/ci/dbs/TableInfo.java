/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo.com 
* @version ����ʱ�䣺2015��3��2�� ����3:56:57 
* ��˵�� 
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
