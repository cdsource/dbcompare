/** 
* @author 吴平福 
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年2月8日 下午11:07:00 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public abstract class AbstractDbCompare
{
	private static final Logger logger = LogManager.getLogger();
	
	protected DbDescInfo cDbDescInfo;
    
    protected String strDomain="";
    protected String strMails="";
	public StringBuffer[] sb = { new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer(), new StringBuffer(),
			new StringBuffer(),new StringBuffer() };
	
	abstract void DoWork(Connection conn_product,Connection conn_develop) throws Exception;

	/**
	 * 
	 */
	public void DoCompare(Connection conn_product,Connection conn_develop,String strDomain,String strMails )
	{
		// TODO Auto-generated constructor stub
    	try
		{
    		this.strDomain=strDomain;
    		this.strMails=strMails;
    		DoWork( conn_product, conn_develop); // �Ƚ����ݿ�
    		//CompareUtil.writeFile(sb); // д���ļ�
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
