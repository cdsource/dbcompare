/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2013-4-16 下午1:33:28 
 * 类说明 
 */

package org.jpf.ci.rpts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.ci.AppConn;
import org.jpf.mails.SonarMail;
import org.jpf.utils.JpfDateTimeUtil;
import org.jpf.utils.JpfDbUtils;
import org.jpf.utils.JpfFileUtil;

/**
 * 
 */
public class SendPrjMail
{
	private static final Logger logger = LogManager.getLogger(SendPrjMail.class);
	private String strDevHtmlString = "";
	private String strCurrPrjName = "";
	private String strSelectDate = "";

	/**
	 * 
	 */
	public SendPrjMail()
	{
		Connection conn = null;
		try
		{
			strSelectDate = JpfDateTimeUtil.GetToday();
			logger.info("strSelectDate=" + strSelectDate);
			strDevHtmlString = JpfFileUtil.GetFileTxt("prj.html");
			MakePrjAvgInfo();

			MakeModuleMail();
			// SendPrjMail2("wupf", strMailTxtString, " SONAR产品质量周报");
			System.out.println("game over");
		} catch (Exception ex)
		{
			logger.error(ex);
			ex.printStackTrace();
		} finally
		{
			JpfDbUtils.DoClear(conn);
		}
	}

	class ModuleInfo
	{
		String strName;
		long lcritical_violations, lcritical_violationsChange;
		long lmajor_violations, lmajor_violationsChange;
		double dline_coverage, dline_coverageChange;
		double dCoverage, dCoverageChange;
		double dbranch_coverage, dbranch_coverageChange;
		double dtest_success_density = 0.0, dtest_success_densityChange = 0.0;
		long ltests, ltestsChange;
		long lPrjLines, lPrjLinesChange;
		int iType = 1;
		double dUtcaseperline, dUtcaseperlineChange;

		private long GetLong(String inStr)
		{
			int iPos = inStr.indexOf(".");
			if (iPos > 0)
			{
				inStr = inStr.substring(0, iPos);
			}
			return Long.parseLong(inStr);
		}

