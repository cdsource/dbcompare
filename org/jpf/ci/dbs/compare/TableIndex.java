/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2015年1月15日 下午8:47:45 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

import java.util.HashMap;
import java.util.Iterator;

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

	/*
	 * 索引比较
	 
	private void IndexCompare(Table table_develop,TableIndex cTableIndex2,StringBuffer[] sb)
	{
		for (Iterator iter_column = this.indexColumns.keySet().iterator(); iter_column.hasNext();)
		{
			String key_index = (String) iter_column.next();
			System.out.println(key_index);
			// 获得开发库中的索引
			IndexColumn index_develop = (IndexColumn) this.indexColumns.get(key_index);
			// 尝试从生产库中获得同名索引
			IndexColumn index_product = (IndexColumn) cTableIndex2.indexColumns.get(key_index);
			if (index_product == null)
			{// 如果索引名为空，说明开发存在，生产不存在
				CompareUtil.appendIndex(table_develop, index_develop, 4,sb);
			} else
			{
				
			}
	}
	*/
}
