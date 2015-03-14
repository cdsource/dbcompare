/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2012-4-9 下午5:26:06 
* 类说明 
*/ 

package org.jpf.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * 
 */
public class JpfDbUtil {
    /**
     * 关闭数据库
     * 
     * @param conn
     *            Connection
     */
    public static void doClear(Connection conn)
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
    public static void doClear( PreparedStatement stmt, ResultSet rs)
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
    public static void doClear(Connection conn, PreparedStatement stmt, ResultSet rs)
    {
        doClear(stmt,rs);
        doClear(conn);
    }

    public static void doError(Connection conn, Logger cLogger, Exception ex)
    {
        cLogger.error(ex);
        try
        {
            conn.rollback();
        } catch (SQLException sqlEx)
        {
        }

    }
    public static void doClear(PreparedStatement pStmt)
    {
        try
        {
            if (pStmt != null)
                pStmt.close();
        } catch (Exception e)
        {
        }
    }    
    public static void doClear(ResultSet rs)
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
}
