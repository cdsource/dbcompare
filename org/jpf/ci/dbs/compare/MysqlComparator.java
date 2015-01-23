/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2015年1月15日 下午4:13:04 
 * 类说明 
 * 更改记录：增加自动更改类型方法
 */

package org.jpf.ci.dbs.compare;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;


/**
 * Oracle 数据库比较
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
	public static boolean auto_syn = false;// 自动同步表结构,true 时启用
	
	private static final Logger logger = LogManager.getLogger();/////////
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
		System.out.println("已连接到两个数据库...将以数据库1为主数据库进行比较");
		String sql = "select * from information_schema.TABLES where table_schema like '%d' order by table_schema,table_name";
		List list1 = com.Rs2List(com.getRsBySQL(sql, con1));
		List list2 = com.Rs2List(com.getRsBySQL(sql, con2));
		com.compare(list1, list2, con1, con2);
		System.out.println("比较完成....");
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
					System.out.println(URL2+"中，缺少表:" + name);
					if (auto_syn)
					{
						System.out.print("----------------自动创建表" + name + "...");
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
					System.out.println(URL2+"的 " + name + " 表中的字段:" + cname + ":"+map2.get(cname)+" 与"+URL1+" "+map1.get(cname)+"中数据类型不一致");
					if (auto_syn)
					{
						System.out.println("----------------此项目请手动修改!");
						//自动更改字段类型
//						String cType = map1.get(cname);
//						this.alterType(name,cname,cType);
					}
				}
			} else
			{
				System.out.println(URL2+"的 " + name + " 表中，缺少字段:" + cname);
				if (auto_syn)
				{
					System.out.print("----------------自动添加字段" + cname + "...");
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
					System.out.println(URL2+"的 " + name + " 表中的字段:" + cname + ":"+map2.get(cname)+" 与"+URL1+" "+map1.get(cname)+"中数据类型不一致");
					if (auto_syn)
					{
						System.out.println("----------------此项目请手动修改!");
					}
				}
			} else
			{
				System.out.println(URL2+"的 " + name + " 表中，缺少字段:" + cname);
				if (auto_syn)
				{
					System.out.print("----------------自动添加字段" + cname + "...");
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
	//自动更给字段类型
	private void alterType(String name,String cName,String cType,Connection conn){
		try{
			String sql ="alter table " +name+" modify "+cName+" "+cType ;
			PreparedStatement pre = conn.prepareStatement(sql);
			pre.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DbUtils.DoClear(conn);
		}
		
	}
}
