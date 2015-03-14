/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2011-10-31 ����2:11:13 
* ��˵�� 
*/ 

package org.jwpf.frame.baseclass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServlet;

/**
 * 
 */
public class baseServlet extends HttpServlet
{
	public void DoClear(Connection conn)
	{
		if(conn!=null)
			try
		{
				conn.close();
		}catch(Exception ex){}
	}
	public void DoClear( PreparedStatement stmt, ResultSet rs)
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
	public void DoClear(Connection conn, PreparedStatement stmt, ResultSet rs)
	{
		DoClear(stmt,rs);
		DoClear(conn);
	}	
}
