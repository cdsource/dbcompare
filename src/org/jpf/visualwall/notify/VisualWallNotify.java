/** 
 * @author ��ƽ�� 
 * E-mail:wupf@asiainfo.com 
 * @version ����ʱ�䣺2015��3��7�� ����3:15:27 
 * ��˵�� 
 */

package org.jpf.visualwall.notify;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.mails.JpfMail;
import org.jpf.utils.JpfDbUtils;
import org.jpf.utils.JpfFileUtil;
import org.jpf.visualwall.WallsDbConn;

/**
 * 
 */
public class VisualWallNotify
{
	private static final Logger logger = LogManager.getLogger();

	/**
	 * 
	 */
	public VisualWallNotify(String strModule, String strKpiType)
	{
		// TODO Auto-generated constructor stub
		Connection conn = null;
		try
		{
			String strSql = "select prj_id,prj_name,leader_mail,areaname,prj_cname from bss_prj where areaname='"
					+ strModule
					+ "'";
			logger.debug(strSql);
			conn = WallsDbConn.GetInstance().GetConn();
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(strSql);
			while (rs1.next())
			{
				String strLeaderMail = rs1.getString("leader_mail") + "," + rs1.getString("prj_cname");
				String strPrjName = rs1.getString("prj_name");
				strSql = " select * from (SELECT  distinct t1.build_date, t1.prj_id, t1.is_last, t1.prj_name, "
						+ GetKeyString(strKpiType)
						+ " as kpi FROM bss_rpt t1, bss_rpt_date t2 WHERE t1.build_date=t2.build_date  and t1.areaname='"
						+ strModule
						+ "' and t2.areaname=t1.areaname and t1.build_date<=now() AND t1. prj_id =t2. prj_id AND t1.prj_id="
						+ rs1.getString("prj_id") + "  ORDER BY build_date desc  limit 1 ) t3 order by build_date  ";
				Statement stmt2 = conn.createStatement();
				ResultSet rs2 = stmt2.executeQuery(strSql);
				logger.debug(strSql);
				boolean bSendMail = true;
				String sMailText = JpfFileUtil.GetFileTxt("notify.html");
				sMailText = sMailText.replaceAll("#wupf1", strPrjName);
				StringBuffer sbMail = new StringBuffer();
				if (rs2.next() && rs2.getDouble("kpi") > -20)
				{
					bSendMail = false;

					// sMailText += rs2.getString("build_date") + ":" +
					// rs2.getDouble("kpi") + "%<br>";
					sbMail.append(
							"<tr style=\"HEIGHT: 16.5pt\" height=22><td class=xl66 style=\"border:0.5pt solid windowtext; HEIGHT: 16.5pt; WIDTH: 188px;\"  height=22 align=\"center\">")
							.append(rs2.getString("build_date"))
							.append("</td><td class=xl66 style=\"border:0.5pt solid windowtext; HEIGHT: 16.5pt; WIDTH: 188px;\"  height=22 align=\"center\">")
							.append(rs2.getDouble("kpi")).append("%</td></tr>");
				}
				if (bSendMail)
				{
					// JpfMail.SendMail(strLeaderMail, sMailText, "UTF-8",
					// strPrjName+"���Ŀ��澯");
					sMailText = sMailText.replaceAll("#wupf2", sbMail.toString());
					JpfMail.SendMail(strLeaderMail, sMailText, "UTF-8", strPrjName + "��������Ľ�Ŀ��澯(�Զ�����)");
				}
			}
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		} finally
		{
			JpfDbUtils.DoClear(conn);
		}
	}

	private String GetKeyString(String strKpiType)
	{
		// TODO Auto-generated method stub
		// ��Ԫ����
		if (strKpiType.equalsIgnoreCase("1"))
		{
			return " ((t1.branch_coverage*100/t2.refer_value)-1) *100";
		}
		// Υ�����
		if (strKpiType.equalsIgnoreCase("2"))
		{
			// ƽ��ֵ
			return "(t2.refer_value/(t1.blocker_violations+t1.critical_violations+t1.major_violations)-1) *100  ";
		}
		// Υ���ܶ�
		if (strKpiType.equalsIgnoreCase("3"))
		{
			// ƽ��ֵ
			return "100*(1-(t1.major_violations+critical_violations+blocker_Violations)/t2.refer_value)  ";
		}
		return "";
	}

	/**
	 * @param args
	 *            ������������TODO �����Խӿ���:TODO ���Գ�����TODO ǰ�ò�����TODO ��Σ� У��ֵ�� ���Ա�ע��
	 *            update 2015��3��7��
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		if (2 == args.length)
		{
			VisualWallNotify cVisualWallNotify = new VisualWallNotify(args[0], args[1]);
		}
		System.out.println("game over");
	}

}
