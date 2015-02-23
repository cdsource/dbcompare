/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2015年1月15日 下午4:00:30 
 * 类说明 
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

			// 生产数据库连接
			Map<String, Table> map_product = GetTableIndexs(conn_product);
			// 开发数据库连接
			Map<String, Table> map_develop = GetTableIndexs(conn_develop);
			// 遍历开发库Map
			for (Iterator iter_table = map_develop.keySet().iterator(); iter_table.hasNext();)
			{
				String key_table = (String) iter_table.next();
				// 获得开发库中的表
				Table table_develop = map_develop.get(key_table);
				// 尝试从生产库中获得同名表
				Table table_product = map_product.get(key_table);
				if (table_product == null)
				{ // 如果获得表为空，说明开发存在，生产不存在
					CompareUtil.appendIndex(table_develop, null, null, 2, sb);
				} else
				{ // 表相同，判断字段、字段类型、字段长度
					for (Iterator iter_column = table_develop.indexs.keySet().iterator(); iter_column.hasNext();)
					{
						String key_index = (String) iter_column.next();
						// System.out.println(key_index);
						// 获得开发库中的索引
						TableIndex index_develop = (TableIndex) table_develop.indexs.get(key_index);
						// 尝试从生产库中获得同名索引
						TableIndex index_product = (TableIndex) table_product.indexs.get(key_index);
						if (index_product == null)
						{// 如果索引名为空，说明开发存在，生产不存在
							CompareUtil.appendIndex(table_develop, index_develop, null, 4, sb);
						} else
						{// 说明两者都存在
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
									// System.out.println("索引字段不存在：" +
									// key_index);
									CompareUtil.appendIndex(table_develop, index_develop, indexcol_devlop, 7, sb);
								} else
								{
									if (indexcol_devlop.getSeqIndex() != indexcol_product.getSeqIndex())
									{
										System.out.println("索引位置不同：" + key_table + " " + key_index + " "
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

			// 遍历生产库Map


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
			{// 一张新表
				tableName = rs.getString("table_name");
				table = new Table(tableName);
				TableIndex cTableIndex = new TableIndex(rs.getString("INDEX_NAME"));
				cTableIndex.AddIndexColumn(rs.getString("Column_Name"), rs.getInt("SEQ_IN_INDEX"));
				table.indexs.put(cTableIndex.getIndexName(), cTableIndex);
				map.put(rs.getString("table_name"), table);
			} else
			{// 已存在的表，增加字段
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