		public void setValue(ResultSet rs) throws SQLException
		{
			if (rs != null)
			{

				String tmpStr = rs.getString("coverage");
				String[] tmpStrs;

				dCoverage = 0.0;
				dCoverageChange = 0.0;
				if (tmpStr != null)
				{
					tmpStrs = tmpStr.split(";");
					if (tmpStrs.length > 0)
						dCoverage = Double.parseDouble(tmpStrs[0]);
					if (tmpStrs.length > 1)
						dCoverageChange = Double.parseDouble(tmpStrs[1]);
				}

				tmpStr = rs.getString("critical_violations");
				tmpStrs = tmpStr.split(";");
				lcritical_violations = GetLong(tmpStrs[0]);
				lcritical_violationsChange = GetLong(tmpStrs[1]);

				tmpStr = rs.getString("major_violations");
				tmpStrs = tmpStr.split(";");
				lmajor_violations = GetLong(tmpStrs[0]);
				lmajor_violationsChange = GetLong(tmpStrs[1]);

				tmpStr = rs.getString("line_coverage");
				dline_coverage = 0.0;
				dline_coverageChange = 0.0;
				if (tmpStr != null)
				{
					tmpStrs = tmpStr.split(";");
					if (tmpStrs.length > 0)
						dline_coverage = Double.parseDouble(tmpStrs[0]);
					if (tmpStrs.length > 1)
						dline_coverageChange = Double.parseDouble(tmpStrs[1]);
				}

				tmpStr = rs.getString("branch_coverage");
				dbranch_coverage = 0.0;
				dbranch_coverageChange = 0.0;
				if (tmpStr != null)
				{
					tmpStrs = tmpStr.split(";");
					if (tmpStrs.length > 0)
						dbranch_coverage = Double.parseDouble(tmpStrs[0]);
					if (tmpStrs.length > 1)
						dbranch_coverageChange = Double.parseDouble(tmpStrs[1]);
				}

				tmpStr = rs.getString("tests");
				tmpStrs = tmpStr.split(";");
				ltests = GetLong(tmpStrs[0]);
				if (tmpStrs.length > 1)
				{
					ltestsChange = GetLong(tmpStrs[1]);
				} else
				{
					ltestsChange = 0;
				}
				tmpStr = rs.getString("test_success_density");
				dtest_success_density = 0.0;
				dtest_success_densityChange = 0.0;
				if (null != tmpStr)
				{
					tmpStrs = tmpStr.split(";");
					if (tmpStrs.length > 0)
						dtest_success_density = Double.parseDouble(tmpStrs[0]);
					if (tmpStrs.length > 1)
						dtest_success_densityChange = Double.parseDouble(tmpStrs[1]);
				}

				tmpStr = rs.getString("prj_lines");
				if (null != tmpStr)
				{
					tmpStrs = tmpStr.split(";");
					if (tmpStrs.length > 0)
					{
						lPrjLines = GetLong(tmpStrs[0]);
					}
					if (tmpStrs.length > 1)
					{
						lPrjLinesChange = GetLong(tmpStrs[1]);
					}
				}
				strName = rs.getString("name");
				if (strName.equalsIgnoreCase("dbe"))
				{
					lPrjLines = 20649;
				}
				if (0 != lPrjLines)
				{
					dUtcaseperline = (double) ltests * 2000 / lPrjLines;
					dUtcaseperlineChange = (double) (ltests + ltestsChange) * 2000 / (lPrjLines + lPrjLinesChange);
					dUtcaseperlineChange = dUtcaseperlineChange - dUtcaseperline;
				}

				if (strName != null)
				{
					if (strName.equalsIgnoreCase("prj_avg"))
					{
						strName = "所有产品平均值";
					} else
					{
						strName = "<a href='http://10.10.10.142:8080/sonar/dashboard/index/" + rs.getLong("id")
								+ "?did=4&period=1'>" + strName + "</a>";
					}
				}
			} else
			{
				strName = strCurrPrjName;
				lcritical_violations = -1;
				lmajor_violations = -1;
				dline_coverage = -1;
				dCoverage = -1;
				dbranch_coverage = -1;
				ltests = -1;
				dtest_success_density = -1;
			}
			FormatData();
		}

		public void FormatData()
		{
			dline_coverage = (double) Math.round(dline_coverage * 10) / 10;
			dline_coverageChange = (double) Math.round(dline_coverageChange * 10) / 10;

			dCoverage = (double) Math.round(dCoverage * 10) / 10;
			dCoverageChange = (double) Math.round(dCoverageChange * 10) / 10;

			dbranch_coverage = (double) Math.round(dbranch_coverage * 10) / 10;
			dbranch_coverageChange = (double) Math.round(dbranch_coverageChange * 10) / 10;

			dtest_success_density = (double) Math.round(dtest_success_density * 10) / 10;
			dtest_success_densityChange = (double) Math.round(dtest_success_densityChange * 10) / 10;

			dUtcaseperline = (double) Math.round(dUtcaseperline * 10) / 10;
			dUtcaseperlineChange = (double) Math.round(dUtcaseperlineChange * 10) / 10;
		}
	}

	Vector<ModuleInfo> vModuleInfos = new Vector<ModuleInfo>();

	private void AddModuleInfo(ResultSet rs, int iType, String strPrjName) throws SQLException
	{
		ModuleInfo cModuleInfo = new ModuleInfo();
		cModuleInfo.setValue(rs);
		cModuleInfo.iType = iType;
		cModuleInfo.strName = strPrjName;
		vModuleInfos.add(cModuleInfo);
	}

	private String GetChangeColor(Double d)
	{
		String strColor = "<font  size='2'>(" + String.valueOf(d) + "%)</font>";
		if (d > 0)
		{
			strColor = "<font color='#008000' size='2'>(+" + d + "%)</font>";
		} else if (d < 0)
		{
			strColor = "<font color='#FF0000'  size='2'>(" + d + "%)</font>";
		}
		return strColor;
	}

	private String GetChangeColor(long lValue)
	{
		String strColor = "<font  size='2'>(" + String.valueOf(lValue) + ")</font>";
		if (lValue > 0)
		{
			strColor = "<font color='#008000' size='2'>(+" + lValue + ")</font>";
		} else if (lValue < 0)
		{
			strColor = "<font  color='#FF0000' size='2'>(" + lValue + ")</font>";
		}
		return strColor;
	}

