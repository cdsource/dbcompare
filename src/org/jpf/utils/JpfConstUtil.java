package org.jpf.utils;

/**
 *
 * <p>Title: ���϶���</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: </p>
 *
 * @author ��ƽ��
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

  //������ͻ
  public static final int SQL_PK_ERROR_CODE = 1;

  //��¼���ƴ���
  public static final int MAX_LOGIN_RETRY_COUNT = 9;
  /*************************************************************************
   * ����SESSION
   ************************************************************************/

  //Ӧ���û���¼������session���û������ID
  public static final String APP_SESSION_LOGINED_OPER =
      "APP_SESSION_LOGINED_OPER";
  public static final String APP_SESSION_LOGIN_COUNT =
      "APP_SESSION_LOGIN_COUNT";
  public static final String APP_SESSION_LOGINED_OPER_NAME =
      "APP_SESSION_LOGINED_OPER_NAME";

//������Ϣ������request attribute�е�key
  public static final String APP_ERROR_MESSAGE_KEY =
      "APP_ERROR_MESSAGE_KEY";

  /*************************************************************************
   * ����ҳ�淵��ֵ
   ************************************************************************/
  public class ResponseInfo
  {
    //���󷵻سɹ�
    public final static int REQUEST_SUCESS = 0;
    //����ҳ�淵�ش���ֵ�����õ�ֵ����С��0
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
     * �����׼ҳ�淵���ַ���
     ***********************************************************************/

    public static final String RESP_SUCCESS = "����ִ�гɹ�";
    public static final String RESP_FAIL_ERR_ACTIONCODE = "������������";
    public static final String RESP_FAIL = "����ִ��ʧ��";
  }

}
