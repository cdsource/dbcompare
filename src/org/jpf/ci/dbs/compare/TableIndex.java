/** 
* @author 吴平福 
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年1月15日 下午8:47:45 
* 类说明 
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
