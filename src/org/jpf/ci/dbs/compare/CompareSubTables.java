/** 
 * @author ��ƽ�� 
 * E-mail:wupf@asiainfo.com 
 * @version ����ʱ�䣺2015��2��8�� ����10:09:38 
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

/**
 * 
 */
public class CompareSubTables
{
	private static final Logger logger = LogManager.getLogger();

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

	public void DoCheck(Connection conn, String strDomain) throws Exception
	{
		try
		{
			compareTables(conn, strDomain); // �Ƚ����ݿ�
			CompareUtil.writeFile(sb); // д���ļ�
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
	}

	private void compareTables(Connection conn, String strDomain) throws Exception
	{

		// �������ݿ�����

		Map<String, Table> map_product = getTables(conn, strDomain);

		String sSql = " SELECT t1.base_name FROM zd.sys_partition_rule t1,information_schema.TABLES t2 where t1.base_name=t2.table_name and t2.table_schema =?";
		PreparedStatement pstmt1 = conn.prepareStatement(sSql);
		logger.debug(sSql);
		pstmt1.setString(1, strDomain);
		ResultSet rs = pstmt1.executeQuery();
		while (rs.next())
		{
			String strParentTableName = rs.getString("base_name");
			System.out.println("checking parent table:" + strParentTableName );
			String strFindSubSql = "SELECT  * FROM information_schema.TABLES WHERE table_schema =? and table_name REGEXP '^"
					+ strParentTableName.toLowerCase() + "_[0-9]'";
			PreparedStatement pstmt = conn.prepareStatement(strFindSubSql);
			pstmt.setString(1, strDomain);
			logger.debug(strFindSubSql);
			ResultSet rs2 = pstmt.executeQuery();
			while (rs2.next())
			{
				String strChildTableName = rs2.getString("TABLE_NAME");
				Table table_parent = map_product.get(strParentTableName);
				if (null == table_parent)
				{
					System.out.println("miss parent table:" + strParentTableName + "   " + strChildTableName);
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
						CompareUtil.append(table_parent, column_develop, null,4, sb);
					} else
					{// ˵�����߶�����
						if (!column_develop.getDataType().equals(column_product.getDataType()))// �ֶ����Ͳ�һ��
							CompareUtil.append(table_parent, column_develop, column_product,5, sb);
						if (column_develop.getNullable().equals(column_product.getNullable()))// �ֶγ��Ȳ�һ��
							CompareUtil.append(table_parent, column_develop, column_product,6, sb);
					}
				}
			}
		}

	}

	public Map<String, Table> getTables(Connection transaction, String strDomain) throws Exception
	{
		String sSql = " select TABLE_NAME,COLUMN_NAME,IS_NULLABLE,COLUMN_TYPE from information_schema.COLUMNS where table_schema =? order By table_name,column_name";
		PreparedStatement pstmt = transaction.prepareStatement(sSql);
		pstmt.setString(1, strDomain);
		logger.debug(sSql);
		System.out.println("Domain:"+strDomain);

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
