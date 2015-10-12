/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年2月8日 下午11:07:00 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDateTimeUtil;

/**
 * @category 基础类
 */
public abstract class AbstractCheckTable
{
	private static final Logger logger = LogManager.getLogger();
	public StringBuffer sb = new StringBuffer();

	abstract String GetCheckSql();

	abstract String GetCheckTitle();

	abstract void FormatOutput(ResultSet rs) throws Exception;

	/**
	 * 
	 */
	public AbstractCheckTable()
	{
	}

	public void doCheck(Connection conn, String strSqlParam)
	{
		// TODO Auto-generated constructor stub
		try
		{
			doWork(conn, strSqlParam); 
			//CompareUtil.writeFile(GetCheckTitle(), sb); 
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}

	}

	private void doWork(Connection conn, String strSqlParam) throws Exception
	{
		String sSql = GetCheckSql();
		logger.debug(sSql);
		PreparedStatement pStmt = conn.prepareStatement(sSql);
		if (null != strSqlParam)
		{
			pStmt.setString(1, strSqlParam);
		}
		ResultSet rs = pStmt.executeQuery();

		sb.append(GetCheckTitle()).append("\n").append(JpfDateTimeUtil.GetToday()).append("\n");

		FormatOutput(rs);

	}
}
