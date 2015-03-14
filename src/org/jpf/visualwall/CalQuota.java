/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2014年3月22日 下午10:39:57 
 * 类说明 
 */

package org.jpf.visualwall;

import java.sql.Connection;






import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.ci.AppConn;
import org.jpf.utils.JpfDateTimeUtil;
import org.jpf.utils.JpfDbUtils;


/**
 * 
 */
public class CalQuota
{
	private static final Logger logger =  LogManager.getLogger();
	/**
	 * @param args
	 *            被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注：
	 *            update 2014年3月22日
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		if (2 == args.length)
		{
			CalQuota cCalQuota = new CalQuota(args[0], args[1]);
		}

	}

	public CalQuota(String strStartDate, String strEndDate)
	{

		Connection conn = null;
		try
		{
			conn = AppConn.GetInstance().GetConn();
			conn.setAutoCommit(false);
			System.out.println("strStartDate:="+strStartDate);
			System.out.println("strEndDate:="+strEndDate);
			long lDays = JpfDateTimeUtil.GetDays(strStartDate, strEndDate);
			int prj_id = 0;
			//-2.54
			double dKpiStart = 55;
			double dKpiEnd = 58;
			double dKpiChange = (dKpiEnd - dKpiStart) / lDays;
			String strSql="delete from hz_rpt_date where DATE_FORMAT(build_date,'%Y-%m-%d')>='"+strStartDate+"'";  
			logger.info(strSql);
			java.sql.Statement statement = conn.createStatement();
			statement.executeUpdate(strSql);
			strSql = "insert into hz_rpt_date(build_date,prj_id,refer_value)values(?,?,?)";
			logger.info(strSql);
			java.sql.PreparedStatement pStatement = conn.prepareStatement(strSql);

			for (int i = 0; i <= lDays; i++)
			{
				pStatement.setDate(1, JpfDateTimeUtil.AddSqlDate(strStartDate, i));
				pStatement.setInt(2, prj_id);
				pStatement.setDouble(3, dKpiStart + dKpiChange * i);
				pStatement.addBatch();
			}
			
			//java.sql.ResultSet rSet = statement.executeQuery("select prj_id,kpi,original_value from hz_prj where kpi is not null");
			java.sql.ResultSet rSet = statement.executeQuery("select prj_id,kpi4,kpi from hz_prj where kpi is not null");
			while (rSet.next())
			{
				prj_id = rSet.getInt("prj_id");
				logger.info("prj_id="+prj_id);
				dKpiStart = rSet.getDouble(3);
				dKpiEnd = rSet.getDouble(2);
				dKpiChange = (dKpiEnd - dKpiStart) /lDays;
				for (int i = 0; i <= lDays; i++)
				{

					pStatement.setDate(1, JpfDateTimeUtil.AddSqlDate(strStartDate, i));
					pStatement.setInt(2, prj_id);
					pStatement.setDouble(3, dKpiStart + dKpiChange * i);
					System.out.println(JpfDateTimeUtil.AddSqlDate(strStartDate, i));
					System.out.println(dKpiStart + dKpiChange * i);
					pStatement.addBatch();
				}
			}

			
			pStatement.executeBatch();
			conn.commit();
			System.out.println("game over");
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		} finally
		{
			JpfDbUtils.DoClear(conn);
		}
	}
}
