/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2014年3月22日 下午10:39:57 
 * 类说明 
 */

package org.jpf.visualwall.precals;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDateTimeUtil;
import org.jpf.utils.JpfDbUtils;
import org.jpf.visualwall.WallsDbConn;

/**
 * 
 */
public class CalQuota2
{
	private static final Logger logger = LogManager.getLogger();
	private final String AREA_KEY = "HZ";

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
			CalQuota2 cCalQuota = new CalQuota2(args[0], args[1]);
		}

	}

	public CalQuota2(String strStartDate, String strEndDate)
	{

		Connection conn = null;
		try
		{
			conn = WallsDbConn.GetInstance().GetConn();
			conn.setAutoCommit(false);
			logger.info(AREA_KEY);
			System.out.println("strStartDate:=" + strStartDate);
			System.out.println("strEndDate:=" + strEndDate);
			long lDays = JpfDateTimeUtil.GetDays(strStartDate, strEndDate);
			// -2.54
			double dKpiAllStart = 0;
			double dKpiAllEnd = 0;
			double dKpiChange = 0;
			String strSql = "delete from bss_rpt_date where areaname='" + AREA_KEY
					+ "' and DATE_FORMAT(build_date,'%Y-%m-%d')>='"
					+ strStartDate + "' and DATE_FORMAT(build_date,'%Y-%m-%d')<='" + strEndDate + "'";
			logger.info(strSql);
			java.sql.Statement statement = conn.createStatement();
			statement.executeUpdate(strSql);
			strSql = "insert into bss_rpt_date(build_date,prj_id,refer_value,areaname)values(?,?,?,?)";
			logger.info(strSql);
			java.sql.PreparedStatement pStatement = conn.prepareStatement(strSql);

			int prj_id = 0;
			// java.sql.ResultSet rSet =
			// statement.executeQuery("select prj_id,kpi,original_value from hz_prj where kpi is not null");
			strSql = "select prj_id,kpi4,kpi from bss_prj where areaname='" + AREA_KEY
					+ "' and kpi is not null order by prj_id";
			// strSql =
			// "select prj_id,kpi4,kpi from bss_prj where areaname='"+AREA_KEY+"' and prj_id=172388 order by prj_id";
			logger.info(strSql);
			java.sql.ResultSet rSet = statement.executeQuery(strSql);
			while (rSet.next())
			{
				prj_id = rSet.getInt("prj_id");
				logger.info("prj_id=" + prj_id);
				double dKpiStart = rSet.getDouble(3);
				double dKpiEnd = rSet.getDouble(2);
				dKpiAllStart += dKpiStart;
				dKpiAllEnd += dKpiEnd;
				dKpiChange = (dKpiEnd - dKpiStart) / lDays;
				for (int i = 0; i <= lDays; i++)
				{

					pStatement.setDate(1, JpfDateTimeUtil.AddSqlDate(strStartDate, i));
					pStatement.setInt(2, prj_id);
					pStatement.setDouble(3, dKpiStart + dKpiChange * i);
					pStatement.setString(4, AREA_KEY);
					pStatement.addBatch();
				}
			}

			prj_id = 0;
			dKpiAllEnd = 57;
			dKpiAllStart =58 ;
			dKpiChange = (dKpiAllEnd - dKpiAllStart) / lDays;
			for (int i = 0; i <= lDays; i++)
			{
				pStatement.setDate(1, JpfDateTimeUtil.AddSqlDate(strStartDate, i));
				pStatement.setInt(2, prj_id);
				pStatement.setDouble(3, dKpiAllStart + dKpiChange * i);
				pStatement.setString(4, AREA_KEY);
				pStatement.addBatch();
			}
			pStatement.executeBatch();
			if (!conn.getAutoCommit())
			{
				conn.commit();
			}
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
