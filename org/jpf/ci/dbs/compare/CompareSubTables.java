/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2015年2月5日 下午11:27:23 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.ci.dbs.DbUtils;

/**
 * 
 */
public class CompareSubTables
{
	private static final Logger logger = LogManager.getLogger();
	private String sSql = " select TABLE_NAME,COLUMN_NAME,IS_NULLABLE,COLUMN_TYPE from information_schema.COLUMNS where table_schema ='ad' order By table_name,column_name";

	public StringBuffer[] sb = { new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer(), new StringBuffer(),
			new StringBuffer() };

	public static void main(String[] args) throws Exception
	{
		CompareSubTables cCompareSubTables = new CompareSubTables();

	}

	public CompareSubTables()
	{
	}

	public void DoCheck(Connection conn) throws Exception
	{
		try
		{
			compareTables(conn); // 比较数据库
			CompareUtil.writeFile(sb); // 写入文件
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
	}

	private void compareTables(Connection conn) throws Exception
	{

		// 生产数据库连接

		Map<String, Table> map_product = getTables(conn);

		// String key_table = (String) iter_table.next();
		// Table table_develop = map_product.get(key_table);// 获得开发库中的表

		Statement stmt = conn.createStatement();
		sSql = "SELECT * FROM zd.sys_partition_rule";
		logger.info(sSql);
		ResultSet rs = stmt.executeQuery(sSql);
		while (rs.next())
		{
			String strParentTableName = rs.getString("base_name");
			String strFindSubSql = "SELECT  * FROM information_schema.TABLES WHERE table_name REGEXP '^"
					+ strParentTableName.toLowerCase() + "_[0-9]'";
			Statement stmt2 = conn.createStatement();
			logger.info(strFindSubSql);
			ResultSet rs2 = stmt2.executeQuery(strFindSubSql);
			while (rs2.next())
			{
				String strChildTableName = rs2.getString("TABLE_NAME");
				Table table_parent = map_product.get(strParentTableName);
				if (null == table_parent)
				{
					logger.error("miss:" + strParentTableName);
					continue;
				}
				Table table_child = map_product.get(strChildTableName);

				for (Iterator iter_column = table_parent.columns.keySet().iterator(); iter_column.hasNext();)
				{
					String key_column = (String) iter_column.next();
					Column column_develop = (Column) table_child.columns.get(key_column);// 获得开发库中的列
					Column column_product = (Column) table_child.columns.get(key_column);// 尝试从生产库中获得同名列
					if (column_product == null)
					{// 如果列名为空，说明开发存在，生产不存在
						CompareUtil.append(table_parent, column_develop, 4, sb);
					} else
					{// 说明两者都存在
						if (!column_develop.getDataType().equals(column_product.getDataType()))// 字段类型不一致
							CompareUtil.append(table_parent, column_develop, 5, sb);
						if (column_develop.getNullable().equals(column_product.getNullable()))// 字段长度不一致
							CompareUtil.append(table_parent, column_develop, 6, sb);
					}
				}
			}
		}

	}

	public Map<String, Table> getTables(Connection transaction) throws Exception
	{
		Statement stmt = transaction.createStatement();
		logger.info(sSql);
		ResultSet rs = stmt.executeQuery(sSql);
		Map<String, Table> map = new HashMap<String, Table>();
		String tableName = "";
		Table table = null;
		while (rs.next())
		{
			if (!tableName.equals(rs.getString("table_name")))
			{// 一张新表
				tableName = rs.getString("table_name");
				table = new Table(tableName);
				Column column = new Column(rs.getString("Column_Name"),
						rs.getString("COLUMN_TYPE"), rs.getString("IS_NULLABLE"), "");
				table.columns.put(column.getColumnName(), column);
				map.put(rs.getString("table_name"), table);
			} else
			{// 已存在的表，增加字段
				Column column = new Column(rs.getString("Column_Name"),
						rs.getString("COLUMN_TYPE"), rs.getString("IS_NULLABLE"), "");
				table.columns.put(column.getColumnName(), column);
			}
		}
		if (null != rs)
			rs.close();
		// transaction.finalize();
		return map;
	}
}
