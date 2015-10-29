/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年2月8日 下午11:07:00 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.Vector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.mails.JpfMail;
import org.jpf.utils.JpfDateTimeUtil;
import org.jpf.utils.JpfDbUtils;
import org.jpf.utils.OsUtil;

/**
 * 
 */
public abstract class AbstractDbCompare
{
	private static final Logger logger = LogManager.getLogger();


	protected StringBuffer[] sb = { new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer(), new StringBuffer(), new StringBuffer(), new StringBuffer(),
			new StringBuffer() };
<<<<<<< HEAD
	
	protected StringBuffer[] errInfoSb = { new StringBuffer(), new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer(), new StringBuffer() };
=======
>>>>>>> origin/master

	//域名-表1或域名-索引1
	protected  LinkedHashMap<String, TreeMap<String ,Table>> domain_table=new  LinkedHashMap<String, TreeMap<String ,Table>>();
	//域名-表2或域名-索引2
	protected  LinkedHashMap<String, TreeMap<String ,Table>> domain_table2=new  LinkedHashMap<String, TreeMap<String ,Table>>();
	
		
	protected Vector<ExecSqlInfo> vSql = new Vector<ExecSqlInfo>();

	// 保存执行错误的SQL
	protected Vector<ErrExecSqlInfo> vErrSql = new Vector<ErrExecSqlInfo>();

<<<<<<< HEAD
	//数据库与数据库比对
	abstract void doWork(Connection conn_product, Connection conn_develop, CompareInfo cCompareInfo) throws Exception;
	//pdm与数据库比对
	abstract void doWork(Connection conn_develop, CompareInfo cCompareInfo) throws Exception;
	//pdm与pdm比对
	abstract void doWork(CompareInfo cCompareInfo)throws Exception;
	// abstract void DoWork(Map<String,Table> map, Connection conn_develop,
	// CompareInfo cCompareInfo) throws Exception;

	abstract String getHtmlName();
	
	abstract String getErrorHtmlName(); 
=======
	abstract void DoWork(Connection conn_product, Connection conn_develop, CompareInfo cCompareInfo) throws Exception;
	abstract void DoWork(Connection conn_develop, CompareInfo cCompareInfo) throws Exception;
	abstract void DoWork(CompareInfo cCompareInfo)throws Exception;
	// abstract void DoWork(Map<String,Table> map, Connection conn_develop,
	// CompareInfo cCompareInfo) throws Exception;
>>>>>>> origin/master

	abstract String getExecSqlHtmlName();

<<<<<<< HEAD
	abstract String getMailTitle();
=======
	abstract String GetExecSqlHtmlName();

	abstract String GetMailTitle();
>>>>>>> origin/master

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

<<<<<<< HEAD
	private String strSaveDate = JpfDateTimeUtil.getCurrDateTime();
=======
	private String strSaveDate = JpfDateTimeUtil.GetCurrDateTime();
>>>>>>> origin/master
	
	public LinkedHashMap<String, TreeMap<String, Table>> getDomain_table2() {
		return domain_table2;
	}
	public void setDomain_table2(
			LinkedHashMap<String, TreeMap<String, Table>> domain_table2) {
		this.domain_table2 = domain_table2;
	}
	public LinkedHashMap<String, TreeMap<String, Table>> getDomain_table() {
		return domain_table;
	}
	public void setDomain_table(
			LinkedHashMap<String, TreeMap<String, Table>> domain_table) {
		this.domain_table = domain_table;
	}
<<<<<<< HEAD
	protected void insertResult(CompareInfo cCompareInfo)
=======
	protected void InsertResult(CompareInfo cCompareInfo)
>>>>>>> origin/master
	{

	}

	protected String showResult()
	{
		return "";
	}
<<<<<<< HEAD
	String getMailTitle_pdm()
	{
		return "PDM与PDM比对(自动发出)";
	}
	
	
=======
	String GetMailTitle_pdm()
	{
		return "PDM与PDM比对(自动发出)";
	}
>>>>>>> origin/master

