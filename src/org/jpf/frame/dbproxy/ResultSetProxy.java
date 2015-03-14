/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2010-11-22 下午01:46:15 
* 类说明 
*/ 

package org.jpf.frame.dbproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

/**
 * @author keyboardsun@163.com
 *
 */
public class ResultSetProxy extends BaseProxy implements InvocationHandler
{
  private static final Logger logger = Logger.getLogger(ResultSetProxy.class);
  boolean first = true;
  private ResultSet rs;
  private ResultSetProxy(ResultSet rs)
  {
    super();
    this.rs = rs;
    logger.debug("ResultSetId = " + id + " ResultSet");
  }

  public Object invoke(Object proxy, Method method, Object[] params) throws Throwable
  {
    try
    {
      Object o = method.invoke(rs, params);
      if (getMethods.contains(method.getName()))
      {
        if (params[0] instanceof String)
        {
          setColumn(params[0], o);
        }
      } else if ("next".equals(method.getName()) || "close".equals(method.getName()))
      {
        String s = getValueString();
        if (!"[]".equals(s))
        {
          if (first)
          {
            first = false;
            logger.debug("ResultSetId =" + id + " Header: " + getColumnString());
          }
          logger.debug("ResultSetId =" + id + " Result: " + s);
        }
        clearColumnInfo();
      }
      return o;
    } catch (Throwable t)
    {
      throw t;
    }
  }

  /**
   * 这里ResultSet.class被出卖
   */
  public static ResultSet newInstance(ResultSet rs)
  {
    InvocationHandler handler = new ResultSetProxy(rs);
    ClassLoader cl = ResultSet.class.getClassLoader();
    return (ResultSet)Proxy.newProxyInstance(cl, new Class[]
                                             {ResultSet.class}, handler);
  }

  /**
   * Get the wrapped result set
   * @return the resultSet
   */
  public ResultSet getRs()
  {
    return rs;
  }
}