	/**
	 * 
	 * @param lValue
	 * @return 被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注： update
	 *         2014年1月22日
	 */
	private String GetUnChangeColor(long lValue)
	{
		String strColor = "<font  size='2'>(" + String.valueOf(lValue) + ")</font>";

		strColor = "<font s size='2'>(" + lValue + ")</font>";

		return strColor;
	}

	private String GetChangeUnColor(long lValue)
	{
		String strColor = "<font  size='2'>(" + String.valueOf(lValue) + ")</font>";
		if (lValue > 0)
		{
			strColor = "<font color='#FF0000' size='2'>(+" + lValue + ")</font>";
		} else if (lValue < 0)
		{
			strColor = "<font  color='#008000' size='2'>(" + lValue + ")</font>";
		}
		return strColor;
	}

	private void GetTdStr(StringBuffer sb, long a, long b)
	{
		if (a > b)
		{
			sb.append("<font color='#FF0000'>").append(a).append("</font>");
		} else
		{
			sb.append(a);
		}
	}

	private void GetTdStrReversed(StringBuffer sb, long a, long b)
	{
		if (a < b)
		{
			sb.append("<font color='#FF0000'>").append(a).append("</font>");
		} else
		{
			sb.append(a);
		}
	}

	private void GetTdStrReversed(StringBuffer sb, Double a, Double b)
	{
		if (a > b)
		{
			sb.append("<font color='#FF0000'>").append(a).append("%</font>");
		} else
		{
			sb.append(a).append("%");
		}

	}

	private void GetTdStr(StringBuffer sb, Double a, Double b)
	{
		if (a < b)
		{
			sb.append("<font color='#FF0000'>").append(a).append("%</font>");
		} else
		{
			sb.append(a).append("%");
		}

	}

	private void AddTdTitle(StringBuffer sb, String strColorString)
	{
		sb.append(
				"</FONT></TD><TD class=xl70 style='BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid; BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: ")
				.append(strColorString)
				.append("'><FONT face=微软雅黑 size='3'>");
	}

	private String GetModuleInfo(int iType)
	{
		StringBuffer sb = new StringBuffer();

		String strColorString = "";

		logger.debug("vModuleInfos=" + vModuleInfos.size());
		for (int iRowCount = 0; iRowCount < vModuleInfos.size(); iRowCount++)
		{
			ModuleInfo cModuleInfo = (ModuleInfo) vModuleInfos.get(iRowCount);
			logger.info("cModuleInfo.strName=" + cModuleInfo.strName);
			/*
			 * int j = cModuleInfo.strName.lastIndexOf(":"); if (j > 0) {
			 * cModuleInfo.strName = cModuleInfo.strName.substring(j,
			 * cModuleInfo.strName.length()); logger.info("cModuleInfo.strName="
			 * + cModuleInfo.strName); }
			 */
			if (iRowCount % 2 == 0)
			{
				strColorString = "transparent";
			} else
			{
				strColorString = " #fefefe";
			}
			sb.append(
					"<TR style='HEIGHT: 16.5pt' height=22><TD class=xl67 style='BORDER-TOP: windowtext; HEIGHT: 16.5pt; BORDER-RIGHT: windowtext 0.5pt solid; BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext 0.5pt solid; BACKGROUND-COLOR: ")
					.append(strColorString)
					.append("' height=22><FONT face=微软雅黑 size='3'>")
					.append(cModuleInfo.strName)
					.append("</FONT></TD><TD class=xl70 style='BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid; BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: ")
					.append(strColorString)
					.append("'><FONT face=微软雅黑 size='3'>");

			GetTdStr(sb, cModuleInfo.lcritical_violations, cAvgModuleInfo.lcritical_violations);
			sb.append(GetChangeUnColor(cModuleInfo.lcritical_violationsChange));
			AddTdTitle(sb, strColorString);

			GetTdStr(sb, cModuleInfo.lmajor_violations, cAvgModuleInfo.lmajor_violations);
			sb.append(GetChangeUnColor(cModuleInfo.lmajor_violationsChange));
			AddTdTitle(sb, strColorString);

			GetTdStr(sb, cModuleInfo.dbranch_coverage, cAvgModuleInfo.dbranch_coverage);
			sb.append(GetChangeColor(cModuleInfo.dbranch_coverageChange));
			AddTdTitle(sb, strColorString);

			GetTdStr(sb, cModuleInfo.dline_coverage, cAvgModuleInfo.dline_coverage);
			sb.append(GetChangeColor(cModuleInfo.dline_coverageChange));
			AddTdTitle(sb, strColorString);

			GetTdStr(sb, cModuleInfo.dCoverage, cAvgModuleInfo.dCoverage);
			sb.append(GetChangeColor(cModuleInfo.dCoverageChange));
			AddTdTitle(sb, strColorString);

			GetTdStrReversed(sb, cModuleInfo.ltests, cAvgModuleInfo.ltests);
			sb.append(GetChangeColor(cModuleInfo.ltestsChange));
			AddTdTitle(sb, strColorString);

			GetTdStr(sb, cModuleInfo.dtest_success_density, cAvgModuleInfo.dtest_success_density);
			sb.append(GetChangeColor(cModuleInfo.dtest_success_densityChange));
			AddTdTitle(sb, strColorString);

			GetTdStr(sb, cModuleInfo.lPrjLines, cAvgModuleInfo.lPrjLines);
			sb.append(GetUnChangeColor(cModuleInfo.lPrjLinesChange));
			AddTdTitle(sb, strColorString);

			GetTdStr(sb, cModuleInfo.dUtcaseperline, cAvgModuleInfo.dUtcaseperline);
			sb.append(GetChangeColor(cModuleInfo.dUtcaseperlineChange));

			sb.append("</FONT></TD></TR>");

		}
		return sb.toString();
	}