	/**
	 * @todo: 保存变更记录
	 */
<<<<<<< HEAD
	private void saveExecSql(ExecSqlInfo cExecSqlInfo, Connection conn_develop, CompareInfo cCompareInfo, String strMsg)
=======
	private void SaveExecSql(ExecSqlInfo cExecSqlInfo, Connection conn_develop, CompareInfo cCompareInfo, String strMsg)
>>>>>>> origin/master
	{
		String strSql="";
		try
		{
			cExecSqlInfo.setStrSql(cExecSqlInfo.getStrSql().replaceAll("`", ""));
			cExecSqlInfo.setStrSql(cExecSqlInfo.getStrSql().replaceAll("'", ""));
			if (strMsg != null && strMsg.length() > 0)
			{
				strMsg = strMsg.replaceAll("'", "");
			}
			strSql = "insert into dd.dbchangehis(table_name,exectype,domain,changesql,state_date,reversesql,execmsg)values('"
					+ cExecSqlInfo.getStrTable()
					+ "',"
					+ cExecSqlInfo.getiType()
					+ ",'"
					+ cCompareInfo.getDbDomain()
					+ "','" + cExecSqlInfo.getStrSql() + "',now(),'','" + strMsg + "')";
<<<<<<< HEAD
			JpfDbUtils.execUpdateSql(conn_develop, strSql,0);
=======
			JpfDbUtils.ExecUpdateSql(conn_develop, strSql,0);
>>>>>>> origin/master

		} catch (Exception ex)
		{
			// TODO: handle exception
			logger.error(strSql);
			ex.printStackTrace();
		}
	}

