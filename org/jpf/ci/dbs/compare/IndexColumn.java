/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2015��1��16�� ����1:55:11 
* ��˵�� 
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
