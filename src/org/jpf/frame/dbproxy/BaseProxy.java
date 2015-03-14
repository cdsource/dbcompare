/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2010-11-22 下午01:44:51 
* 类说明 
*/ 

package org.jpf.frame.dbproxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author keyboardsun@163.com
 */
public class BaseProxy
{
  /**
   * 存放statement对象的set方法。
   */
  protected static final Set setMethods = new HashSet();
  /**
   * 存放statement对象的set方法。
   */
  protected static final Set getMethods = new HashSet();
  /**
   * 存放statement对象的exec的几个方法。
   */
  protected static final Set execSqlMethods = new HashSet();
  private Map paramsMap = new HashMap();
  private List paramsNames = new ArrayList();
  private List paramsValues = new ArrayList();
  /**
   * 初始id,用于确定Proxy的同一个对象
   */
  private static int nextId = 100000;
  /**
   * 用于确定Proxy的同一个对象
   */
  protected int id;
  static
  {
    setMethods.add("setString");
    setMethods.add("setInt");
    setMethods.add("setByte");
    setMethods.add("setShort");
    setMethods.add("setLong");
    setMethods.add("setDouble");
    setMethods.add("setFloat");
    setMethods.add("setTimestamp");
    setMethods.add("setDate");
    setMethods.add("setTime");
    setMethods.add("setArray");
    setMethods.add("setBigDecimal");
    setMethods.add("setAsciiStream");
    setMethods.add("setBinaryStream");
    setMethods.add("setBlob");
    setMethods.add("setBoolean");
    setMethods.add("setBytes");
    setMethods.add("setCharacterStream");
    setMethods.add("setClob");
    setMethods.add("setObject");
    setMethods.add("setNull");
    getMethods.add("getString");
    getMethods.add("getInt");
    getMethods.add("getByte");
    getMethods.add("getShort");
    getMethods.add("getLong");
    getMethods.add("getDouble");
    getMethods.add("getFloat");
    getMethods.add("getTimestamp");
    getMethods.add("getDate");
    getMethods.add("getTime");
    getMethods.add("getArray");
    getMethods.add("getBigDecimal");
    getMethods.add("getAsciiStream");
    getMethods.add("getBinaryStream");
    getMethods.add("getBlob");
    getMethods.add("getBoolean");
    getMethods.add("getBytes");
    getMethods.add("getCharacterStream");
    getMethods.add("getClob");
    getMethods.add("getObject");
    getMethods.add("getNull");
    execSqlMethods.add("execute");
    execSqlMethods.add("executeUpdate");
    execSqlMethods.add("executeQuery");
  }

  public BaseProxy()
  {
    id = getNextId();
  }

  /**
   * 用于取得proxy调用对象的id，这里防止id重复，所以是同步的
   * @return
   */
  protected synchronized static int getNextId()
  {
    return nextId++;
  }

  protected void setColumn(Object key, Object value)
  {
    paramsMap.put(key, value);
    paramsNames.add(key);
    paramsValues.add(value);
  }

  protected Object getColumn(Object key)
  {
    return paramsMap.get(key);
  }

  protected String getValueString()
  {
    return paramsValues.toString();
  }

  protected String getTypeString()
  {
    List typeList = new ArrayList(paramsValues.size());
    for (int i = 0; i < paramsValues.size(); i++)
    {
      Object value = paramsValues.get(i);
      if (value == null)
      {
        typeList.add("null");
      } else
      {
        typeList.add(value.getClass().getName());
      }
    }
    return typeList.toString();
  }

  protected String getColumnString()
  {
    return paramsNames.toString();
  }

  protected void clearColumnInfo()
  {
    paramsMap.clear();
    paramsNames.clear();
    paramsValues.clear();
  }
}
