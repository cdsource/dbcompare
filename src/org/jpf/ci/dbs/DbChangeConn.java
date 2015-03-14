/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2013-4-7 下午4:37:58 
 * 类说明 
 */

package org.jpf.ci.dbs;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class DbChangeConn
{
	private static final Logger logger = LogManager.getLogger(DbChangeConn.class);
	public static DbChangeConn cDbChangeConn = new DbChangeConn();

	public static DbChangeConn GetInstance()
	{
		return cDbChangeConn;
	}

	private String URL = "jdbc:mysql://10.10.12.153:4306/";
	private String dbuser = "billing";
	private String dbpass = "billing";

	// for id
	private String URL2="jdbc:mysql://10.10.12.153:3310/";
	private String dbuser2 = "billing";
	private String dbpass2 = "billing";
	
	//for 150
	private String URL3="jdbc:mysql://10.10.12.150:4306/";
	private String dbuser3 = "billing";
	private String dbpass3 = "billing";
	
	public String GetUsr()
	{
		return dbuser;
	}

	public String GetPwd()
	{
		return dbpass;
	}
	public Connection GetConn(int iDbType)throws Exception
	{
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver).newInstance();
		if(1==iDbType)
			return DriverManager.getConnection(URL, dbuser, dbpass);
		return  DriverManager.getConnection(URL2, dbuser2, dbpass2);
	}
	
	public Connection GetMutiConn()throws Exception
	{
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver).newInstance();
		return  DriverManager.getConnection(URL3, dbuser3, dbpass3);
	}

}
