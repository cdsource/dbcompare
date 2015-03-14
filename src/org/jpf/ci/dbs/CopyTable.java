/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2014��10��20�� ����9:21:10 
* ��˵�� 
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
	 * ������������TODO
	 * �����Խӿ���:TODO
	 * ���Գ�����TODO
	 * ǰ�ò�����TODO
	 * ��Σ�
	 * У��ֵ��
	 * ���Ա�ע��
	 * update 2014��10��20��
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
