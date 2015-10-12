/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo.com 
* @version ����ʱ�䣺2015��2��13�� ����3:50:47 
* ��˵�� 
*/ 

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class DbDescInfo
{
	private static final Logger logger = LogManager.getLogger();
	private String Url;
	private String Usr;
	private String Pwd;
	/**
	 * 
	 */
	public DbDescInfo(String Url,String Usr,String Pwd)
	{
		// TODO Auto-generated constructor stub
		this.Url="jdbc:mysql://"+Url+"/";
		this.Usr=Usr;
		this.Pwd=Pwd;
	}
	public Connection GetConn() throws Exception
	{
		logger.debug(this.Url);
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver).newInstance();
		return DriverManager.getConnection(Url, Usr, Pwd);
	}
	public String GetUrlStr()
	{
		return Url;
	}
}
