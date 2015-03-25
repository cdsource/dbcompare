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
import org.hamcrest.core.Is;

public class CompareTable extends AbstractDbCompare
{
	private static final Logger logger = LogManager.getLogger();

	public void DoWork(Connection conn_pdm, Connection conn_develop) throws Exception
	{

		// PDM数据库连接
		Map<String, Table> map_pdm = GetTables(conn_pdm);
		// 开发数据库连接

		Map<String, Table> map_develop = GetTables(conn_develop);
		// 遍历开发库Map
		for (Iterator iter_table = map_develop.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();

			Table table_develop = map_develop.get(key_table);// 获得PDM中的表
			Table table_pdm = map_pdm.get(key_table);// 尝试从比对库中获得同名表
			if (table_pdm == null)
			{ // 如果获得表为空，说明PDM存在，比对库不存在
				CompareUtil.append(table_develop, null, null,2, sb);
			} else
			{ // 表相同，判断字段、字段类型、字段长度
				for (Iterator iter_column = table_develop.columns.keySet().iterator(); iter_column.hasNext();)
				{
					String key_column = (String) iter_column.next();
					Column column_develop = (Column) table_develop.columns.get(key_column);// 获得开发库中的列
					Column column_product = (Column) table_pdm.columns.get(key_column);// 尝试从生产库中获得同名列
					if (column_product == null)
					{// 如果列名为空，说明PDM存在，比对库不存在
						CompareUtil.append(table_develop, column_develop, null,4, sb);
					} else
					{// 说明两者都存在
						if (!column_develop.getDataType().equals(column_product.getDataType()))// 字段类型不一致
							CompareUtil.append(table_develop, column_product,column_develop, 5, sb);
						if (!column_develop.getNullable().equals(column_product.getNullable()))// 字段长度不一致
							CompareUtil.append(table_develop, column_develop, column_product,6, sb);
					}
				}
			}
		}
		// 遍历生产库Map
		for (Iterator iter_table = map_pdm.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();
			if (key_table.equalsIgnoreCase("test_hanyd_sys_data_history"))
			{
				System.out.println(key_table);
			}
			Table table_pdm = map_pdm.get(key_table);// 尝试从生产库中获得同名表
			Table table_develop = map_develop.get(key_table);// 获得开发库中的表
			if (table_develop == null)
			{ // 如果获得表为空，说明PDM存在，比对库不存在
				CompareUtil.append(table_pdm, null, null,1, sb);
			} else
			{ // 表相同，判断字段、字段类型、字段长度
				for (Iterator iter_column = table_pdm.columns
						.keySet().iterator(); iter_column.hasNext();)
				{
					String key_column = (String) iter_column.next();
					Column column_product = (Column) table_pdm.columns.get(key_column);// 获得生产库中的列
					Column column_develop = (Column) table_develop.columns.get(key_column);// 尝试从开发库中获得同名列
					if (column_develop == null)
					{// 如果列名为空，说明生产存在，PDM不存在
						CompareUtil.append(table_pdm, column_product, null,3, sb);
					}
				}
			}
		}

	}

	public Map<String, Table> GetTables(Connection transaction) throws Exception
	{
		String sSql = " select TABLE_NAME,COLUMN_NAME,IS_NULLABLE,COLUMN_TYPE,COLUMN_comment from information_schema.COLUMNS where table_schema=? and table_name not REGEXP '[a-zA-Z]_[0-9]' ";
		if (strExcludeTable !=null && strExcludeTable.length()>0)
		{
			sSql+=" and table_name not like %'"+strExcludeTable+"%' ";
		}
		sSql+=" order By table_name,column_name";
		PreparedStatement pstmt = transaction.prepareStatement(sSql);
		pstmt.setString(1, strDomain);
		logger.debug(sSql);
		System.out.println("Domain="+strDomain);

		ResultSet rs = pstmt.executeQuery();
		Map<String, Table> map = new HashMap<String, Table>();
		String tableName = "";
		Table table = null;
		while (rs.next())
		{
			if (!tableName.equals(rs.getString("table_name").toLowerCase().trim()))
			{// һ���±�
				tableName = rs.getString("table_name").toLowerCase().trim();
				table = new Table(tableName);
				Column column = new Column(rs.getString("Column_Name").toLowerCase().trim(),
						rs.getString("COLUMN_TYPE").toLowerCase().trim(), rs.getString("IS_NULLABLE").toLowerCase().trim(), rs.getString("COLUMN_comment"));
				table.columns.put(column.getColumnName(), column);
				map.put(rs.getString("table_name").toLowerCase().trim(), table);
			} else
			{// �Ѵ��ڵı������ֶ�
				Column column = new Column(rs.getString("Column_Name").toLowerCase().trim(),
						rs.getString("COLUMN_TYPE").toLowerCase().trim(), rs.getString("IS_NULLABLE").toLowerCase().trim(), rs.getString("COLUMN_comment"));
				table.columns.put(column.getColumnName(), column);
			}
		}
		if (null != rs)
			rs.close();
		// transaction.finalize();
		return map;
	}

}
