/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年2月8日 下午11:07:00 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.mails.JpfMail;
import org.jpf.utils.JpfDateTimeUtil;
import org.jpf.utils.JpfDbUtils;


/**
 * 
 */
public abstract class AbstractDbCompare
{
	private static final Logger logger = LogManager.getLogger();
	protected DbDescInfo cDbDescInfo;

	protected StringBuffer[] sb = { new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer(), new StringBuffer(), new StringBuffer(), new StringBuffer(), new StringBuffer() };

	protected Vector<ExecSqlInfo> vSql = new Vector<ExecSqlInfo>();

	abstract void DoWork(Connection conn_product, Connection conn_develop, CompareInfo cCompareInfo) throws Exception;

	abstract String GetHtmlName();

	abstract String GetMailTitle();

	protected int iCount1 = 0;
	protected int iCount2 = 0;
	protected int iCount3 = 0;
	protected int iCount4 = 0;
	protected int iCount5 = 0;
	protected int iCount6 = 0;
	protected int iCount7 = 0;
	protected int iCount8 = 0;
	protected int iCount9 = 0;
	protected int iCount10 = 0;
	
	protected final int iSqlTimeOut = 360;

	private String strSaveDate=JpfDateTimeUtil.GetCurrDateTime();
	protected void InsertResult(String strDbUrl)
	{

	}

	protected String ShowResult()
	{
		return "";
	}

	private void SaveExecSql(ExecSqlInfo  cExecSqlInfo, Connection conn_develop, CompareInfo cCompareInfo,String strMsg)
	{
		// 检查数据库是否存在
		
		try
		{
			String strSql="insert into dd.dbchangehis(table_name,exectype,domain,changesql,state_date,reversesql,execmsg)values('"+cExecSqlInfo.getStrTable()+"',"+cExecSqlInfo.getiType()+",'"+cCompareInfo.getStrDomain()+"','"+  cExecSqlInfo.getStrSql()+"',now(),'','"+strMsg+"')";
			JpfDbUtils.ExecUpdateSql(conn_develop, strSql);

		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
	}
	private void CheckSaveSqlDb(Connection conn_develop)
	{
		// 检查数据库是否存在
		
		try
		{
			String strSql = "CREATE DATABASE IF NOT EXISTS dd default charset utf8 COLLATE utf8_general_ci;";
			JpfDbUtils.ExecUpdateSql(conn_develop, strSql);
			strSql = "CREATE TABLE if not exists dd.dbchangehis (table_name  VARCHAR(128), exectype smallint,domain VARCHAR(45), changesql VARCHAR(1024) NOT NULL,state_date DATETIME NOT NULL,reversesql VARCHAR(45),execmsg VARCHAR(512));";
			JpfDbUtils.ExecUpdateSql(conn_develop, strSql);

		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
	}
	/**
	 * 
	 */
	public void DoCompare(Connection conn_product, Connection conn_develop, CompareInfo cCompareInfo)
	{
		// TODO Auto-generated constructor stub
		try
		{
			long sTime = System.currentTimeMillis();
			CompareUtil.CleanBuf(cCompareInfo.getStrDomain());
			DoWork(conn_product, conn_develop, cCompareInfo);

			if (cCompareInfo.getDoExec() == 1)
			{
				CheckSaveSqlDb(conn_develop);
				for (int i = 0; i < vSql.size(); i++)
				{
					System.out.println(vSql.get(i));
					try
					{
						//JpfDbUtils.ExecUpdateSql(conn_develop, vSql.get(i));
						SaveExecSql(vSql.get(i),conn_develop,cCompareInfo,null);
					} catch (Exception ex)
					{
						// TODO: handle exception
						ex.printStackTrace();
						SaveExecSql(vSql.get(i),conn_develop,cCompareInfo,ex.getLocalizedMessage());
					}
				}
			}
			CompareUtil.SendMail(sb, GetMailTitle(), cCompareInfo, GetHtmlName(), ShowResult());

			InsertResult(cCompareInfo.getStrJdbcUrl());
			long eTime = System.currentTimeMillis();
			logger.info("处理用时(单位MS):" + (eTime - sTime));
		} catch (SQLException ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			JpfMail.SendMail(cCompareInfo.getStrMails(), ex.getMessage() + "<br>" + cCompareInfo.getStrJdbcUrl()+"<br>ErrorCode:"+ex.getErrorCode(),
					"GBK", "数据库比对SQL异常");
		
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			JpfMail.SendMail("", ex.getMessage() + "<br>" + cCompareInfo.getStrJdbcUrl(),
					"GBK", "数据库比对异常");
		}

	}

	public AbstractDbCompare()
	{

	}

}
