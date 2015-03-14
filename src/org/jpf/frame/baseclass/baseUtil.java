/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2011-8-30 下午01:57:54 
* 类说明 
*/ 

package org.jpf.frame.baseclass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * 
 */
public abstract class baseUtil
{

	  /**
	   * 关闭数据库
	   * @param conn Connection
	   */
	  public static void DoClear(Connection conn)
	  {
	    try {
	      if (conn != null) {
	    	  conn.close();
	      }
	    }
	    catch (Exception ex) {
	    }
	  }

	  public static void DoError(Connection conn, Logger cLogger, Exception ex)
	  {
	    cLogger.error(ex);
	    try {
	      conn.rollback();
	    }
	    catch (SQLException sqlEx) {}

	  }
		public static void DoClear(ResultSet rs  )
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
		}
        public static void DoClear(  PreparedStatement stmt)
        {
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
			DoClear(rs);		
			DoClear(stmt);
			DoClear(conn);
		}	  
		public static void DoClear(Connection conn, PreparedStatement stmt)
		{
			DoClear(stmt);
			DoClear(conn);
		}			
}
