/** 
 * @author ��ƽ�� 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version ����ʱ�䣺2015��1��15�� ����4:00:30 
 * ��˵�� 
 */

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.ci.dbs.DbUtils;

public class CompareIndex
{
	private static final Logger logger = LogManager.getLogger();

	public static StringBuffer[] sb = { new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer(), new StringBuffer(),
			new StringBuffer() };



	public static void main(String[] args) throws Exception
	{
		CompareIndex cCompareIndex = new CompareIndex();

	}

	public CompareIndex()
	{
		try
		{
			compareTables(); // �Ƚ����ݿ�
			CompareUtil.writeFile(sb); // д���ļ�
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}

		System.out.println("game over");
	}

	public void compareTables() throws Exception
	{
		Connection trans_product = null;
		Connection trans_develop = null;
		try
		{

			// �������ݿ�����
			trans_product = DbInfo.GetInstance().getTransaction_product();
			Map<String, Table> map_product = getTables(trans_product);
			// �������ݿ�����
			trans_develop = DbInfo.GetInstance().getTransaction_develop();
			Map<String, Table> map_develop = getTables(trans_develop);
			// ����������Map
			for (Iterator iter_table = map_develop.keySet().iterator(); iter_table.hasNext();)
			{
				String key_table = (String) iter_table.next();
				// ��ÿ������еı�
				Table table_develop = map_develop.get(key_table);
				// ���Դ��������л��ͬ����
				Table table_product = map_product.get(key_table);
				if (table_product == null)
				{ // �����ñ�Ϊ�գ�˵���������ڣ�����������
					CompareUtil.appendIndex(table_develop, null, null,2, sb);
				} else
				{ // ����ͬ���ж��ֶΡ��ֶ����͡��ֶγ���
					for (Iterator iter_column = table_develop.indexs.keySet().iterator(); iter_column.hasNext();)
					{
						String key_index = (String) iter_column.next();
						//System.out.println(key_index);
						// ��ÿ������е�����
						TableIndex index_develop = (TableIndex) table_develop.indexs.get(key_index);
						// ���Դ��������л��ͬ������
						TableIndex index_product = (TableIndex) table_product.indexs.get(key_index);
						if (index_product == null)
						{// ���������Ϊ�գ�˵���������ڣ�����������
							CompareUtil.appendIndex(table_develop, index_develop,null, 4, sb);
						} else
						{// ˵�����߶�����
							for (Iterator iter_idx_column = index_develop.indexColumns.keySet().iterator(); iter_idx_column
									.hasNext();)
							{
								String key_index_name = (String) iter_idx_column.next();
								//System.out.println(key_index);
								IndexColumn indexcol_devlop = (IndexColumn) index_develop.indexColumns.get(key_index_name);
								IndexColumn indexcol_product = (IndexColumn) index_product.indexColumns.get(key_index_name);
								if (indexcol_product == null)
								{
									//System.out.println("�����ֶβ����ڣ�" + key_index);
									CompareUtil.appendIndex(table_develop, index_develop,indexcol_product, 7, sb);
								} else
								{
									if (indexcol_devlop.getSeqIndex() != indexcol_product.getSeqIndex())
									{
										System.out.println("����λ�ò�ͬ��"+key_table+" "+key_index+" "+key_index_name+" " + indexcol_devlop.getSeqIndex() + " <>"
												+ indexcol_product.getSeqIndex());
										CompareUtil.appendIndex(table_develop, index_develop,indexcol_product, 8, sb);
									}
								}
							}
						}
					}
				}
			}

			// ����������Map

		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		} finally
		{
			DbUtils.DoClear(trans_product);
			DbUtils.DoClear(trans_develop);
		}
	}

	public Map<String, Table> getTables(Connection transaction) throws Exception
	{
		String sSql = " select TABLE_NAME,COLUMN_NAME,INDEX_NAME,SEQ_IN_INDEX from information_schema.STATISTICS  where table_schema ='ad' order By table_name,INDEX_NAME";
		logger.info(sSql);
		Statement stmt = transaction.createStatement();
		ResultSet rs = stmt.executeQuery(sSql);
		Map<String, Table> map = new HashMap<String, Table>();
		String tableName = "";
		Table table = null;
		while (rs.next())
		{
			if (!tableName.equals(rs.getString("table_name")))
			{// һ���±�
				tableName = rs.getString("table_name");
				table = new Table(tableName);
				TableIndex cTableIndex = new TableIndex(rs.getString("INDEX_NAME"));
				cTableIndex.AddIndexColumn(rs.getString("Column_Name"), rs.getInt("SEQ_IN_INDEX"));
				table.indexs.put(cTableIndex.getIndexName(), cTableIndex);
				map.put(rs.getString("table_name"), table);
			} else
			{// �Ѵ��ڵı������ֶ�
				TableIndex cTableIndex = (TableIndex) table.indexs.get(rs.getString("INDEX_NAME"));
				if (cTableIndex == null)
				{
					TableIndex cTableIndex2 = new TableIndex(rs.getString("INDEX_NAME"));
					cTableIndex2.AddIndexColumn(rs.getString("Column_Name"), rs.getInt("SEQ_IN_INDEX"));
					table.indexs.put(cTableIndex2.getIndexName(), cTableIndex2);
				} else
				{
					cTableIndex.AddIndexColumn(rs.getString("Column_Name"), rs.getInt("SEQ_IN_INDEX"));
					table.indexs.put(cTableIndex.getIndexName(), cTableIndex);
				}
			}
		}
		if (null != rs)
			rs.close();
		// transaction.finalize();
		DbUtils.DoClear(transaction);
		return map;
	}

}
