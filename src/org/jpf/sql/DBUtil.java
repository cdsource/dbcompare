package org.jpf.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

/**
 *
 * <p>
 * Title: WBASS
 * </p>
 * <p>
 * Description: WBASS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: asiainfo
 * </p>
 *
 * @author 吴平福
 * @version 2.0
 */
public final class DBUtil {
    public DBUtil() {
    }

    private static final Logger logger = Logger.getLogger(DBUtil.class);
    /**
     * @todo: call proc
     * @param cLogger
     *            Logger
     * @param conn
     *            Connection
     * @param strSql
     *            String
     * @param strParm
     *            String
     * @return boolean
     * @throws Exception
     */
    public static boolean ExecProc(final Logger cLogger /* IN */,
                                   Connection conn /* IN */,
                                   final String strProcName /* IN */,
                                   final SqlParam[] cSqlParam /* IN */) throws
            Exception {
        CallableStatement proc = null;
        try {
            cLogger.debug("call proc:" + strProcName);
            proc = conn.prepareCall(strProcName);
            for (int i = 0; i < cSqlParam.length; i++) {
                // for String
                if (1 == cSqlParam[i].iType) {
                    proc.setString(i + 1, cSqlParam[i].strParam);
                    cLogger.debug("param=" + cSqlParam[i].strParam);
                } else
                // for long
                {
                    proc.setLong(i + 1, cSqlParam[i].lParam);
                    cLogger.debug("param=" + cSqlParam[i].lParam);
                }
            }
            return proc.execute();
        } finally {
            closeStatement(proc);
        }

    }

    /**
     *
     * @param cLogger
     *            Logger
     * @param conn
     *            Connection
     * @param strProcName
     *            String
     * @return boolean
     * @throws Exception
     */
    public static boolean ExecProc(final Logger cLogger /* IN */,
                                   Connection conn,
                                   final String strProcName /* IN */) throws
            Exception {
        CallableStatement proc = null;
        cLogger.debug("strProcName=" + strProcName);
        try {

            proc = conn.prepareCall(strProcName);
            return proc.execute();
        } finally {
            closeStatement(proc);
        }
    }

    /**
     *
     * @param cLogger
     *            Logger
     * @param conn
     *            Connection
     * @param strProcName
     *            String
     * @param strParams
     *            String[]
     * @return boolean
     * @throws Exception
     */
    public static boolean ExecProc(final Logger cLogger /* IN */,
                                   Connection conn /* IN */,
                                   final String strProcName /* IN */,
                                   final String[] strParams /* IN */) throws
            Exception {
        CallableStatement proc = null;
        try {
            cLogger.debug(strProcName);
            proc = conn.prepareCall(strProcName);
            for (int i = 0; i < strParams.length; i++) {
                proc.setString(i + 1, strParams[i]);
                cLogger.debug("param=" + strParams[i]);
            }

            return proc.execute();
        } finally {
            closeStatement(proc);
        }
    }

    /**
     *
     * @param cLogger
     *            Logger
     * @param conn
     *            Connection
     * @param strSql
     *            String
     * @param strParm
     *            String
     * @return ResultSet
     * @throws Exception
     */
    public static ResultSet ExecSqlQuery(final Logger cLogger /* IN */,
                                         Connection conn /* IN */,
                                         final String strSql /* IN */,
                                         final String strParm /* IN */) throws
            Exception {
        cLogger.debug(strSql);
        PreparedStatement pStmt = conn.prepareStatement(strSql);
        cLogger.debug("param=" + strParm);
        pStmt.setString(1, strParm);
        return pStmt.executeQuery();
    }

    public static ResultSet ExecSqlQuery(Connection conn /* IN */,
                                         final String strSql /* IN */) throws
            Exception {

        logger.debug(strSql);
        PreparedStatement pStmt = conn.prepareStatement(strSql);
        return pStmt.executeQuery();
    }

