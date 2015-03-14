/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2010-11-22 ����01:46:59 
* ��˵�� 
*/ 

package org.jpf.frame.dbproxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class TestProxy
{
  private static Connection getConnection() throws Throwable
  {
    Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
    Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.10.10.203:1521:ora01", "js", "js");
    return ConnectionProxy.newInstance(conn); //�����Ϊ��δ��룬���ݿ�Ĳ�����ȫ������������Proxy
  }

  public static void main(String args[])
  {
    try
    {
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement("select * from sys_log where rownum<? AND oper_code=?");
      statement.setInt(1, 100);
      statement.setInt(2, 0);
      ResultSet rs = statement.executeQuery();
      while (rs.next())
      {
        System.out.println(rs.getString("OPER_MSG"));
        System.out.println(rs.getString("OPER_TABLE"));
      }
    } catch (Throwable e)
    {
      e.printStackTrace();
    }
  }
}
