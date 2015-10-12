/** 
* @author 吴平福 
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年5月29日 上午12:47:23 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

/**
 * 
 */
public class ErrExecSqlInfo
{

	/**
	 * 
	 */
	public ErrExecSqlInfo(String strSql,String strMsg)
	{
		// TODO Auto-generated constructor stub
		ExecSql=strSql;
		ErrMsg=strMsg;
	}
   public String getExecSql()
	{
		return ExecSql;
	}
	public void setExecSql(String execSql)
	{
		ExecSql = execSql;
	}
	public String getErrMsg()
	{
		return ErrMsg;
	}
	public void setErrMsg(String errMsg)
	{
		ErrMsg = errMsg;
	}
    private String ExecSql;
    private String ErrMsg;
}
