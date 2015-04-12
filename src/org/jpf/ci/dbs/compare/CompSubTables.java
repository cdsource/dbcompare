/** 
* @author 吴平福 
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年3月22日 下午11:09:55 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 */
public class CompSubTables
{

	/**
	 * dbcompare -sub billing@billing://10.1.228.11:4306/sonar
	 * select * from information_schema.tables where table_schema='wd' order by TABLE_NAME;
	 */
	public CompSubTables()
	{
		// TODO Auto-generated constructor stub
		try
		{
			compareTables(conn, strDomain); // 比较数据库
			CompareUtil.writeFile(sb); // 写入文件
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
	}
	private void compareTables(Connection conn, String strDomain) throws Exception
	{

		// 生产数据库连接

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
					Column column_develop = (Column) table_child.columns.get(key_column);// 获得开发库中的列
					Column column_product = (Column) table_child.columns.get(key_column);// 尝试从生产库中获得同名列
					if (column_product == null)
					{// 如果列名为空，说明开发存在，生产不存在
						CompareUtil.append(table_parent, column_develop, null,4, sb);
					} else
					{// 说明两者都存在
						if (!column_develop.getDataType().equals(column_product.getDataType()))// 字段类型不一致
							CompareUtil.append(table_parent, column_develop, column_product,5, sb);
						if (column_develop.getNullable().equals(column_product.getNullable()))// 字段长度不一致
							CompareUtil.append(table_parent, column_develop, column_product,6, sb);
					}
				}
			}
		}

	}
	/**
	 * @param args
	 * 被测试类名：TODO
	 * 被测试接口名:TODO
	 * 测试场景：TODO
	 * 前置参数：TODO
	 * 入参：
	 * 校验值：
	 * 测试备注：
	 * update 2015年3月22日
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
