/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo.com 
* @version ����ʱ�䣺2015��1��15�� ����8:47:45 
* ��˵�� 
*/ 

package org.jpf.ci.dbs.compare;

/**
 * 
 */
public class TableIndex
{

	private String indexName;
	private String colNames="";
	
	public String getIndexName()
	{
		return indexName;
	}

	public void setIndexName(String indexName)
	{
		this.indexName = indexName;
	}

	/**
	 * 
	 */
	public TableIndex( String indexName)
	{
		// TODO Auto-generated constructor stub
		this.indexName = indexName;
	}
	
	public void AddColName(String columnName)
	{
		colNames+=columnName+",";

	}

	public String getColNames()
	{
		return colNames;
	}

	public void setColNames(String colNames)
	{
		this.colNames = colNames;
	}
}
