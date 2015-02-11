/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2015��2��8�� ����10:34:34 
* ��˵�� 
*/ 

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.ci.dbs.DbUtils;
import org.jpf.utils.DateTimeUtil;

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
	public AbstractCheckTable()
	{
		// TODO Auto-generated constructor stub
		try
		{
			DoCheck(); // �Ƚ����ݿ�
			CompareUtil.writeFile(GetCheckTitle(),sb); // д���ļ�
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}

		System.out.println("game over");
	}
	public void DoCheck() throws Exception
	{
		Connection trans_product = null;
		try
		{
			// �������ݿ�����
			trans_product = DbInfo.GetInstance().getTransaction_product();

			Statement stmt = trans_product.createStatement();
			String sSql = GetCheckSql();
			logger.info(sSql);
			ResultSet rs = stmt.executeQuery(sSql);
			
			sb.append(GetCheckTitle()).append("\n").append(DateTimeUtil.getToday()).append("\n");
			
			FormatOutput(rs);

		} catch (Exception ex)
		{
			ex.printStackTrace();
			logger.error(ex);
		} finally
		{
			DbUtils.DoClear(trans_product);
		}
	}
	

}
