/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2014年3月30日 下午8:57:35 
* 类说明 
*/ 

package org.jpf.ci.dbs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class DbRestore
{
	private static final Logger logger = LogManager.getLogger();
	/**
	 * 
	 */
	public DbRestore()
	{
		// TODO Auto-generated constructor stub
	}
	public int DoRestore(String strDbName)
	{
		try
		{
			Runtime rt = Runtime.getRuntime();
			String createDb = "mysqladmin -u" + DbChangeConn.GetInstance().GetUsr() + " -p" + DbChangeConn.GetInstance().GetPwd() + " create " + strDbName;
			String mysql = "mysql -u" + DbChangeConn.GetInstance().GetUsr() + " -p" + DbChangeConn.GetInstance().GetPwd() + " " + strDbName + " <" + "" + strDbName + ".sql";
			logger.info(mysql);
			rt.exec("cmd.exe /c " + createDb);
			Process proc = rt.exec("cmd.exe /c " + mysql);
			int tag = proc.waitFor();// 等待进程终止
		} catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
}