    public static ResultSet ExecSqlQuery(final Logger cLogger /* IN */,
                                         Connection conn /* IN */,
                                         final String strSql /* IN */,
                                         final SqlParam[] cSqlParams /* IN */) throws
            Exception {
        cLogger.debug(strSql);
        PreparedStatement pStmt = conn.prepareStatement(strSql);
        for (int i = 0; i < cSqlParams.length; i++) {
            // for String
            if (1 == cSqlParams[i].iType) {
                pStmt.setString(i + 1, cSqlParams[i].strParam);
                cLogger.debug("param=" + cSqlParams[i].strParam);
            } else
            // for long
            {
                pStmt.setLong(i + 1, cSqlParams[i].lParam);
                cLogger.debug("param=" + cSqlParams[i].lParam);
            }
        }

        return pStmt.executeQuery();
    }

    /**
     *
     * @param cLogger
     *            Logger
     * @param conn
     *            Connection
     * @param strSql
     *            String
     * @param strParm
     *            String
     * @return ResultSet
     * @throws Exception
     */
    public static ResultSet ExecSqlQuery(final Logger cLogger /* IN */,
                                         Connection conn /* IN */,
                                         final String strSql /* IN */,
                                         final long iParam /* IN */) throws
            Exception {

        cLogger.debug(strSql);
        PreparedStatement pStmt = conn.prepareStatement(strSql);
        cLogger.debug("param=" + iParam);
        pStmt.setLong(1, iParam);
        return pStmt.executeQuery();
    }

    /**
     *
     * @param cLogger
     *            Logger
     * @param conn
     *            Connection
     * @param strSql
     *            String
     * @param iParm
     *            int
     * @return ResultSet
     * @throws Exception
     */
    public static ResultSet ExecSqlQuery(final Logger cLogger /* IN */,
                                         Connection conn /* IN */,
                                         final String strSql /* IN */,
                                         final int iParam /* IN */
            ) throws Exception {
        cLogger.debug(strSql);
        PreparedStatement pStmt = conn.prepareStatement(strSql);
        cLogger.debug("param=" + iParam);
        pStmt.setInt(1, iParam);
        return pStmt.executeQuery();
    }

    /**
     *
     * @param cLogger
     *            Logger
     * @param conn
     *            Connection
     * @param strSql
     *            String
     * @param strParm
     *            String
     * @return int
     * @throws Exception
     */

    public static int ExecSqlUpdate(final Logger cLogger /* IN */,
                                    Connection conn /* IN */,
                                    final String strSql /* IN */,
                                    final String strParam /* IN */) throws
            Exception {
        PreparedStatement pStmt = null;
        try {
            cLogger.debug(strSql);
            pStmt = conn.prepareStatement(strSql);
            cLogger.debug("param=" + strParam);
            pStmt.setString(1, strParam);
            return pStmt.executeUpdate();
        } finally {
        	closeStatement(pStmt);
        }
    }

    /**
     *
     * @param cLogger
     *            Logger
     * @param conn
     *            Connection
     * @param strSql
     *            String
     * @param cSqlParams
     *            SqlParam[]
     * @return int
     * @throws Exception
     */
    public static int ExecSqlUpdate(final Logger cLogger /* IN */,
                                    Connection conn /* IN */,
                                    final String strSql /* IN */,
                                    final SqlParam[] cSqlParams /* IN */) throws
            Exception {
        PreparedStatement pStmt = null;
        try {
            cLogger.debug(strSql);
            pStmt = conn.prepareStatement(strSql);
            for (int i = 0; i < cSqlParams.length; i++) {
                if (0 == cSqlParams[i].iType) {
                    pStmt.setLong(i + 1, cSqlParams[i].lParam);
                    cLogger.debug("param:=" + cSqlParams[i].lParam);
                } else {
                    pStmt.setString(i + 1, cSqlParams[i].strParam);
                    cLogger.debug("param:=" + cSqlParams[i].strParam);
                }
            }

            return pStmt.executeUpdate();
        } finally {
        	closeStatement(pStmt);
        }
    }

