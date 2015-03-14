/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2014年12月25日 下午1:31:23 
 * 类说明 
 */

package org.jpf.visualwall;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class WallsDbConn
{
	private static final Logger logger = LogManager.getLogger();
	public static WallsDbConn cWallsDbConn = new WallsDbConn();

	public static WallsDbConn GetInstance()
	{
		return cWallsDbConn;
	}

	public Connection GetConn()
	{
		Connection conn = null;

		try
		{

			String driver = "com.mysql.jdbc.Driver";
			String URL = "jdbc:mysql://10.1.228.11:4306/visualwall";
			// String URL = "jdbc:mysql://127.0.0.1:3306/sonar";
			String dbuser = "sonar";

			String dbpass = "sonar";
			logger.info("DB URL:" + URL);
			Class.forName(driver).newInstance();

			conn = DriverManager.getConnection(URL, dbuser, dbpass);

		} catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
		return conn;
	}

}
