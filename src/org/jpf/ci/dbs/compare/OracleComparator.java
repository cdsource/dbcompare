/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 *@version 创建时间：2015年1月15日 下午4:25:25 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Oracle 数据库比较
 * 
 * @author wupingfu
 * @since 2013-2-28
 */
public class OracleComparator
{
	public static String url1 = "jdbc:oracle:thin:@localhost:1521:SIP";
	public static String url2 = "jdbc:oracle:thin:@localhost:1521:SIP";
	public static String USERNAME = "admin";
	public static String PASSWORD = "root";
	// 自动同步表结构,true 时启用
	public static boolean auto_syn = true;
	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) throws Exception
	{
		OracleComparator com = new OracleComparator();
		Connection con1 = com.getConnection(url1, USERNAME, PASSWORD);
		Connection con2 = com.getConnection(url2, USERNAME, PASSWORD);
		logger.info("已连接到两个数据库...将以数据库1为主数据库进行比较");
		String sql = "select TABLE_NAME from USER_TABLES";
		List list1 = com.rs2List(com.getRsBySQL(sql, con1));
		List list2 = com.rs2List(com.getRsBySQL(sql, con2));
		com.compare(list1, list2, con1, con2);
		logger.info("比较完成....");
	}

	private void compare(List list1, List list2, Connection con1, Connection con2) throws Exception
	{
		for (Iterator iterator = list1.iterator(); iterator.hasNext();)
		{
			String name = (String) iterator.next();
			if (list2.contains(name))
			{
				this.tableCompare(name, con1, con2);
			} else
			{
				if (name.indexOf("$") == -1)
				{
					logger.debug("数据库2中，缺少表:" + name);
					if (auto_syn)
					{
						logger.debug("----------------自动创建表" + name + "...");
						logger.debug(createTable(name, con1, con2));
					}
				}
			}
		}
	}

	private void tableCompare(String name, Connection con1, Connection con2) throws Exception
	{
		String sql = "select COLUMN_NAME,DATA_TYPE from USER_TAB_COLUMNS where TABLE_NAME='" + name + "' ";
		Map<String, String> map1 = this.parseColumnList(this.getRsBySQL(sql, con1));
		Map<String, String> map2 = this.parseColumnList(this.getRsBySQL(sql, con2));
		Set set = map1.keySet();
		for (Iterator iterator = set.iterator(); iterator.hasNext();)
		{
			String cname = (String) iterator.next();
			if (map2.containsKey(cname))
			{
				if (!map2.get(cname).equals(map1.get(cname)))
				{
					logger.debug("数据库2的 " + name + " 表中的字段:" + cname + " 与数据库1中数据类型不一致");
					if (auto_syn)
					{
						logger.debug("----------------此项目请手动修改!");
					}
				}
			} else
			{
				logger.debug("数据库2的 " + name + " 表中，缺少字段:" + cname);
				if (auto_syn)
				{
					logger.debug("----------------自动添加字段" + cname + "...");
					logger.debug(appendColumn(name, cname, con1, con2));
				}
			}
		}
	}

	private Map parseColumnList(ResultSet rs1) throws Exception
	{
		Map map = new HashMap();
		while (rs1.next())
		{
			map.put(rs1.getString("COLUMN_NAME"), rs1.getString("DATA_TYPE"));
		}
		return map;
	}

	private List rs2List(ResultSet rs1) throws Exception
	{
		List list = new ArrayList();
		while (rs1.next())
		{
			list.add(rs1.getString("TABLE_NAME"));
		}
		logger.info(list.size());
		return list;
	}

	private ResultSet getRsBySQL(String sql, Connection con1) throws Exception
	{
		Statement stmt = con1.createStatement();
		return stmt.executeQuery(sql);
	}

	public static String DRIVER = "oracle.jdbc.driver.OracleDriver";
	static
	{
		try
		{
			Class.forName(DRIVER).newInstance();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private Connection getConnection(String url, String username, String password)
	{
		try
		{
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private String createTable(String name, Connection con1, Connection con2)
	{
		try
		{
			String sql = "select dbms_metadata.get_ddl('TABLE','" + name + "') as XQL from dual";
			Statement stmt = con1.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				String ddl = rs.getString("XQL");
				Statement stmt2 = con2.createStatement();
				stmt2.execute(ddl);
				return "success";
			} else
			{
				return "fail";
			}
		} catch (SQLException e)
		{
			return "fail";
		}
	}

	private String appendColumn(String name, String colname, Connection con1, Connection con2)
	{
		try
		{
			String sql = "select DATA_TYPE,DATA_LENGTH from USER_TAB_COLUMNS where TABLE_NAME='" + name
					+ "' and COLUMN_NAME='" + colname + "'";
			Statement stmt = con1.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				String type = rs.getString("DATA_TYPE");
				BigDecimal b = rs.getBigDecimal("DATA_LENGTH");
				String sql2 = null;
				if (type.contains("CLOB"))
				{
					sql2 = "alter table " + name + " add ( \"" + colname + "\" " + type + "  NULL )";
				} else
				{
					sql2 = "alter table " + name + " add ( \"" + colname + "\" " + type + "(" + b.intValue()
							+ ") NULL )";
				}
				Statement stmt2 = con2.createStatement();
				stmt2.execute(sql2);
				return "success";
			} else
			{
				return "fail";
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			return "fail";
		}
	}
}