    /**
     * @todo exec sql update
     * @param cLogger
     *            Logger
     * @param conn
     *            Connection
     * @param strSql
     *            String
     * @param lParm
     *            long
     * @return int
     * @throws Exception
     */
    public static int ExecSqlUpdate(final Logger cLogger /* IN */,
                                    Connection conn /* IN */,
                                    final String strSql /* IN */,
                                    final long lParam /* IN */) throws
            Exception {
        PreparedStatement pStmt = null;
        try {
            cLogger.debug(strSql);
            cLogger.debug("param=" + lParam);
            pStmt = conn.prepareStatement(strSql);
            pStmt.setLong(1, lParam);
            return pStmt.executeUpdate();
        } finally {
        	closeStatement(pStmt);
        }
    }

    public static int ExecSqlUpdate(final Logger cLogger /* IN */,
                                    Connection conn /* IN */,
                                    final String strSql /* IN */,
                                    final int iParam /* IN */) throws Exception {
        PreparedStatement pStmt = null;
        try {
            cLogger.debug(strSql);
            cLogger.debug("param=" + iParam);
            pStmt = conn.prepareStatement(strSql);
            pStmt.setInt(1, iParam);
            return pStmt.executeUpdate();
        } finally {
        	closeStatement(pStmt);
        }
    }

    /**
     *
     * @param proc
     *            CallableStatement
     */
    private static void closeStatement(CallableStatement proc) {
        try {
            if (null != proc) {
                proc.close();
            }
        } catch (Exception ex) {
        }
    }
    /**
    *
    * @param proc
    *            CallableStatement
    */
   private static void closeStatement(PreparedStatement pstmt) {
       try {
           if (null != pstmt) {
        	   pstmt.close();
           }
       } catch (Exception ex) {
       }
   }
    /**
     *
     * @param cLogger
     *            Logger
     * @param conn
     *            Connection
     * @param in_sql
     *            String
     * @return int
     * @throws Exception
     */
    public static int ExecSqlUpdate(Logger cLogger, Connection conn,
                                    String in_sql) throws Exception {
        PreparedStatement pStmt = null;
        try {
            cLogger.debug(in_sql);
            pStmt = conn.prepareStatement(in_sql);
            return pStmt.executeUpdate();
        } finally {
            try {
                pStmt.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     *
     * @param cLogger
     *            Logger
     * @param conn
     *            Connection
     * @param in_sql
     *            String
     * @return ResultSet
     * @throws Exception
     */
    public static ResultSet ExecSqlQuery(Logger cLogger, Connection conn,
                                         String in_sql) throws Exception {
        cLogger.debug(in_sql);
        PreparedStatement 

         pStmt = conn.prepareStatement(in_sql);
        return pStmt.executeQuery();

    }

    /**
     *
     * @param cLogger
     *            Logger
     * @param conn
     *            Connection
     * @param strSql
     *            String
     * @param lParam
     *            long -1 表示没有该参数
     * @return ResultSet
     * @throws Exception
     */
    public static ResultSet ExecSqlQueryForBsr(Logger cLogger, Connection conn,
                                               String strSql, long lParam) throws
            Exception {

        cLogger.debug(strSql);
        PreparedStatement pStmt = conn.prepareStatement(strSql,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        if (lParam >= 0) {
            cLogger.debug("param=" + lParam);
            pStmt.setLong(1, lParam);
        }
        return pStmt.executeQuery();
    }
    public static int DeleteTableData(Connection conn, String tablename, String condition) throws Exception
    {
  	  int iResult = 0;
      String strSql = "delete from  " + tablename + " where " + condition;
      logger.info(strSql);
      PreparedStatement pStmt = conn.prepareStatement(strSql);
      iResult=pStmt.executeUpdate();
      pStmt.close();
      return iResult;
    }
}
