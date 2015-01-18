/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2015年1月16日 下午1:55:11 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

/**
 * 
 */
public class IndexColumn
{
	
	public IndexColumn(String columnName, int seqIndex)
	{
		this.columnName=columnName;
		this.seqIndex=seqIndex;
	}
	private String columnName;
	private int seqIndex;
	
	public int getSeqIndex()
	{
		return seqIndex;
	}
	public void setSeqIndex(int seqIndex)
	{
		this.seqIndex = seqIndex;
	}
	public String getColumnName()
	{
		return columnName;
	}
	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}
}
