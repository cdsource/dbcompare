/** 
 * @author ��ƽ�� 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version ����ʱ�䣺2015��1��15�� ����4:13:04 
 * ��˵�� 
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
 * Oracle ���ݿ�Ƚ�
 * 
 * @author Eric zhou
 * @since 2013-2-28
 */
public class MysqlComparator
{
	public static String url1 = "jdbc:oracle:thin:@localhost:1521:SIP";
	public static String url2 = "jdbc:oracle:thin:@localhost:1521:SIP";
	public static String USERNAME = "billing";
	public static String PASSWORD = "billing";
	public static boolean auto_syn = false;// �Զ�ͬ����ṹ,true ʱ����
	
	private static final Logger logger = LogManager.getLogger();
	private String URL1 = "jdbc:mysql://10.10.12.153:4306/";
	private String dbuser1 = "billing";
	private String dbpass1 = "billing";

	// for id
	private String URL2="jdbc:mysql://10.10.12.150:4306/";
	private String dbuser2 = "billing";
	private String dbpass2 = "billing";
	
	
	private Connection GetDestConn( )throws Exception
	{
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver).newInstance();
		return  DriverManager.getConnection(URL1, dbuser1, dbpass1);
	}
	private Connection GetCompareConn( )throws Exception
	{
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver).newInstance();
		return  DriverManager.getConnection(URL2, dbuser2, dbpass2);
	}
	public static void main(String[] args) throws Exception
	{
		MysqlComparator com = new MysqlComparator();
		Connection con1 = com.GetDestConn();
		Connection con2 = com.GetCompareConn();
		System.out.println("�����ӵ��������ݿ�...�������ݿ�1Ϊ�����ݿ���бȽ�");
		String sql = "select * from information_schema.TABLES where table_schema like '%d' order by table_schema,table_name";
		List list1 = com.Rs2List(com.getRsBySQL(sql, con1));
		List list2 = com.Rs2List(com.getRsBySQL(sql, con2));
		com.compare(list1, list2, con1, con2);
		System.out.println("�Ƚ����....");
	}

	private void compare(List list1, List list2, Connection con1, Connection con2) throws Exception
	{
		for (Iterator iterator = list1.iterator(); iterator.hasNext();)
		{
			String name = (String) iterator.next();
			if (list2.contains(name))
			{
				this.TableCompare(name, con1, con2);
			} else
			{
				if (name.indexOf("$") == -1)
				{
					System.out.println(URL2+"�У�ȱ�ٱ�:" + name);
					if (auto_syn)
					{
						System.out.print("----------------�Զ�������" + name + "...");
						//System.out.println(createTable(name, con1, con2));
					}
				}
			}
		}
	}

	private void IndexCompare(String name, Connection con1, Connection con2) throws Exception
	{
		String sql = "select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE from information_schema.COLUMNS where TABLE_NAME='" + name + "' ";
		Map<String, String> map1 = this.parseColumnList(this.getRsBySQL(sql, con1));
		//logger.info("source:"+map1.size());
		Map<String, String> map2 = this.parseColumnList(this.getRsBySQL(sql, con2));
		//logger.info("desc:"+map2.size());
		Set set = map1.keySet();
		for (Iterator iterator = set.iterator(); iterator.hasNext();)
		{
			String cname = (String) iterator.next();
			if (map2.containsKey(cname))
			{
				if (!map2.get(cname).equals(map1.get(cname)))
				{
					System.out.println(URL2+"�� " + name + " ���е��ֶ�:" + cname + ":"+map2.get(cname)+" ��"+URL1+" "+map1.get(cname)+"���������Ͳ�һ��");
					if (auto_syn)
					{
						System.out.println("----------------����Ŀ���ֶ��޸�!");
					}
				}
			} else
			{
				System.out.println(URL2+"�� " + name + " ���У�ȱ���ֶ�:" + cname);
				if (auto_syn)
				{
					System.out.print("----------------�Զ�����ֶ�" + cname + "...");
					//System.out.println(appendColumn(name, cname, con1, con2));
				}
			}
		}
	}
	
	private void TableCompare(String name, Connection con1, Connection con2) throws Exception
	{
		String sql = "select COLUMN_NAME,COLUMN_TYPE from information_schema.COLUMNS where TABLE_NAME='" + name + "' ";
		Map<String, String> map1 = this.parseColumnList(this.getRsBySQL(sql, con1));
		//logger.info("source:"+map1.size());
		Map<String, String> map2 = this.parseColumnList(this.getRsBySQL(sql, con2));
		//logger.info("desc:"+map2.size());
		Set set = map1.keySet();
		for (Iterator iterator = set.iterator(); iterator.hasNext();)
		{
			String cname = (String) iterator.next();
			if (map2.containsKey(cname))
			{
				if (!map2.get(cname).equals(map1.get(cname)))
				{
					System.out.println(URL2+"�� " + name + " ���е��ֶ�:" + cname + ":"+map2.get(cname)+" ��"+URL1+" "+map1.get(cname)+"���������Ͳ�һ��");
					if (auto_syn)
					{
						System.out.println("----------------����Ŀ���ֶ��޸�!");
					}
				}
			} else
			{
				System.out.println(URL2+"�� " + name + " ���У�ȱ���ֶ�:" + cname);
				if (auto_syn)
				{
					System.out.print("----------------�Զ�����ֶ�" + cname + "...");
					//System.out.println(appendColumn(name, cname, con1, con2));
				}
			}
		}
	}

	private Map parseColumnList(ResultSet rs1) throws Exception
	{
		Map map = new HashMap();
		while (rs1.next())
		{
			map.put(rs1.getString("COLUMN_NAME"), rs1.getString("COLUMN_TYPE"));
		}
		return map;
	}

	private Map parseIndexList(ResultSet rs1) throws Exception
	{
		Map map = new HashMap();
		while (rs1.next())
		{
			map.put(rs1.getString("COLUMN_NAME"), rs1.getString("COLUMN_TYPE"));
		}
		return map;
	}
	
	private List Rs2List(ResultSet rs1) throws Exception
	{
		List list = new ArrayList();
		while (rs1.next())
		{
			list.add(rs1.getString("TABLE_NAME"));
		}
		System.out.println(list.size());
		return list;
	}

	private ResultSet getRsBySQL(String sql, Connection con1) throws Exception
	{
		//logger.info(sql);
		Statement stmt = con1.createStatement();
		return stmt.executeQuery(sql);
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
