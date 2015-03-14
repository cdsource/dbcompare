/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2015年1月14日 下午9:58:28 
* 类说明 
*/ 

package org.jpf.utils;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class JpfDbUtils
{
	private static final Logger logger = LogManager.getLogger();
	public static void DoClear(Connection conn)
	{
		try
		{
			if (conn != null)
			{
				conn.close();
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public static void ExecUpdateSql(Connection conn,String strSql)throws Exception
	{
		java.sql.Statement stmt=conn.createStatement();
		logger.info(strSql);
		int i=stmt.executeUpdate(strSql);
		logger.info("rows count={}",i);
		if (conn.getAutoCommit() == false)
		{
			conn.commit();
		}
	}
	
}
