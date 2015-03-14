/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2010-11-22 下午01:46:37 
* 类说明 
*/ 

package org.jpf.frame.dbproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;

public class StatementProxy extends BaseProxy implements InvocationHandler
{
  private static final Logger logger = Logger.getLogger(StatementProxy.class);
  private Statement statement;
  private StatementProxy(Statement stmt)
  {
    super();
    this.statement = stmt;
  }

  public Object invoke(Object proxy, Method method, Object[] params) throws Throwable
  {
    try
    {
      if (execSqlMethods.contains(method.getName()))
      {
        logger.debug("StatementId =" + id + " Statement: " + params[0].toString());
        if ("executeQuery".equals(method.getName()))
        {
          ResultSet rs = (ResultSet)method.invoke(statement, params);
          if (rs != null)
          {
            return ResultSetProxy.newInstance(rs);
          } else
          {
            return null;
          }
        } else
        {
          return method.invoke(statement, params);
        }
      } else if ("getResultSet".equals(method.getName()))
      {
        ResultSet rs = (ResultSet)method.invoke(statement, params);
        if (rs != null)
        {
          return ResultSetProxy.newInstance(rs);
        } else
        {
          return null;
        }
      } else if ("equals".equals(method.getName()))
      {
        Object ps = params[0];
        if (ps instanceof Proxy)
        {
          return new Boolean(proxy == ps);
        }
        return new Boolean(false);
      } else if ("hashCode".equals(method.getName()))
      {
        return new Integer(proxy.hashCode());
      } else
      {
        return method.invoke(statement, params);
      }
    } catch (Throwable t)
    {
      throw t;
    }
  }

  /**
   * Statement.class被出卖
   */
  public static Statement newInstance(Statement stmt)
  {
    InvocationHandler handler = new StatementProxy(stmt);
    ClassLoader cl = Statement.class.getClassLoader();
    return (Statement)Proxy.newProxyInstance(cl, new Class[]
                                             {Statement.class}, handler);
  }
}
