/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2014年4月16日 下午4:27:18 
 * 类说明 
 */

package org.jpf.ci.rpts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



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
public class DailyPackageRpt
{
	private static final Logger logger =    LogManager.getLogger(DailyPackageRpt.class);
	/**
	 * 
	 */
	private long pjr_id = 162751;
	private String strPrjName = "Payment";
	private String strMailCC = "bss-billing-a&r";

	private Double FormatDouble(Double d)
	{
		return (double) Math.round(d * 10) / 10;
	}

	private String GetNotSameStr(String _strOld, int _iLength)
	{
		if (_strOld == null)
		{
			return _strOld;
		}

		_strOld = _strOld.trim();
		_strOld=_strOld.substring(_iLength,_strOld.length());
		for (int j = 0; j <_iLength; j++)
		{
			_strOld = "&nbsp;" + _strOld;
		}
		System.out.println(_strOld);
		return _strOld;
	}

	public DailyPackageRpt(int iType)
	{
		DoDailyRpt(iType);
	}

	private String GetPackageName(String _strFullFileName)
	{
		if (_strFullFileName != null)
		{
			int i = _strFullFileName.lastIndexOf(".");
			if (i > 0)
			{
				_strFullFileName = _strFullFileName.substring(0, i);
			}
			return _strFullFileName;

		} else
		{
			return "";
		}

	}

	private void DoDailyRpt(int iType)
	{
		// TODO Auto-generated constructor stub
		// 扫描本地目录，从数据库中获取记录
		Connection conn = null;
		logger.info("iType=" + iType);

		String strSelectDate = JpfDateTimeUtil.GetDay("yyyy-MM-dd", 0);

		try
		{
			String strDevHtmlString = JpfFileUtil.GetFileTxt("file_coverage.html");

			String strSql = "select * from (SELECT t1.long_name"
					+ " ,MAX(CASE t3.metric_id WHEN 50 THEN t3.value  END)branch_coverage"
					+ " ,MAX(CASE t3.metric_id WHEN 37 THEN t3.value  END)coverage"
					+ " ,MAX(CASE t3.metric_id WHEN 43 THEN t3.value  END)line_coverage"
					+ " ,MAX(CASE t3.metric_id WHEN 46 THEN t3.value  END)conver_branch"
					+ " ,MAX(CASE t3.metric_id WHEN 48 THEN t3.value  END)un_conver_branch"
					+ " FROM projects t1,snapshots t2,project_measures t3"
					+ " WHERE t1.scope='FIL' AND t1.`qualifier`='CLA' AND t1.root_id="
					+ pjr_id
					+ "  AND t1.enabled=1  AND t1.id=t2.project_id"
					+ " AND t2.islast=1  AND t2.id=t3.snapshot_id"
					+ " AND t3.metric_id IN (37,43,46,48,50) GROUP BY t1.id)t1  ORDER BY t1.long_name";
			logger.info("strSql=" + strSql);
			conn = AppConn.GetInstance().GetConn();
			PreparedStatement pStmt = conn.prepareStatement(strSql);
			ResultSet rs = pStmt.executeQuery();

			rs = pStmt.executeQuery(strSql);
			StringBuffer sb = new StringBuffer();
			sb.setLength(0);
			int iFileCount = 0;
			String strFileName = "";
			String strPackage = "";
			while (rs.next())
			{

				strFileName=rs.getString(1).trim();
				if (strPackage.length() == 0)
				{
					strPackage=GetPackageName(strFileName);
					sb.append("<tr style='font-family: 微软雅黑;background:#414E93")
							.append("text-align:left; color:#414E93; padding:1.5pt 0;border:thin #BED6F6 0.5pt;'><td align='left'>")
							.append(strPackage).append("</td><td>")
							.append("</td><td></td><td></td></tr>");
				}else {
					if (!strPackage.equalsIgnoreCase(GetPackageName(strFileName)))
					{
						strPackage=GetPackageName(strFileName);
						sb.append("<tr style='font-family: 微软雅黑;background:#414E93")
								.append("text-align:left; color:#414E93; padding:1.5pt 0;border:thin #BED6F6 0.5pt;'><td  align='left'>")
								.append(strPackage).append("</td><td>")
								.append("</td><td></td><td></td></tr>");
					}
				}
				
				String strShowStr = GetNotSameStr(strFileName, strPackage.length());

				sb.append("<tr style='font-family: 微软雅黑;background:#FFFFFF")
						.append("text-align:left; color:#414E93; padding:1.5pt 0;border:thin #BED6F6 0.5pt;'><td style='background-color: #FFFFFF'>")
						.append(strShowStr).append("</td><td style='background-color: #FFFFFF'>").append(FormatDouble(rs.getDouble(2)))
						.append("%</td><td style='background-color: #FFFFFF'>").append(FormatDouble(rs.getDouble(3)))
						.append("%</td><td style='background-color: #FFFFFF'>").append(FormatDouble(rs.getDouble(4))).append("%</td><td style='background-color: #FFFFFF'>")
						.append(rs.getInt("conver_branch")).append("</td><td style='background-color: #FFFFFF'>")
						.append(rs.getInt("conver_branch")-rs.getInt("un_conver_branch")).append("</td></tr>");
				iFileCount++;
			}
			strDevHtmlString = strDevHtmlString.replaceAll("#wupf2", strSelectDate);
			strDevHtmlString = strDevHtmlString.replaceAll("#wupf3", sb.toString());
			strDevHtmlString = strDevHtmlString.replaceAll("#wupf4", String.valueOf(iFileCount));

			logger.info("strMailCC=" + strMailCC);
			if (0 == iType)
			{
				SonarMail.sendMail("", strDevHtmlString, "GBK", strPrjName + " --SONAR单元测试覆盖率日报(自动发出)");
			}
			if (1 == iType)
			{
				SonarMail.sendMail(strMailCC, strDevHtmlString, "GBK", strPrjName + " --SONAR单元测试覆盖率日报(自动发出)");
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

	/**
	 * @param args
	 *            被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注：
	 *            update 2014年4月16日
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

		if (1 == args.length)
		{
			int i = Integer.parseInt(args[0]);
			DailyPackageRpt cDailyPackageRpt = new DailyPackageRpt(i);
		} else
		{
			DailyPackageRpt cDailyPackageRpt = new DailyPackageRpt(0);
		}

	}

}
