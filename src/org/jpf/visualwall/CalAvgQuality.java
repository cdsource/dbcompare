/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2014年3月28日 下午5:58:36 
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
public class CalAvgQuality
{
	private static final Logger logger =  LogManager.getLogger(CalAvgQuality.class);

	/**
	 * 
	 */
	public CalAvgQuality()
	{
		// TODO Auto-generated constructor stub
		Connection conn = null;
		logger.info("CalQuality");
		try
		{
			String strStartDate = "2014-10-08";
			String strEndDate = "2014-12-31";
			conn = AppConn.GetInstance().GetConn();
			conn.setAutoCommit(false);
			java.sql.Statement stmt = conn.createStatement();
			String strSql = "delete from hz_rpt where   DATE_FORMAT(build_date,'%Y-%m-%d')<='" + strEndDate + "' and DATE_FORMAT(build_date,'%Y-%m-%d')>='"+strStartDate+"'";
			logger.info(strSql);
			stmt.executeUpdate(strSql);
			strSql = "SELECT * FROM hz_prj WHERE kpi IS NOT NULL";
			logger.info(strSql);
			java.sql.ResultSet rSet = stmt.executeQuery(strSql);
			while (rSet.next())
			{
				strSql = "insert into hz_rpt1(prj_id,build_date,prj_name,is_last,branch_coverage) "
						+ " select t2.project_id,?,t4.prj_name,1 e, case t3.metric_id when 50 then t3.value end as branch_coverage"
						+ " from snapshots t2,project_measures t3,hz_prj t4 where t2.project_id=?"
						+ " and DATE_FORMAT(t2.build_date,'%Y-%m-%d')<=? and t2.id=t3.snapshot_id AND t3.rule_id IS NULL  and"
						+ " t3.metric_id in (50) and t4.prj_id=t2.project_id order by t2.build_date desc limit 1";
				java.sql.PreparedStatement pStatement = conn.prepareStatement(strSql);
				logger.info(strSql);
				long lDays = JpfDateTimeUtil.GetDays(strStartDate, strEndDate);
				for (int i = 0; i < lDays; i++)
				{
					//logger.info(DateTimeUtil.AddSqlDate(strStartDate, i));
					pStatement.setDate(1, JpfDateTimeUtil.AddSqlDate(strStartDate, i));
					pStatement.setLong(2, rSet.getLong("prj_id"));
					pStatement.setDate(3, JpfDateTimeUtil.AddSqlDate(strStartDate, i));
					pStatement.addBatch();
				}

				pStatement.executeBatch();
			}
			
			strSql = "insert into hz_rpt select distinct * from hz_rpt1";
			logger.info(strSql);
			stmt.executeUpdate(strSql);
			
			strSql = "insert into hz_rpt(prj_id,build_date,prj_name,is_last,branch_coverage) select 0,build_date,'avg',1,avg(branch_coverage) from hz_rpt group by build_date ";
			logger.info(strSql);
			stmt.executeUpdate(strSql);
			
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

	/**
	 * @param args
	 *            被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注：
	 *            update 2014年3月28日
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		CalAvgQuality cCalAvgQuality = new CalAvgQuality();
	}

}
