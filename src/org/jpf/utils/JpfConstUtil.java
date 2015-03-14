package org.jpf.utils;

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
public final class JpfConstUtil
{

  public class CONTENT_TYPE
  {
    public final static String GBK_EXECL_CONTENT_TYPE =
        "application/vnd.ms-excel;charset=GBK";
    public final static String UTF_EXECL_CONTENT_TYPE =
        "application/vnd.ms-excel;charset=UTF-8";
    public final static String WEB_CONTEXT_TYPE = "text/plain";
  }

  //主键冲突
  public static final int SQL_PK_ERROR_CODE = 1;

  //登录限制次数
  public static final int MAX_LOGIN_RETRY_COUNT = 9;
  /*************************************************************************
   * 定义SESSION
   ************************************************************************/

  //应用用户登录保存在session中用户对象的ID
  public static final String APP_SESSION_LOGINED_OPER =
      "APP_SESSION_LOGINED_OPER";
  public static final String APP_SESSION_LOGIN_COUNT =
      "APP_SESSION_LOGIN_COUNT";
  public static final String APP_SESSION_LOGINED_OPER_NAME =
      "APP_SESSION_LOGINED_OPER_NAME";

//错误信息保存在request attribute中的key
  public static final String APP_ERROR_MESSAGE_KEY =
      "APP_ERROR_MESSAGE_KEY";

  /*************************************************************************
   * 定义页面返回值
   ************************************************************************/
  public class ResponseInfo
  {
    //请求返回成功
    public final static int REQUEST_SUCESS = 0;
    //定义页面返回错误值，设置的值必须小于0
    public final static int REQUEST_FAIL = -1;
    public final static int REQUEST_FAIL_SQL_INSERT = -2;
    public final static int REQUEST_FAIL_SQL_UPDATE = -3;
    public final static int REQUEST_FAIL_SQL_DELETE = -4;
    public final static int REQUEST_FAIL_SQL_SELECT = -5;
    public final static int REQUEST_FAIL_SQL_PK = -6;
    public final static int REQUEST_FAIL_SQL_COM = -7;
    public final static int REQUEST_FAIL_COND_FAIL = -8;
    public final static int REQUEST_FAIL_PARAM_FAIL = -9;
    public final static int REQUEST_FAIL_SQL_FK = -10;
    public final static int REQUEST_FAIL_FILE_UPLOAD = -11;
    public final static int REQUEST_FAIL_FILE_UNKNOWN = -12;

    /************************************************************************
     * 定义标准页面返回字符串
     ***********************************************************************/

    public static final String RESP_SUCCESS = "命令执行成功";
    public static final String RESP_FAIL_ERR_ACTIONCODE = "错误的命令参数";
    public static final String RESP_FAIL = "命令执行失败";
  }

}
