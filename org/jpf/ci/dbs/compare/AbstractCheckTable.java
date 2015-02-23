/** 
* @author 吴平福 
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年2月8日 下午11:07:00 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDateTimeUtil;

/**
 * 
 */
public abstract class AbstractCheckTable
{
	private static final Logger logger = LogManager.getLogger();
	public StringBuffer sb = new StringBuffer();
	abstract String GetCheckSql();
	abstract String GetCheckTitle();
	abstract void FormatOutput(ResultSet rs)throws Exception;
	/**
	 * 
	 */
	public AbstractCheckTable(){}
	

	public void DoCheck(Connection conn)
	{
		// TODO Auto-generated constructor stub
		try
		{
			DoWork(conn); // �Ƚ����ݿ�
			CompareUtil.writeFile(GetCheckTitle(),sb); // д���ļ�
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}

	}
	
	private void DoWork(Connection conn) throws Exception
	{
			Statement stmt = conn.createStatement();
			String sSql = GetCheckSql();
			logger.info(sSql);
			ResultSet rs = stmt.executeQuery(sSql);
			
			sb.append(GetCheckTitle()).append("\n").append(JpfDateTimeUtil.GetToday()).append("\n");
			
			FormatOutput(rs);


	}
	

}
