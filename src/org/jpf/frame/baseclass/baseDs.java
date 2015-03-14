/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2011-7-13 上午10:28:01 
 * 类说明 
 */

package org.jwpf.frame.baseclass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * 
 */
public class baseDs
{
	/**
	 * 关闭数据库
	 * 
	 * @param conn
	 *            Connection
	 */
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
		}
	}
	public static void DoClear( PreparedStatement stmt, ResultSet rs)
	{
		try
		{
			if (rs != null)
			{
				rs.close();
			}
		} catch (Exception ex)
		{
		}

		try
		{
			if (stmt != null)
			{
				stmt.close();
			}
		} catch (Exception ex)
		{
		}
	}
	public static void DoClear(Connection conn, PreparedStatement stmt, ResultSet rs)
	{
		DoClear(stmt,rs);
		DoClear(conn);
	}

	public static void DoError(Connection conn, Logger cLogger, Exception ex)
	{
		cLogger.error(ex);
		try
		{
			conn.rollback();
		} catch (SQLException sqlEx)
		{
		}

	}
}
