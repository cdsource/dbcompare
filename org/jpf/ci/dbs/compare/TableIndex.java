/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2015年1月15日 下午8:47:45 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

import java.util.HashMap;

/**
 * 
 */
public class TableIndex
{
    //索引，组件
	public HashMap indexColumns = new HashMap();
	
	public String getIndexName()
	{
		return indexName;
	}
	public void setIndexName(String indexName)
	{
		this.indexName = indexName;
	}


	private String indexName;

	/**
	 * 
	 */
	public TableIndex( String indexName)
	{
		// TODO Auto-generated constructor stub
		this.indexName = indexName;
	}
	
	public void AddIndexColumn(String columnName, int seqIndex)
	{
		IndexColumn cIndexColumn = new IndexColumn(columnName,seqIndex);
		indexColumns.put(columnName, cIndexColumn);

	}
}
