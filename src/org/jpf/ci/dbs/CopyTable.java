/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2014年10月20日 下午9:21:10 
* 类说明 
*/ 

package org.jpf.ci.dbs;

import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDbUtils;


/**
 * 
 */
public class CopyTable
{
	private static final Logger logger = LogManager.getLogger();
	/**
	 * @param args
	 * 被测试类名：TODO
	 * 被测试接口名:TODO
	 * 测试场景：TODO
	 * 前置参数：TODO
	 * 入参：
	 * 校验值：
	 * 测试备注：
	 * update 2014年10月20日
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		CopyTable cCopyTable =new CopyTable();
	}

	private String strCopyTableName="bs_billing_period";
	private String strDbName="bd";
	public CopyTable()
	{
		Connection ConnSource=null;
		Connection ConnDest=null;
		try
		{
			String strSql="SHOW CREATE TABLE "+strDbName+"."+strCopyTableName;
			logger.info(strSql);
			ConnSource=MutiDbConn.GetInstance().GetSourceConn(strDbName);
			java.sql.Statement stmt = ConnSource.createStatement();
			ResultSet rs=stmt.executeQuery(strSql);
			if(rs.next())
			{
				strSql=rs.getString(2);
			}
			logger.info(strSql);
			
			ConnDest=MutiDbConn.GetInstance().GetDestConn(strDbName);
			java.sql.Statement stmt2 = ConnDest.createStatement();
			stmt2.executeUpdate("drop table "+strDbName+"."+strCopyTableName);
			stmt2.executeUpdate(strSql);
			logger.info("game over");
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			logger.error(ex);
		}finally
		{
			JpfDbUtils.DoClear(ConnSource);
			JpfDbUtils.DoClear(ConnDest);
		}
	}
}
