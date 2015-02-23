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

public class CompareTable extends AbstractDbCompare
{
	private static final Logger logger = LogManager.getLogger();

	public StringBuffer[] sb = { new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer() };

	public void DoWork(Connection conn_product, Connection conn_develop) throws Exception
	{

		// 生产数据库连接

		Map<String, Table> map_product = GetTables(conn_product);
		// 开发数据库连接

		Map<String, Table> map_develop = GetTables(conn_develop);
		// 遍历开发库Map
		for (Iterator iter_table = map_develop.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();
			Table table_develop = map_develop.get(key_table);// 获得开发库中的表
			Table table_product = map_product.get(key_table);// 尝试从生产库中获得同名表
			if (table_product == null)
			{ // 如果获得表为空，说明开发存在，生产不存在
				CompareUtil.append(table_develop, null, 2, sb);
			} else
			{ // 表相同，判断字段、字段类型、字段长度
				for (Iterator iter_column = table_develop.columns.keySet().iterator(); iter_column.hasNext();)
				{
					String key_column = (String) iter_column.next();
					Column column_develop = (Column) table_develop.columns.get(key_column);// 获得开发库中的列
					Column column_product = (Column) table_product.columns.get(key_column);// 尝试从生产库中获得同名列
					if (column_product == null)
					{// 如果列名为空，说明开发存在，生产不存在
						CompareUtil.append(table_develop, column_develop, 4, sb);
					} else
					{// 说明两者都存在
						if (!column_develop.getDataType().equals(column_product.getDataType()))// 字段类型不一致
							CompareUtil.append(table_develop, column_develop, 5, sb);
						if (!column_develop.getNullable().equals(column_product.getNullable()))// 字段长度不一致
							CompareUtil.append(table_develop, column_develop, 6, sb);

						// if
						// (!column_develop.getComment().equals(column_product.getComment()))//
						// 字段注释不一致
						// CompareUtil.append(table_develop, column_develop,
						// 7,sb);
					}
				}
			}
		}
		// 遍历生产库Map
		for (Iterator iter_table = map_product.keySet().iterator(); iter_table
				.hasNext();)
		{
			String key_table = (String) iter_table.next();
			Table table_product = map_product.get(key_table);// 尝试从生产库中获得同名表
			Table table_develop = map_develop.get(key_table);// 获得开发库中的表
			if (table_develop == null)
			{ // 如果获得表为空，说明开发存在，生产不存在
				CompareUtil.append(table_product, null, 1, sb);
			} else
			{ // 表相同，判断字段、字段类型、字段长度
				for (Iterator iter_column = table_product.columns
						.keySet().iterator(); iter_column.hasNext();)
				{
					String key_column = (String) iter_column.next();
					Column column_product = (Column) table_product.columns.get(key_column);// 获得生产库中的列
					Column column_develop = (Column) table_develop.columns.get(key_column);// 尝试从开发库中获得同名列
					if (column_develop == null)
					{// 如果列名为空，说明生产存在，开发不存在
						CompareUtil.append(table_product, column_product, 3, sb);
					}
				}
			}
		}

	}

	public Map<String, Table> GetTables(Connection transaction) throws Exception
	{
		String sSql = " select TABLE_NAME,COLUMN_NAME,IS_NULLABLE,COLUMN_TYPE,COLUMN_comment from information_schema.COLUMNS where table_schema=? and table_name not REGEXP '[a-zA-Z]_[0-9]' order By table_name,column_name";
		PreparedStatement pstmt = transaction.prepareStatement(sSql);
		pstmt.setString(1, strDomain);
		logger.info(sSql);
		logger.info("strDomain="+strDomain);

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
				Column column = new Column(rs.getString("Column_Name"),
						rs.getString("COLUMN_TYPE"), rs.getString("IS_NULLABLE"), rs.getString("COLUMN_comment"));
				table.columns.put(column.getColumnName(), column);
				map.put(rs.getString("table_name"), table);
			} else
			{// �Ѵ��ڵı������ֶ�
				Column column = new Column(rs.getString("Column_Name"),
						rs.getString("COLUMN_TYPE"), rs.getString("IS_NULLABLE"), rs.getString("COLUMN_comment"));
				table.columns.put(column.getColumnName(), column);
			}
		}
		if (null != rs)
			rs.close();
		// transaction.finalize();
		return map;
	}

}
