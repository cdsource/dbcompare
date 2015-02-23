/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo.com 
* @version ����ʱ�䣺2015��1��18�� ����12:47:25 
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
public class DbInfo
{
	private static final Logger logger = LogManager.getLogger();
	private String URL1 = "jdbc:mysql://10.10.12.153:4306/";
	private String dbuser1 = "billing";
	private String dbpass1 = "billing";

	public static DbInfo cMutiDbConn = new DbInfo();

	public static DbInfo GetInstance()
	{
		return cMutiDbConn;
	}
	public Connection getTransaction_product() throws Exception
	{
		logger.info(URL1);
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver).newInstance();
		return DriverManager.getConnection(URL1, dbuser1, dbpass1);
	}

}
