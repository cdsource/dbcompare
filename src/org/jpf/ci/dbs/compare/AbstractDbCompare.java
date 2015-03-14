/** 
* @author 吴平福 
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年2月8日 下午11:07:00 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

import java.sql.Connection;


/**
 * 
 */
public abstract class AbstractDbCompare
{

	protected DbDescInfo cDbDescInfo;
    
    protected String strDomain="";
    protected String strMails="";

    protected StringBuffer[] sb = { new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer(), new StringBuffer(),
			new StringBuffer(),new StringBuffer() };
	
	abstract void DoWork(Connection conn_product,Connection conn_develop) throws Exception;

	/**
	 * 
	 */
	public void DoCompare(Connection conn_product,Connection conn_develop,String strDomain,String strMails,String strDbUrl )
	{
		// TODO Auto-generated constructor stub
    	try
		{
    		this.strDomain=strDomain;
    		this.strMails=strMails;
    		DoWork( conn_product, conn_develop);    	
    		CompareUtil.SendMail(sb, strMails,strDbUrl); 
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}

	}
	
	public AbstractDbCompare()
	{

	}

}
