/** 
 * @author ��ƽ�� 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version ����ʱ�䣺2015��2��5�� ����11:27:23 
 * ��˵�� 
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
			compareTables(conn); // �Ƚ����ݿ�
			CompareUtil.writeFile(sb); // д���ļ�
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
	}

	private void compareTables(Connection conn) throws Exception
	{

		// �������ݿ�����

		Map<String, Table> map_product = getTables(conn);

		// String key_table = (String) iter_table.next();
		// Table table_develop = map_product.get(key_table);// ��ÿ������еı�

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
					Column column_develop = (Column) table_child.columns.get(key_column);// ��ÿ������е���
					Column column_product = (Column) table_child.columns.get(key_column);// ���Դ��������л��ͬ����
					if (column_product == null)
					{// �������Ϊ�գ�˵���������ڣ�����������
						CompareUtil.append(table_parent, column_develop, 4, sb);
					} else
					{// ˵�����߶�����
						if (!column_develop.getDataType().equals(column_product.getDataType()))// �ֶ����Ͳ�һ��
							CompareUtil.append(table_parent, column_develop, 5, sb);
						if (column_develop.getNullable().equals(column_product.getNullable()))// �ֶγ��Ȳ�һ��
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
			{// һ���±�
				tableName = rs.getString("table_name");
				table = new Table(tableName);
				Column column = new Column(rs.getString("Column_Name"),
						rs.getString("COLUMN_TYPE"), rs.getString("IS_NULLABLE"), "");
				table.columns.put(column.getColumnName(), column);
				map.put(rs.getString("table_name"), table);
			} else
			{// �Ѵ��ڵı������ֶ�
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
