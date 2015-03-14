/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2014年11月28日 下午1:12:38 
 * 类说明 
 */

package org.jpf.visualwall.gathers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDbUtils;

/**
 * 
 */
public abstract class AbstractQualityInfo
{
	private static final Logger logger = LogManager.getLogger();

	/**
	 * 
	 */
	public AbstractQualityInfo()
	{
		// TODO Auto-generated constructor stub
		GetQualityInfo();
	}

	abstract String GetDBUsr();

	abstract String GetDBPwd();

	abstract String GetDBUrl();

	abstract String GetQuerySql();

	abstract String GetAreaName();

	abstract int GetPrjCount();

	abstract String GetExcludePrjSql();

	protected String GetInsertSql(ResultSet rSet) throws SQLException
	{
		// TODO Auto-generated method stub
		String strSql = "insert into bss_rpt (areaname,prj_id,build_date,prj_name,is_last,blocker_violations,critical_violations,major_violations,prj_lines,tests,test_success_density,branch_coverage,line_coverage,state_date) "
				+ " values('"
				+ GetAreaName() + "'," + rSet.getLong("prj_id")
				+ ",'" + rSet.getString("build_date")
				+ "','" + rSet.getString("prj_name")
				+ "',1," + rSet.getLong("blocker_violations")
				+ "," + rSet.getLong("critical_violations")
				+ "," + rSet.getLong("major_violations")
				+ "," + rSet.getLong("prj_lines")
				+ "," + rSet.getLong("tests")
				+ "," + rSet.getDouble("test_success_density")
				+ "," + rSet.getDouble("branch_coverage")
				+ "," + rSet.getDouble("line_coverage") + ",now())";
		return strSql;
	}

	protected String GetDelSql(ResultSet rSet) throws SQLException
	{
		// TODO Auto-generated method stub
		String strSql = "delete from bss_rpt where areaname='" + GetAreaName() + "' and prj_id="
				+ rSet.getLong("prj_id")
				+ "  and build_date='" + rSet.getString("build_date") + "'";
		return strSql;
	}

	protected String GetUpdateSql(ResultSet rSet) throws SQLException
	{
		// TODO Auto-generated method stub
		String strSql = "update bss_rpt set is_last=0 where areaname='" + GetAreaName() + "' and prj_id="
				+ rSet.getLong("prj_id")
				+ " and is_last=1 and build_date<'" + rSet.getString("build_date") + "'";
		return strSql;
	}

	protected String GetUpdate2Sql(ResultSet rSet) throws SQLException
	{
		// TODO Auto-generated method stub
		String strSql = "update bss_rpt set is_last=1 where areaname='" + GetAreaName() + "' and prj_id="
				+ rSet.getLong("prj_id")
				+ " and build_date='" + rSet.getString("build_date") + "'";
		return strSql;
	}

	protected Connection GetSourceConn() throws Exception
	{
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver).newInstance();
		// 10.10.13.90:6511 boss/boss6511
		// String URL="jdbc:mysql://10.10.12.150:4306/"+strDb;
		// String dbuser = "billing";
		// String dbpass = "billing";
		return DriverManager.getConnection(GetDBUrl(), GetDBUsr(), GetDBPwd());
	}

	private void AddBatchSql(Connection conn, String strSql) throws Exception
	{
		
		if (strSql.trim().toLowerCase().startsWith("insert"))
		{
			strSql = strSql.replaceAll("bss_rpt", "bss_rpt_bak");
			//logger.info(strSql);
			//WallsDbConn.GetInstance().ExecUpdateSql(conn, strSql);
		}
		
	}

	public void GetQualityInfo()
	{
		Connection conn1 = null;
		Connection conn2 = null;
		try
		{
			conn1 = this.GetSourceConn();
			conn2 = WallsDbConn.GetInstance().GetConn();

			Statement stmt1 = conn1.createStatement();

			ResultSet rSet = stmt1.executeQuery(GetQuerySql());
			String strSql = "";
			while (rSet.next())
			{
				strSql = GetDelSql(rSet);
				JpfDbUtils.ExecUpdateSql(conn2, strSql);
				AddBatchSql(conn2, strSql);

				strSql = GetUpdateSql(rSet);
				JpfDbUtils.ExecUpdateSql(conn2, strSql);
				AddBatchSql(conn2, strSql);

				strSql = GetInsertSql(rSet);
				JpfDbUtils.ExecUpdateSql(conn2, strSql);
				AddBatchSql(conn2, strSql);

				strSql = GetUpdate2Sql(rSet);
				JpfDbUtils.ExecUpdateSql(conn2, strSql);
				AddBatchSql(conn2, strSql);

			}

			strSql = "delete from bss_rpt  where prj_id>0 and  areaname='" + GetAreaName()
					+ "' and prj_id not in (select prj_id from bss_prj t2 where t2.areaname='" + GetAreaName() + "')";
			JpfDbUtils.ExecUpdateSql(conn2, strSql);
			AddBatchSql(conn2, strSql);

			strSql = "delete from bss_rpt where prj_id=0 and is_last=1 and build_date=CURRENT_DATE and areaname='" + GetAreaName() + "'";
			JpfDbUtils.ExecUpdateSql(conn2, strSql);
			AddBatchSql(conn2, strSql);

			strSql = "insert into bss_rpt(areaname,prj_id,build_date,prj_name,is_last,blocker_violations,critical_violations,major_violations,prj_lines,tests,test_success_density,branch_coverage,prj_count) "
					+ " select '"
					+ GetAreaName()
					+ "',0,CURRENT_DATE,'avg',1,sum(blocker_violations)/"
					+ GetPrjCount()
					+ ",sum(critical_violations)/"
					+ GetPrjCount()
					+ ",sum(major_violations)/"
					+ GetPrjCount()
					+ ",sum(prj_lines)/"
					+ GetPrjCount()
					+ ",sum(tests)/"
					+ GetPrjCount()
					+ ",sum(test_success_density)/"
					+ GetPrjCount()
					+ ",sum(branch_coverage)/"
					+ GetPrjCount()
					+ ","
					+ GetPrjCount()
					+ " from (select distinct * from   bss_rpt  "
					+ " where is_last=1 and prj_id<>0 "
					+ GetExcludePrjSql()
					+ "and areaname='"
					+ GetAreaName()
					+ "')t1";

			JpfDbUtils.ExecUpdateSql(conn2, strSql);
			AddBatchSql(conn2, strSql);

			strSql = "update  bss_rpt set is_last=1 where prj_id=0 and build_date=CURRENT_DATE and areaname='"
					+ GetAreaName() + "'";
			JpfDbUtils.ExecUpdateSql(conn2, strSql);
			AddBatchSql(conn2, strSql);

			// WallsDbConn.GetInstance().ExecUpdateSql(conn2, strSql);
			// AddBatchSql(conn2, strSql);
			// stmt2.executeBatch();
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			logger.error(ex);
		} finally
		{
			JpfDbUtils.DoClear(conn1);
			JpfDbUtils.DoClear(conn2);
		}
		System.out.println("game over");
	}

}
