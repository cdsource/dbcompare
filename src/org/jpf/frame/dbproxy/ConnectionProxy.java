/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2010-11-22 下午01:45:25 
* 类说明 
*/ 

package org.jpf.frame.dbproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 * 本类用于记录Connection一层的日志
 * @author keyboardsun@163.com
 */
public class ConnectionProxy extends BaseProxy implements InvocationHandler
{
  private static final Logger logger = Logger.getLogger(ConnectionProxy.class);

  private Connection connection;
  private ConnectionProxy(Connection conn)
  {
    super();
    this.connection = conn;
    logger.debug("ConnectionId=" + id);
  }

  /*传说中的invoke，所有Connection .class类调用的方法都要经过他分配，很流氓的东西
   这里的类必须要实现InvocationHandler接口，这样才有invoke的由来
   */
  public Object invoke(Object prox, Method method, Object[] params) throws Throwable
  {
    try
    {
      if ("prepareStatement".equalsIgnoreCase(method.getName()))
      {
        logger.debug("ConnectionId=" + id + " Preparing Statement: " + params[0].toString());
        PreparedStatement stmt = (PreparedStatement)method.invoke(connection, params); //这里才是真正的调用到该调用的文件
        stmt = PreparedStatementProxy.newInstance(stmt, (String)params[0]);
        return stmt; //这里是出卖PreparedStatement.class等文件给代理的。
      } else if ("prepareCall".equalsIgnoreCase(method.getName()))
      {
        logger.debug("ConnectionId=" + id + " prepareCall Statement: " + params[0].toString());
        PreparedStatement stmt = (PreparedStatement)method.invoke(connection, params);
        stmt = PreparedStatementProxy.newInstance(stmt, (String)params[0]);
        return stmt;
      } else if ("createStatement".equalsIgnoreCase(method.getName()))
      {
        logger.debug("ConnectionId=" + id + " createStatement: " + params[0].toString());
        Statement stmt = (Statement)method.invoke(connection, params);
        stmt = StatementProxy.newInstance(stmt);
        return stmt;
      } else
      {
        return method.invoke(connection, params);
      }
    } catch (Throwable t)
    {
      throw t;
    }
  }

  /**
   * 这里让这丫的Connection也进入proxy的过滤文件列表
   */
  public static Connection newInstance(Connection conn)
  {
    InvocationHandler handler = new ConnectionProxy(conn);
    ClassLoader cl = Connection.class.getClassLoader();
    return (Connection)Proxy.newProxyInstance(cl, new Class[]
                                              {Connection.class /*看这里，在这里Connection.class被出卖了，到他的信息都要先被ConnectionProxy 看看了，一下类我就不标注了*/}, handler);
  }
}
