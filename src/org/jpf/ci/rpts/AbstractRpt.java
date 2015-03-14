/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2014年8月13日 下午10:58:35 
 * 类说明 
 */

package org.jpf.ci.rpts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
public abstract class AbstractRpt
{
	private static final Logger logger = LogManager.getLogger();

	// 邮件模板名称
	public abstract String GetTemplateName();

	// 邮件标题
	public abstract String GetMailTitle();

	/**
	 * 
	 * @return 返回覆盖率相关SQL update 2014年7月24日
	 */
	public abstract String GetSql(int iType, String strPrjName);

	/**
	 * 
	 */

	public abstract void DoMailText(StringBuffer sb, ResultSet rs) throws Exception;

	protected Double FormatDouble(Double d)
	{
		return (double) Math.round(d * 10) / 10;
	}
	protected int FormatInt(Double d)
	{
		return (int) Math.ceil(d ) ;
	}
	public AbstractRpt(String strPrjName, int iType, String strMailCC)
	{
		DoWeekRpt(strPrjName, iType, strMailCC);
	}

	String strEndDate = JpfDateTimeUtil.GetDay("yyyy-MM-dd", -1);
	String strBeginDate = JpfDateTimeUtil.GetDay("yyyy-MM-dd", -7);

	private void GetAvailableDate(String strPrjName)
	{

		Connection conn = null;
		try
		{
			String strSql =
					"select distinct build_date from hz_sonar_user2 where prj_name=?  and build_date<=? order by build_date desc;";
			logger.info("strSql={}", strSql);
			conn = AppConn.GetInstance().GetConn();
			PreparedStatement pStmt = conn.prepareStatement(strSql);
			pStmt.setString(1, strPrjName);
			pStmt.setString(2, strEndDate);
			logger.info("strPrjName={}", strPrjName);
			logger.info("strEndDate={}", strEndDate);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next())
			{
				strEndDate = rs.getString(1);
			}

			pStmt.setString(1, strPrjName);
			pStmt.setString(2, strBeginDate);
			rs = pStmt.executeQuery();
			if (rs.next())
			{
				strBeginDate = rs.getString(1);
			}
			logger.info("strBeginDate={}", strBeginDate);
			logger.info("strEndDate={}", strEndDate);
		} catch (Exception ex)
		{
			logger.error(ex);
			ex.printStackTrace();
		} finally
		{
			JpfDbUtils.DoClear(conn);
		}

	}

	private void DoWeekRpt(String strPrjName, int iType, String strMailCC)
	{
		// TODO Auto-generated constructor stub
		// 扫描本地目录，从数据库中获取记录
		Connection conn = null;

		try
		{

			GetAvailableDate(strPrjName);

			conn = AppConn.GetInstance().GetConn();

			Statement pStmt = conn.createStatement();
			String strSql = "delete from hz_sonar_user where prj_name='" + strPrjName + "'";
			logger.info("strSql={}", strSql);
			pStmt.executeUpdate(strSql);

			strSql = "insert into hz_sonar_user select prj_name,login,'" + strEndDate + "','" + strBeginDate
					+ "',kee,max(case t1.build_date when '" + strEndDate
					+ "' then t1.brach_coverage  end)brach_coverage1,max(case t1.build_date when '" + strBeginDate
					+ "' then t1.brach_coverage  end)brach_coverage2,"
					+ "max(case t1.build_date when '" + strEndDate
					+ "' then t1.major_violations  end)major_violations1,"
					+ " max(case t1.build_date when '" + strBeginDate
					+ "' then t1.major_violations  end)major_violations2,"
					+ " max(case t1.build_date when '" + strEndDate
					+ "' then t1.critical_violations  end)critical_violations1,"
					+ " max(case t1.build_date when '" + strBeginDate
					+ "' then t1.critical_violations  end)critical_violations2,"
					+ " max(case t1.build_date when '" + strEndDate + "' then t1.cover_branch  end)cover_branch1,"
					+ " max(case t1.build_date when '" + strBeginDate + "' then t1.cover_branch  end)cover_branch2, "
					+ " max(case t1.build_date when '" + strEndDate + "' then t1.un_cover_branch  end)un_cover_branch1,"
					+ " max(case t1.build_date when '" + strBeginDate + "' then t1.un_cover_branch  end)un_cover_branch2 "
					+ " from hz_sonar_user2 t1 where prj_name='" + strPrjName + "' and login is not null "
					+ " group by kee order by kee;";
			logger.info("strSql={}", strSql);
			pStmt.executeUpdate(strSql);
			// conn.commit();
			MakeWeekRpt(strPrjName, iType, strMailCC);

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

	private void MakeWeekRpt(String strPrjName, int iType, String strMailCC)
	{
		// TODO Auto-generated constructor stub
		// 扫描本地目录，从数据库中获取记录
		Connection conn = null;

		try
		{
			logger.info("mail template name:{}", GetTemplateName());
			String strDevHtmlString = JpfFileUtil.GetFileTxt(GetTemplateName());

			conn = AppConn.GetInstance().GetConn();

			Statement pStmt = conn.createStatement();
			String strSql = GetSql(iType, strPrjName);

			logger.info("strSql={}", strSql);
			ResultSet rs = pStmt.executeQuery(strSql);
			StringBuffer sb = new StringBuffer();
			sb.setLength(0);
			DoMailText(sb, rs);

			strDevHtmlString = strDevHtmlString.replaceAll("#wupf3", sb.toString());

			logger.info("strMailCC=" + strMailCC);

			SonarMail.sendMail(strMailCC, strDevHtmlString, "GBK", strPrjName + " --" + GetMailTitle() + "(自动发出)");

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

}
