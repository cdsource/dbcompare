package org.jpf.exutil;

import java.sql.SQLException;

public class ExceptionUtil
{
   public static final int SQL_PK_ERROR_CODE = 1;

   public final static int REQUEST_FAIL_SQL_PK = -6;
   public final static int REQUEST_FAIL_SQL_INSERT = -2;
   public final static int REQUEST_FAIL_SQL_UPDATE = -3;
   public final static int REQUEST_FAIL_SQL_DELETE = -4;
   public final static int REQUEST_FAIL_SQL_SELECT = -5;
   public final static int REQUEST_FAIL_SQL_COM = -7;
   public ExceptionUtil()
   {
   }

   /**
    *
    * @param sqlExp SQLException
    * @param conn Connection
    */
   public static int DoSQLError(SQLException sqlExp, String strSql)
   {

      if (strSql != null && strSql.toLowerCase().startsWith("insert"))
      {
         return REQUEST_FAIL_SQL_INSERT;
      }
      if (strSql != null && strSql.toLowerCase().startsWith("update"))
      {
         return REQUEST_FAIL_SQL_UPDATE;
      }
      if (strSql != null && strSql.toLowerCase().startsWith("delete"))
      {
         return REQUEST_FAIL_SQL_DELETE;
      }
      if (strSql != null && strSql.toLowerCase().startsWith("select"))
      {
         return REQUEST_FAIL_SQL_SELECT;
      }

      return REQUEST_FAIL_SQL_COM;

   }

}
