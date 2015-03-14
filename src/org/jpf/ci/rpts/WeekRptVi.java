/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2014��8��13�� ����11:02:58 
* ��˵�� 
*/ 

package org.jpf.ci.rpts;

import java.sql.ResultSet;

/**
 * 
 */
public class WeekRptVi extends AbstractRpt
{

	/**
	 * @param strPrjName
	 * @param iType
	 * @param strMailCC
	 */
	public WeekRptVi(String strPrjName, int iType, String strMailCC)
	{
		super(strPrjName, iType, strMailCC);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.jpf.ci.rpts.AbstractWeekRpt#GetTemplateName()
	 */
	@Override
	public String GetTemplateName()
	{
		// TODO Auto-generated method stub
		return "week_vi.html";
	}

	/**
	 * @param args
	 * ������������TODO
	 * �����Խӿ���:TODO
	 * ���Գ�����TODO
	 * ǰ�ò�����TODO
	 * ��Σ�
	 * У��ֵ��
	 * ���Ա�ע��
	 * update 2014��8��13��
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		if (3 == args.length)
		{
			int i = Integer.parseInt(args[0]);
			WeekRptVi cRpt = new WeekRptVi(args[1],i,args[2]);
		}
	}

	/* (non-Javadoc)
	 * @see org.jpf.ci.rpts.AbstractWeekRpt#GetSql(int, java.lang.String)
	 * ����Υ�����SQL update 2014��7��24��
	 */
	@Override
	public String GetSql(int iType, String strPrjName)
	{
		// TODO Auto-generated method stub
		String strSql = "select * from hz_sonar_user where prj_name='" + strPrjName + "'";
		if (0 == iType)
		{
			strSql += " and (IFNULL(major_violations1,0)<>IFNULL(major_violations2,0) or IFNULL(critical_violations1,0)<>IFNULL(critical_violations2,0)) ";
		}
		strSql += " order by login,kee";
		return strSql;
	}

	/* (non-Javadoc)
	 * @see org.jpf.ci.rpts.AbstractWeekRpt#DoMailText(java.lang.StringBuffer, java.sql.ResultSet)
	 */
	@Override
	public void DoMailText(StringBuffer sb, ResultSet rs) throws Exception
	{
		// TODO Auto-generated method stub
		while (rs.next())
		{
			sb.append("<tr><td align='left'>").append(rs.getString("kee"))
			.append("</td><td>").append(rs.getString("login"))
			.append("</td><td>").append(rs.getLong("critical_violations1"))
			.append("</td><td>").append(rs.getLong("major_violations1"))
			.append("</td><td>").append(rs.getLong("critical_violations2"))
			.append("</td><td>").append(rs.getLong("major_violations2"))
			.append("</td><td>").append(rs.getLong("major_violations1")-rs.getLong("major_violations2")+rs.getLong("critical_violations1")-rs.getLong("critical_violations2"))
			.append("</td></tr>");
		}
	}

	/* (non-Javadoc)
	 * @see org.jpf.ci.rpts.AbstractRpt#GetMailTitle()
	 */
	@Override
	public String GetMailTitle()
	{
		// TODO Auto-generated method stub
		return " һ�ܴ���Υ��仯���";
	}

}
