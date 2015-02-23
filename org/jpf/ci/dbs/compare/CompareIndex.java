/** 
 * @author ��ƽ�� 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version ����ʱ�䣺2015��1��15�� ����4:00:30 
 * ��˵�� 
 */

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompareIndex extends AbstractDbCompare
{
	private static final Logger logger = LogManager.getLogger();


	public void DoWork(Connection conn_product, Connection conn_develop) throws Exception
	{

			// �������ݿ�����
			Map<String, Table> map_product = GetTableIndexs(conn_product);
			// �������ݿ�����
			Map<String, Table> map_develop = GetTableIndexs(conn_develop);
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
					CompareUtil.appendIndex(table_develop, null, null, 2, sb);
				} else
				{ // ����ͬ���ж��ֶΡ��ֶ����͡��ֶγ���
					for (Iterator iter_column = table_develop.indexs.keySet().iterator(); iter_column.hasNext();)
					{
						String key_index = (String) iter_column.next();
						// System.out.println(key_index);
						// ��ÿ������е�����
						TableIndex index_develop = (TableIndex) table_develop.indexs.get(key_index);
						// ���Դ��������л��ͬ������
						TableIndex index_product = (TableIndex) table_product.indexs.get(key_index);
						if (index_product == null)
						{// ���������Ϊ�գ�˵���������ڣ�����������
							CompareUtil.appendIndex(table_develop, index_develop, null, 4, sb);
						} else
						{// ˵�����߶�����
							for (Iterator iter_idx_column = index_develop.indexColumns.keySet().iterator(); iter_idx_column
									.hasNext();)
							{
								String key_index_name = (String) iter_idx_column.next();
								// System.out.println(key_index);
								IndexColumn indexcol_devlop = (IndexColumn) index_develop.indexColumns
										.get(key_index_name);
								IndexColumn indexcol_product = (IndexColumn) index_product.indexColumns
										.get(key_index_name);
								if (indexcol_product == null)
								{
									// System.out.println("�����ֶβ����ڣ�" +
									// key_index);
									CompareUtil.appendIndex(table_develop, index_develop, indexcol_devlop, 7, sb);
								} else
								{
									if (indexcol_devlop.getSeqIndex() != indexcol_product.getSeqIndex())
									{
										System.out.println("����λ�ò�ͬ��" + key_table + " " + key_index + " "
												+ key_index_name + " " + indexcol_devlop.getSeqIndex() + " <>"
												+ indexcol_product.getSeqIndex());
										CompareUtil.appendIndex(table_develop, index_develop, indexcol_product, 8, sb);
									}
								}
							}
						}
					}
				}
			}

			// ����������Map


	}

	public Map<String, Table> GetTableIndexs(Connection transaction) throws Exception
	{
		String sSql = " select TABLE_NAME,COLUMN_NAME,INDEX_NAME,SEQ_IN_INDEX from information_schema.STATISTICS  where table_schema =?  and table_name not REGEXP '[a-zA-Z]_[0-9]'  order By table_name,INDEX_NAME";
		
		PreparedStatement pstmt = transaction.prepareStatement(sSql);
		pstmt.setString(1, strDomain);
		logger.info(sSql);
		logger.info(strDomain);
		ResultSet rs = pstmt.executeQuery();
		
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

		return map;
	}




}
