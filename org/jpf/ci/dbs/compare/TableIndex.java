/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2015��1��15�� ����8:47:45 
* ��˵�� 
*/ 

package org.jpf.ci.dbs.compare;

import java.util.HashMap;

/**
 * 
 */
public class TableIndex
{
    //���������
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