	private void MakePrjAvgInfo() throws Exception
	{
		// 扫描本地目录，从数据库中获取记录
		Connection conn = null;
		try
		{

			conn = AppConn.GetInstance().GetConn();

			String strSql = "select t1.id,t1.name,t2.id,DATE_FORMAT(t2.build_date,'%Y-%m-%d') build_date"
					// +
					// ",max(case t3.metric_id when 80 then CONCAT(t3.value,';',t3.variation_value_1)  end)blocker_violations"
					+ ",max(case t3.metric_id when 81 then CONCAT_WS(';',t3.value,t3.variation_value_1) end)critical_violations"
					+ ",max(case t3.metric_id when 82 then CONCAT_WS(';',t3.value,t3.variation_value_1)  end)major_violations"
					+ ",max(case t3.metric_id when 43 then CONCAT_WS(';',t3.value,t3.variation_value_1) end)line_coverage"
					+ ",max(case t3.metric_id when 37 then CONCAT_WS(';',t3.value,t3.variation_value_1) end)coverage"
					+ ",max(case t3.metric_id when 50 then CONCAT_WS(';',t3.value,t3.variation_value_1)  end)branch_coverage"
					+ ",max(case t3.metric_id when 30 then CONCAT_WS(';',t3.value,t3.variation_value_1)  end)tests"
					+ ",max(case t3.metric_id when 35 then CONCAT_WS(';',t3.value,t3.variation_value_1)  end)test_success_density"
					+ ",max(case t3.metric_id when 1 then CONCAT_WS(';',t3.value,t3.variation_value_1)  end)prj_lines"
					+ " from projects t1,snapshots t2,project_measures t3"
					+ " where t1.scope='PRJ' and t1.id =80664 and t1.enabled=1  and t1.id=t2.project_id 	 and t2.islast=1  and t2.id=t3.snapshot_id 	 and t3.metric_id in (1,37,43,30,81,82,35,50)"
					// + "  and DATE_FORMAT(t2.build_date,'%Y-%m-%d')='" +
					// strSelectDate + "'"
					+ "  group by t1.id  order by t1.name";

			logger.debug("strSql=" + strSql);

			PreparedStatement pStmt = conn.prepareStatement(strSql);
			ResultSet rs = pStmt.executeQuery();

			while (rs.next())
			{

				cAvgModuleInfo.setValue(rs);
			}

			// logger.debug("写入文件完成,共写入行数：" + iCount);
		} catch (Exception ex)
		{
			logger.error(ex);
			ex.printStackTrace();

		} finally
		{
			JpfDbUtils.DoClear(conn);
		}
	}

