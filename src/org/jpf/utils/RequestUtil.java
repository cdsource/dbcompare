package org.jpf.utils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p>Title: 网上订单</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: </p>
 *
 * @author 吴平福
 * @version 1.0
 */
public class RequestUtil
{

  /**
   * @deprecated
   * 根据request对象和参数名称得到参数的字符串值
   *@param request HttpServletRequest
   *@param strParamName String 参数名称
   *@return String 参数的字符串值
   * */

  public static String getStrParamter(HttpServletRequest request,
                                      String strParamName)
  {
    String strParamValue = "";
    Object objParamValue = request.getParameter(strParamName);
    if (null != objParamValue) {
      try {
        strParamValue = (String) objParamValue;
      }
      catch (Exception ex) {
        strParamValue = "";
      }
    }
    return strParamValue;

  }

  /**
   * @todo REQUEST 取中文字符串
   * @param request HttpServletRequest
   * @param requstStr String
   * @throws Exception
   * @return String
   */
  public static String getRequestForChinaString(HttpServletRequest request,
                                                String strRequest) throws
      Exception
  {
    try {
      String sStr = request.getParameter(strRequest);
      sStr = (sStr == null) ? "" : sStr.trim();
      return new String(sStr.getBytes("iso8859-1"), "gbk");
    }
    catch (Exception ex) {}
    return null;
  }

  /**
   *
   * @param request HttpServletRequest
   * @param strRequest String
   * @param strDefaultValue String
   * @return String
   * @throws Exception
   */
  public static String getRequestForChinaString(HttpServletRequest request,
                                                String strRequest, String strDefaultValue) throws
      Exception
  {

    String sStr = request.getParameter(strRequest);
    if (sStr != null) {
      sStr = sStr.trim();
      return new String(sStr.getBytes("iso8859-1"), "gbk");
    }
    return strDefaultValue;

  }

  /**
   * @todo REQUEST 取int
   * @param request HttpServletRequest
   * @param requestStr String
   * @throws Exception
   * @return String
   */
  public static int getRequestForInt(HttpServletRequest request,
                                     String strRequest) throws Exception
  {
    return Integer.parseInt(request.getParameter(strRequest));
  }

  /**
   *
   * @param request HttpServletRequest
   * @param strRequest String
   * @param iDefaultValue int
   * @return int
   * @throws Exception
   */
  public static int getRequestForInt(HttpServletRequest request,
                                     String strRequest, int iDefaultValue) throws Exception
  {
    String strTmp = request.getParameter(strRequest);
    if (strTmp != null) {
      return Integer.parseInt(strTmp);
    }
    return iDefaultValue;
  }

  /**
   *
   * @param request HttpServletRequest
   * @param strRequest String
   * @return long
   * @throws Exception
   */
  public static long getRequestForLong(HttpServletRequest request,
                                       String strRequest) throws Exception
  {
    return Long.parseLong(request.getParameter(strRequest));
  }

  /**
   *
   * @param request HttpServletRequest
   * @param strRequest String
   * @param lDefaultValue long
   * @return long
   */
  public static long getRequestForLong(HttpServletRequest request,
                                       String strRequest, long lDefaultValue)
  {
    String strTmp = request.getParameter(strRequest);
    if (strTmp != null) {
      return Long.parseLong(strTmp);
    }
    return lDefaultValue;
  }

  /**
   * @todo REQUEST 取double
   * @param request HttpServletRequest
   * @param requestStr String
   * @throws Exception
   * @return String
   */
  public static double getRequestForDouble(HttpServletRequest request,
                                           String strRequest) throws Exception
  {
    return Double.parseDouble(request.getParameter(strRequest));
  }



}