	/**
	 * @todo: 执行差异的SQL
	 */
<<<<<<< HEAD
	private void execDiffSql(Connection conn_develop, CompareInfo cCompareInfo)
=======
	private void ExecDiffSql(Connection conn_develop, CompareInfo cCompareInfo)
>>>>>>> origin/master
	{
		if (cCompareInfo.getDoExec() == 1)
		{

<<<<<<< HEAD
			CompareSelfCheck.checkSaveSqlDb(conn_develop);

=======
			CompareSelfCheck.CheckSaveSqlDb(conn_develop);

			// for (int i = 0; i < vSql.size(); i++)
>>>>>>> origin/master
			long sTime = System.currentTimeMillis();
			String strMsg = "";
			for (int i = 0; i < vSql.size(); i++)
			{
				if (i % 100 == 0 && i>0)
				{
					long eTime = System.currentTimeMillis();
					logger.info(i + "/" + vSql.size() + " 处理用时(单位MS):" + (eTime - sTime));
					sTime = System.currentTimeMillis();
				}
				strMsg = "";

				ExecSqlInfo cExecSqlInfo = vSql.get(i);
				try
				{
					if (cExecSqlInfo.getStrSql().startsWith("DROP"))
					{
						ErrExecSqlInfo cErrExecSqlInfo = new ErrExecSqlInfo(cExecSqlInfo.getStrSql(), "no exec");
						vErrSql.add(cErrExecSqlInfo);
					} else
					{
<<<<<<< HEAD
						JpfDbUtils.execUpdateSql(conn_develop, cExecSqlInfo.getStrSql());
=======
						JpfDbUtils.ExecUpdateSql(conn_develop, cExecSqlInfo.getStrSql());
>>>>>>> origin/master
					}

				} catch (Exception ex)
				{
					// TODO: handle exception
					ex.printStackTrace();
					strMsg = ex.getMessage();
					ErrExecSqlInfo cErrExecSqlInfo = new ErrExecSqlInfo(cExecSqlInfo.getStrSql(), strMsg);
					vErrSql.add(cErrExecSqlInfo);
				}
				try
				{
<<<<<<< HEAD
					saveExecSql(vSql.get(i), conn_develop, cCompareInfo, strMsg);
=======
					SaveExecSql(vSql.get(i), conn_develop, cCompareInfo, strMsg);
>>>>>>> origin/master
				} catch (Exception ex2)
				{
					// TODO: handle exception
					ex2.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 */
	public void doCompare(Connection conn_product, Connection conn_develop, CompareInfo cCompareInfo)
	{
		// TODO Auto-generated constructor stub
		try
		{
			long sTime = System.currentTimeMillis();
			if(conn_product==null&&conn_develop==null)
			{
<<<<<<< HEAD
				doWork(cCompareInfo);
				CompareUtil.pdmComSendMail(sb, getMailTitle_pdm(), cCompareInfo, getHtmlName(), showResult());
			}
			else if(conn_product==null)
			{
				CompareUtil.cleanBuf(cCompareInfo.getDbDomain());
				doWork(conn_develop, cCompareInfo);
				execDiffSql(conn_develop, cCompareInfo);
				if (cCompareInfo.getDoExec() == 1)
				{
					CompareUtil.sendExecSqlMail(vErrSql, getMailTitle(), cCompareInfo, getExecSqlHtmlName(), showResult());
				} else
				{
					CompareUtil.sendMail(sb, getMailTitle(), cCompareInfo, getHtmlName(), showResult());
=======
				DoWork(cCompareInfo);
				CompareUtil.PdmComSendMail(sb, GetMailTitle_pdm(), cCompareInfo, GetHtmlName(), ShowResult());
			}
			else if(conn_product==null)
			{
				CompareUtil.CleanBuf(cCompareInfo.getDbDomain());
				DoWork(conn_develop, cCompareInfo);
				ExecDiffSql(conn_develop, cCompareInfo);
				if (cCompareInfo.getDoExec() == 1)
				{
					CompareUtil.SendExecSqlMail(vErrSql, GetMailTitle(), cCompareInfo, GetExecSqlHtmlName(), ShowResult());
				} else
				{
					CompareUtil.SendMail(sb, GetMailTitle(), cCompareInfo, GetHtmlName(), ShowResult());
>>>>>>> origin/master
				}

			}
			else
			{
<<<<<<< HEAD
				CompareUtil.cleanBuf(cCompareInfo.getDbDomain());
				doWork(conn_product, conn_develop, cCompareInfo);
				execDiffSql(conn_develop, cCompareInfo);
				if (cCompareInfo.getDoExec() == 1)
				{
					CompareUtil.sendExecSqlMail(vErrSql, getMailTitle(), cCompareInfo, getExecSqlHtmlName(), showResult());
				} else
				{
					CompareUtil.sendMail(sb, getMailTitle(), cCompareInfo, getHtmlName(), showResult());
=======
				CompareUtil.CleanBuf(cCompareInfo.getDbDomain());
				DoWork(conn_product, conn_develop, cCompareInfo);
				ExecDiffSql(conn_develop, cCompareInfo);
				if (cCompareInfo.getDoExec() == 1)
				{
					CompareUtil.SendExecSqlMail(vErrSql, GetMailTitle(), cCompareInfo, GetExecSqlHtmlName(), ShowResult());
				} else
				{
					CompareUtil.SendMail(sb, GetMailTitle(), cCompareInfo, GetHtmlName(), ShowResult());
>>>>>>> origin/master
				}

				
			}
			
<<<<<<< HEAD
=======
			
>>>>>>> origin/master
			sb=null;
			domain_table=null;
			domain_table2=null;

<<<<<<< HEAD
			insertResult(cCompareInfo);
=======
			//InsertResult(cCompareInfo);
>>>>>>> origin/master
			long eTime = System.currentTimeMillis();
			logger.info("处理用时(单位MS):" + (eTime - sTime));
		} catch (SQLException ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			JpfMail.sendMail(cCompareInfo.getStrMails(), ex.getMessage() + "<br>" + cCompareInfo.getDevJdbcUrl()
					+ "<br>ErrorCode:" + ex.getErrorCode()
					+ "<br>Compare Database:" + cCompareInfo.getDbDomain()
					+ "<br>ip:" + OsUtil.getLocalIP(),
					"GBK", cCompareInfo.getStrCondName() + "数据库比对SQL异常");

		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			JpfMail.sendMail("", ex.getMessage() + "<br>" + cCompareInfo.getDevJdbcUrl(),
					"GBK", "数据库比对异常");
		}

	}

	public AbstractDbCompare()
	{

	}

}