	ModuleInfo cAvgModuleInfo = new ModuleInfo();

	private void MakePrjMail(String strPrjName, long lPrj_Id) throws Exception
	{
		// 扫描本地目录，从数据库中获取记录
		Connection conn = null;
		try
		{
			strPrjName = strPrjName.trim();
			logger.info("get prj:cxx prj_name=" + strPrjName);
			conn = AppConn.GetInstance().GetConn();

			String strSql = "select t1.id,t1.name,t2.id,DATE_FORMAT(t2.build_date,'%Y-%m-%d') build_date"
					// +
					// ",max(case t3.metric_id when 80 then CONCAT(t3.value,';',t3.variation_value_1)  end)blocker_violations"
					+ ",max(case t3.metric_id when 81 then CONCAT_WS(';',t3.value,t3.variation_value_1)  end)critical_violations"
					+ ",max(case t3.metric_id when 82 then CONCAT_WS(';',t3.value,t3.variation_value_1)  end)major_violations"
					+ ",max(case t3.metric_id when 37 then CONCAT_WS(';',t3.value,t3.variation_value_1)  end)coverage"
					+ ",max(case t3.metric_id when 43 then CONCAT_WS(';',t3.value,t3.variation_value_1)  end)line_coverage"
					+ ",max(case t3.metric_id when 50 then CONCAT_WS(';',t3.value,t3.variation_value_1)   end)branch_coverage"
					+ ",max(case t3.metric_id when 30 then CONCAT_WS(';',t3.value,t3.variation_value_1)  end)tests"
					+ ",max(case t3.metric_id when 35 then CONCAT_WS(';',t3.value,t3.variation_value_1)  end)test_success_density"
					+ ",max(case t3.metric_id when 1 then CONCAT_WS(';',t3.value,t3.variation_value_1) end)prj_lines"
					+ " from projects t1,snapshots t2,project_measures t3"
					+ " where t1.scope='PRJ' and t1.id="
					+ lPrj_Id
					+ " and t1.enabled=1  and t1.id=t2.project_id 	 and t2.islast=1  and t2.id=t3.snapshot_id 	AND t3.rule_id IS NULL  and t3.metric_id in (1,37,30,43,81,82,35,50)"
					// + "  and DATE_FORMAT(t2.build_date,'%Y-%m-%d')='" +
					// strSelectDate + "'"
					+ "  group by t1.id  order by t1.name";

			logger.debug("strSql=" + strSql);

			PreparedStatement pStmt = conn.prepareStatement(strSql);
			ResultSet rs = pStmt.executeQuery();

			if (rs.next())
			{
				AddModuleInfo(rs, 0, strPrjName);
			}

			// logger.debug("写入文件完成,共写入行数：" + iCount);
		} catch (Exception ex)
		{
			logger.error(ex);
			ex.printStackTrace();

		} finally
		{
			JpfDbUtils.DoClear(conn);
		}
	}

