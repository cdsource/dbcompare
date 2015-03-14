package org.jpf.sql;

/**
 *
 * <p>Title: openwbass</p>
 *
 * <p>Description: NGBOSS</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: asiainfo</p>
 *
 * @author Œ‚∆Ω∏£
 * @version 1.0
 */
public class SqlParam
{
   public String strParam;
   public long lParam;
   public int iType; //0:long 1 string
   public SqlParam(String strParam)
   {
      this.iType = 1;
      this.strParam = strParam;
   }

   public SqlParam(long lParam)
   {
      this.iType = 0;
      this.lParam = lParam;
   }

   public SqlParam(int iParam)
   {
      this.iType = 2;
      lParam = iParam;
   }
   public SqlParam()
   {
      this.iType = -1;
   }   
}
