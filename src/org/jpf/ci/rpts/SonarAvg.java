/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2013-4-12 下午6:55:39 
 * 类说明 
 */

package org.jpf.ci.rpts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;





import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.ci.AppConn;
import org.jpf.utils.JpfDbUtils;
import org.jpf.utils.conf.ConfigProp;

/**
 * 
 */
public class SonarAvg
{
	private static final Logger logger = LogManager.getLogger();

	public SonarAvg()
	{
		Connection conn = null;
		try
		{
			String strEXCLUDE_PRJ_ID=ConfigProp.GetStrFromConfig("sonar_hz.properties", "EXCLUDE_PRJ_ID");
			logger.info("strEXCLUDE_PRJ_ID={}",strEXCLUDE_PRJ_ID);
			String strEXCLUDE_PRJ_ID_Cond="";
			if (strEXCLUDE_PRJ_ID!=null && strEXCLUDE_PRJ_ID.trim().length()>0)
			{
				strEXCLUDE_PRJ_ID_Cond=" and t1.id not in("+strEXCLUDE_PRJ_ID+")";
				logger.info("strEXCLUDE_PRJ_ID_Cond={}",strEXCLUDE_PRJ_ID_Cond);
			}else {
				throw new Exception("no EXCLUDE_PRJ_ID config,check sonar_hz.properties");
			}
			String strMetricString=ConfigProp.GetStrFromConfig("sonar_hz.properties", "PRJ_AVG_METRIC");
			//int[] metrics = { 24,37,43,75,78,80,81,82,139,140,141,142,138,148,161 };
			//strMetricString="1";
			logger.info("metric_id="+strMetricString);
			String[] metrics=strMetricString.split(",");
			//String strSql = "select count(*) from projects  t1 where scope='PRJ' and enabled=1 and qualifier='TRK' "+strEXCLUDE_PRJ_ID_Cond;
			String strSql="select count(*) from hz_prj";
			logger.debug("strSql=" + strSql);
			conn = AppConn.GetInstance().GetConn();
			conn.setAutoCommit(false);
			PreparedStatement pStmt = conn.prepareStatement(strSql);
			ResultSet rs = pStmt.executeQuery();
			
			String dValue = "";
			String cValue = "";
			int iCount = 0;
			if (rs.next())
			{
				iCount = rs.getInt(1);
			}

			logger.debug("iCount=" + iCount);

			for (int i = 0; i < metrics.length; i++)
			{
				strSql = "select sum(t3.value)/" + iCount + ",sum(t3.variation_value_1)/"+iCount+" from projects t1,snapshots t2,project_measures t3"
						+ " where  t1.id=t2.project_id and t2.islast=1 and t2.id=t3.snapshot_id  and t3.metric_id ="
						+ metrics[i]
						+ " and t1.scope='PRJ' and t1.enabled=1 and t1.qualifier='TRK' "+strEXCLUDE_PRJ_ID_Cond;
				logger.debug("strSql=" + strSql);
				pStmt = conn.prepareStatement(strSql);
				rs = pStmt.executeQuery();
				if (rs.next())
				{
					dValue = rs.getString(1);
					cValue =rs.getString(2);
				}
				strSql = "update project_measures set value=" + dValue + ",variation_value_1="+cValue+" where metric_id="
						+ metrics[i]+" and snapshot_id in (select id from snapshots where project_id=80664 and islast=1)";
				logger.debug("strSql=" + strSql);
				pStmt.executeUpdate(strSql);

				conn.commit();
			}
			rs.close();
			strSql = "update snapshots set created_at=now(),build_date=CURRENT_DATE where islast=1 and project_id =80664";
			logger.debug("strSql=" + strSql);
			pStmt.executeUpdate(strSql);
			pStmt.close();
			conn.commit();
			System.out.println("game over");
		} catch (Exception ex)
		{
			ex.printStackTrace();
			logger.error(ex);
		} finally
		{
			JpfDbUtils.DoClear(conn);
		}
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		SonarAvg cSonarAvg = new SonarAvg();
		/*
		double a=10;
		double b=3;
		a=a/b;
		System.out.println(a);
		*/
	}


}
