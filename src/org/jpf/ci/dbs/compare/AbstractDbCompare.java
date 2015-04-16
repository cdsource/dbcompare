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
import org.jpf.utils.JpfDbUtils;
import org.jpf.visualwall.WallsDbConn;


/**
 * 
 */
public abstract class AbstractDbCompare
{
	private static final Logger logger = LogManager.getLogger();
	protected DbDescInfo cDbDescInfo;
    
    protected String strDomain="";
    protected String strMails="";
    protected String strExcludeTable="";


    protected StringBuffer[] sb = { new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer(), new StringBuffer(),
			new StringBuffer(),new StringBuffer() };
	
	abstract void DoWork(Connection conn_product,Connection conn_develop) throws Exception;
	abstract String GetHtmlName();
	
	protected int iCount1=0;
	protected int iCount2=0;
	protected int iCount3=0;
	protected int iCount4=0;
	protected int iCount5=0;
	protected int iCount6=0;
	/**
	 * 
	 */
	public void DoCompare(Connection conn_product,Connection conn_develop,String strDomain,String strMails,String strDbUrl,String strPdmInfo,String strExcludeTable )
	{
		// TODO Auto-generated constructor stub
    	try
		{
    		this.strDomain=strDomain;
    		this.strMails=strMails;
    		this.strExcludeTable=strExcludeTable;
    		DoWork( conn_product, conn_develop);    	

    		CompareUtil.SendMail(sb, strMails,strDbUrl,strPdmInfo,GetHtmlName()); 
    		InsertResult(strDbUrl);
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}

	}
	
	private void InsertResult(String strDbUrl)
	{
		Connection conn=null;
		try
		{
			conn=WallsDbConn.GetInstance().GetConn();
			String strSql="insert into dbci values( '"+strMails+"','"+strDbUrl+"',CURRENT_DATE,"+iCount1+","+iCount2+","+iCount3+","+iCount4+","+iCount5+","+iCount6+",'HZ')";
			logger.info(strSql);
			JpfDbUtils.ExecUpdateSql(conn, strSql);
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}finally
		{
			JpfDbUtils.DoClear(conn);
		}
	}
	public AbstractDbCompare()
	{

	}

}