	private void SetAverage()
	{
		// cAvgModuleInfo
		// vModuleInfos
		int iCount = vModuleInfos.size();
		long lValue = 0;
		for (int i = 0; i < iCount; i++)
		{
			lValue += ((ModuleInfo) vModuleInfos.get(i)).lcritical_violationsChange;
		}
		cAvgModuleInfo.lcritical_violationsChange = lValue / iCount;

		lValue = 0;
		for (int i = 0; i < iCount; i++)
		{
			lValue += ((ModuleInfo) vModuleInfos.get(i)).lmajor_violationsChange;
		}
		cAvgModuleInfo.lmajor_violationsChange = lValue / iCount;

		lValue = 0;
		for (int i = 0; i < iCount; i++)
		{
			lValue += ((ModuleInfo) vModuleInfos.get(i)).lPrjLinesChange;
		}
		cAvgModuleInfo.lPrjLinesChange = lValue / iCount;

		lValue = 0;
		for (int i = 0; i < iCount; i++)
		{
			lValue += ((ModuleInfo) vModuleInfos.get(i)).ltestsChange;
		}
		cAvgModuleInfo.ltestsChange = lValue / iCount;

		double dVouble = 0.0;
		for (int i = 0; i < iCount; i++)
		{
			dVouble += ((ModuleInfo) vModuleInfos.get(i)).dbranch_coverageChange;
		}
		cAvgModuleInfo.dbranch_coverageChange = dVouble / iCount;

		dVouble = 0.0;
		for (int i = 0; i < iCount; i++)
		{
			dVouble += ((ModuleInfo) vModuleInfos.get(i)).dCoverageChange;
		}
		cAvgModuleInfo.dCoverageChange = dVouble / iCount;

		dVouble = 0.0;
		for (int i = 0; i < iCount; i++)
		{
			dVouble += ((ModuleInfo) vModuleInfos.get(i)).dline_coverageChange;
		}
		cAvgModuleInfo.dline_coverageChange = dVouble / iCount;

		dVouble = 0.0;
		for (int i = 0; i < iCount; i++)
		{
			dVouble += ((ModuleInfo) vModuleInfos.get(i)).dtest_success_densityChange;
		}
		cAvgModuleInfo.dtest_success_densityChange = dVouble / iCount;

		dVouble = 0.0;
		for (int i = 0; i < iCount; i++)
		{
			dVouble += ((ModuleInfo) vModuleInfos.get(i)).dUtcaseperlineChange;
		}
		cAvgModuleInfo.dUtcaseperlineChange = dVouble / iCount;

		cAvgModuleInfo.FormatData();
	}

	private void MakeModuleMail() throws Exception
	{
		// 扫描本地目录，从数据库中获取记录
		Connection conn = null;
		String strMailCC = "";
		try
		{

			String strSql = "select t1.prj_mail,t1.leader_mail  from hz_prj t1 ";
			logger.debug("strSql=" + strSql);
			conn = AppConn.GetInstance().GetConn();
			PreparedStatement pStmt = conn.prepareStatement(strSql);
			ResultSet rs = pStmt.executeQuery();

			rs = pStmt.executeQuery(strSql);

			if (rs.next())
			{
				strMailCC += rs.getString("leader_mail").trim() + "," + rs.getString("prj_mail").trim() + ",";
			}

			StringBuffer sb = new StringBuffer();
			sb.setLength(0);
			String newMailString = strDevHtmlString;
			// for prj
			vModuleInfos.clear();

			strSql = "select *  from hz_prj  order by prj_name";
			logger.info("strSql=" + strSql);
			pStmt = conn.prepareStatement(strSql);
			rs = pStmt.executeQuery();
			while (rs.next())
			{
				MakePrjMail(rs.getString("prj_name").trim(), rs.getLong("prj_id"));
			}
			SetAverage();
			// vModuleInfos.add(cAvgModuleInfo);

			vModuleInfos.add(0, cAvgModuleInfo);

			newMailString = newMailString.replaceFirst("#wupf6", GetModuleInfo(1));
			newMailString = newMailString.replaceFirst("#wupf5", strSelectDate);

			logger.info("strMailCC=" + strMailCC);

			// SonarMail.sendMail("", newMailString, "GBK",
			// " SONAR产品质量日报(自动发出)");

			SonarMail
					.sendMail(
							"BSS-Billing-PRD-MNG,bss-billing-cmc-managers,chenyf,fanxj,wangjing,kongll,chencm,huangyd,lizl2,wupf,zhuhh5",
							newMailString, "GBK", " SONAR产品质量日报(自动发出)");

			// logger.debug("写入文件完成,共写入行数：" + iCount);
		} catch (Exception ex)
		{
			logger.error(ex);
			ex.printStackTrace();
		} finally
		{
			JpfDbUtils.DoClear(conn);
		}
	}

	/**
	 * @param args
	 *            被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注：
	 *            update 2013-4-16
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

		if (1 == args.length)
		{
			SendPrjMail cSendRrjMail = new SendPrjMail();
		} else
		{
			SendPrjMail cSendDevMail = new SendPrjMail();
		}

	}

}
