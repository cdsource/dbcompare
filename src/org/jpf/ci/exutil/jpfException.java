/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2011-1-7 ����03:37:38 
* ��˵�� 
*/ 

package org.jpf.exutil;

import java.sql.SQLException;

/**
 * 
 */
public class jpfException extends Exception implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1422889706965924713L;
	int iCode=0;
	String strDesc="";
	
	public void HandleSqlEx(SQLException ex)
	{
		if (ex.getErrorCode() == 1)
		{
			iCode=10000+ex.getErrorCode();
			strDesc="������ͻ";
		}		
		if (ex.getErrorCode() == 904)
		{
			iCode=10000+ex.getErrorCode();
			strDesc="�в�����";
		}
		if (ex.getErrorCode() == 942)
		{
			iCode=10000+ex.getErrorCode();
			strDesc="������";
		}		
	}
    /**
     * �쳣���͵Ĺ��캯��
     * */
    public jpfException() {
        super("");
        this.iCode = 0;
    }

    /**
     * �쳣���͸��ݴ�����Ϣ�Ĺ��캯��
     * @param errMsg (int)������Ϣ
     * */
    public jpfException(String errMsg) {
        super(errMsg);
        this.iCode = 0;
    }

    /**
     * �쳣���͸��ݴ������Ĺ��캯��
     * @param errCode (int)�������
     * */
    public jpfException(int errCode) {
        this.iCode = errCode;
    }

    /**
     * �쳣���͸��ݴ������ʹ�����Ϣ�Ĺ��캯��
     * @param errCode (int)�������
     * @param errMsg (int)������Ϣ
     * */
    public jpfException(int errCode,String errMsg) {
        super(errMsg);
        this.iCode = errCode;
    }

    /**
     * �õ��������
     * @return �������
     * */
    public int getErrCode() {
        return this.iCode;
    }

    /**
     * �õ�������Ϣ
     * @return ������Ϣ
     * */
    public String getErrMsg() {
        return super.getMessage();
    }

    /**
     * �õ�������Ϣ
     * @return ������Ϣ
     * */
    public String toString() {
        return super.toString();
    }	
}
