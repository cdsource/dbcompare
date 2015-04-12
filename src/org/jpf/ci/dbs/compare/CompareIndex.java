/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
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
		Map<String, Table> map_pdm = GetTableIndexs(conn_product);
		// 开发数据库连接
		Map<String, Table> map_develop = GetTableIndexs(conn_develop);
		// 遍历开发库Map
		for (Iterator iter_table = map_develop.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();
			// 获得开发库中的表
			Table table_develop = map_develop.get(key_table);
			// 尝试从生产库中获得同名表
			Table table_pdm = map_pdm.get(key_table);
			if (table_pdm != null)
			{
				for (Iterator iter_column = table_develop.indexs.keySet().iterator(); iter_column.hasNext();)
				{
					String key_index = (String) iter_column.next();
					// System.out.println(key_index);
					// 获得开发库中的索引
					TableIndex index_develop = (TableIndex) table_develop.indexs.get(key_index);
					// 尝试从PDM库中获得同名索引
					TableIndex index_pdm = (TableIndex) table_pdm.indexs.get(key_index);
					if (index_pdm == null)
					{// 如果索引名为空，说明开发存在，pdm不存在
						CompareUtil.appendIndex(table_develop, null,index_develop,  2, sb);
					} else
					{// 说明两者都存在

						if (!index_develop.getColNames().equalsIgnoreCase(index_pdm.getColNames()))
						{
							CompareUtil.appendIndex(table_develop, index_pdm,index_develop,  3, sb);
						}
					}
				}
			}
		}

		// 遍历PDMMap
		for (Iterator iter_table = map_pdm.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();
			// 获得开发库中的表
			Table table_develop = map_develop.get(key_table);
			// 尝试从生产库中获得同名表
			Table table_pdm = map_pdm.get(key_table);
			if (table_develop != null)
			{
				for (Iterator iter_column = table_pdm.indexs.keySet().iterator(); iter_column.hasNext();)
				{
					String key_index = (String) iter_column.next();
					// System.out.println(key_index);
					// 获得开发库中的索引
					TableIndex index_develop = (TableIndex) table_develop.indexs.get(key_index);
					// 尝试从PDM库中获得同名索引
					TableIndex index_pdm = (TableIndex) table_pdm.indexs.get(key_index);
					if (index_develop == null)
					{// 如果索引名为空，说明开发存在，pdm不存在
						CompareUtil.appendIndex(table_pdm, index_pdm, null, 1, sb);
					} else
					{// 说明两者都存在
						
						if (!index_develop.getColNames().equalsIgnoreCase(index_pdm.getColNames()))
						{
							CompareUtil.appendIndex(table_develop, index_pdm,index_develop,  3, sb);
						}
					}
				}
			}
		}

	}

	public Map<String, Table> GetTableIndexs(Connection transaction) throws Exception
	{
		String sSql = " select TABLE_NAME,COLUMN_NAME,INDEX_NAME,SEQ_IN_INDEX from information_schema.STATISTICS  where table_schema =?  and table_name not REGEXP '[a-zA-Z]_[0-9]'  ";
		if (strExcludeTable != null && strExcludeTable.length() > 0)
		{
			sSql += " and table_name not like %'" + strExcludeTable + "%' ";
		}
		sSql += " order By table_name,INDEX_NAME,SEQ_IN_INDEX";

		PreparedStatement pstmt = transaction.prepareStatement(sSql);
		pstmt.setString(1, strDomain);
		logger.debug(sSql);
		logger.debug("Domain:" + strDomain);

		ResultSet rs = pstmt.executeQuery();

		Map<String, Table> map = new HashMap<String, Table>();
		String tableName = "";
		Table cTable = null;
		while (rs.next())
		{
			if (!tableName.equals(rs.getString("table_name").toLowerCase().trim()))
			{// 一张新表
				tableName = rs.getString("table_name").toLowerCase().trim();
				cTable = new Table(tableName);
				TableIndex cTableIndex = new TableIndex(rs.getString("INDEX_NAME"));
				cTableIndex.AddColName(rs.getString("COLUMN_NAME"));
				cTable.indexs.put(cTableIndex.getIndexName(), cTableIndex);
				map.put(tableName, cTable);
			} else
			{// 已存在的表，增加字段
				TableIndex cTableIndex = (TableIndex) cTable.indexs.get(rs.getString("INDEX_NAME"));
				if (cTableIndex == null)
				{
					TableIndex cTableIndex2 = new TableIndex(rs.getString("INDEX_NAME"));
					cTableIndex2.AddColName(rs.getString("COLUMN_NAME"));
					cTable.indexs.put(cTableIndex2.getIndexName(), cTableIndex2);
				} else
				{
					cTableIndex.AddColName(rs.getString("COLUMN_NAME"));
					cTable.indexs.put(cTableIndex.getIndexName(), cTableIndex);
				}
			}
		}
		if (null != rs)
			rs.close();

		return map;
	}

	/* (non-Javadoc)
	 * @see org.jpf.ci.dbs.compare.AbstractDbCompare#GetHtmlName()
	 */
	@Override
	String GetHtmlName()
	{
		// TODO Auto-generated method stub
		return "compare_index.html";
	}

}
