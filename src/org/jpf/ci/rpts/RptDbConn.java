/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2014年12月28日 下午2:51:27 
* 类说明 
*/ 

package org.jpf.ci.rpts;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class RptDbConn
{
	private static final Logger logger = LogManager.getLogger();
	public static RptDbConn cRptDbConn=new RptDbConn();
	
	public static RptDbConn GetInstance()
	{
		return cRptDbConn;
	}
	public Connection GetConn()
	{
		Connection conn=null;

		try {

			String driver ="com.mysql.jdbc.Driver";

			String URL = "jdbc:mysql://10.10.12.153:3333/sonar";
			//String URL = "jdbc:mysql://127.0.0.1:3306/sonar";
			String dbuser = "sonar";

			String dbpass ="sonar";
			logger.info("DB URL:"+URL);
			Class.forName(driver).newInstance();

			conn = DriverManager.getConnection(URL, dbuser, dbpass);

		} catch (Exception ex) {

			ex.printStackTrace();

			return null;

		}		
		return conn;
	}


}
