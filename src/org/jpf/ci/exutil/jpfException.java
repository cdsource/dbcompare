/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2011-1-7 下午03:37:38 
* 类说明 
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
			strDesc="主键冲突";
		}		
		if (ex.getErrorCode() == 904)
		{
			iCode=10000+ex.getErrorCode();
			strDesc="列不存在";
		}
		if (ex.getErrorCode() == 942)
		{
			iCode=10000+ex.getErrorCode();
			strDesc="表不存在";
		}		
	}
    /**
     * 异常类型的构造函数
     * */
    public jpfException() {
        super("");
        this.iCode = 0;
    }

    /**
     * 异常类型根据错误信息的构造函数
     * @param errMsg (int)错误信息
     * */
    public jpfException(String errMsg) {
        super(errMsg);
        this.iCode = 0;
    }

    /**
     * 异常类型根据错误代码的构造函数
     * @param errCode (int)错误代码
     * */
    public jpfException(int errCode) {
        this.iCode = errCode;
    }

    /**
     * 异常类型根据错误代码和错误信息的构造函数
     * @param errCode (int)错误代码
     * @param errMsg (int)错误信息
     * */
    public jpfException(int errCode,String errMsg) {
        super(errMsg);
        this.iCode = errCode;
    }

    /**
     * 得到错误代码
     * @return 错误代码
     * */
    public int getErrCode() {
        return this.iCode;
    }

    /**
     * 得到错误信息
     * @return 错误信息
     * */
    public String getErrMsg() {
        return super.getMessage();
    }

    /**
     * 得到错误信息
     * @return 错误信息
     * */
    public String toString() {
        return super.toString();
    }	
}
