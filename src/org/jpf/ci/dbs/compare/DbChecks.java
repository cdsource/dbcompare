/** 
* @author 吴平福 
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年5月6日 下午2:29:52 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.mails.JpfMail;
import org.jpf.utils.JpfDateTimeUtil;
import org.jpf.utils.JpfDbUtils;
import org.jpf.utils.JpfFileUtil;
import org.jpf.xmls.JpfXmlUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 */
public class DbChecks
{
	private static final Logger logger = LogManager.getLogger();


	private void doCheck(DbDescInfo cDbDescInfo,String strDomain,StringBuffer sbTotal,StringBuffer sbDetail)
	{
		Connection conn=null;
		try
		{
			long lProduce=0;
			long lTriger=0;
			long lView=0;
			long lAutoIncrement=0;
			conn=cDbDescInfo.getConn();
			String strSql="select trigger_schema,trigger_name from INFORMATION_SCHEMA.TRIGGERS  Where trigger_schema=?";
			PreparedStatement  pstmt=conn.prepareStatement(strSql);
			pstmt.setString(1, strDomain);
			logger.info(strSql);
			ResultSet rSet=pstmt.executeQuery();
			while (rSet.next())
			{
				lTriger++;
				sbDetail.append("<tr><td>").append(strDomain).append("</td>")
					.append("<td>触发器</td>")
					.append("<td>").append(rSet.getString("trigger_name")).append("</td>")
					.append("<td>drop trigger ").append(rSet.getString("trigger_name")).append(";</td></tr>");
			}

			
			strSql="select routine_schema,routine_name from INFORMATION_SCHEMA.Routines  Where routine_schema=?";
			 pstmt=conn.prepareStatement(strSql);
			pstmt.setString(1, strDomain);
			logger.info(strSql);
			rSet=pstmt.executeQuery();
			while (rSet.next())
			{
				lProduce++;
				sbDetail.append("<tr><td>").append(strDomain).append("</td>")
					.append("<td>存贮过程</td>")
					.append("<td>").append(rSet.getString("routine_name")).append("</td>")
					.append("<td>drop procedure ").append(rSet.getString("routine_name")).append(";</td></tr>");
			}
			
			strSql="select table_schema,table_name from INFORMATION_SCHEMA.tables  Where table_type='view' and table_schema=?";
			 pstmt=conn.prepareStatement(strSql);
			pstmt.setString(1, strDomain);
			logger.info(strSql);
			rSet=pstmt.executeQuery();
			while (rSet.next())
			{
				lView++;
				sbDetail.append("<tr><td>").append(strDomain).append("</td>")
					.append("<td>视图</td>")
					.append("<td>").append(rSet.getString("table_name")).append("</td>")
					.append("<td>drop view ").append(rSet.getString("table_name")).append(";</td></tr>");
			}
			
			strSql="select table_schema,table_name,column_name from INFORMATION_SCHEMA.COLUMNS  Where extra='auto_increment' and table_schema=?";
			 pstmt=conn.prepareStatement(strSql);
			pstmt.setString(1, strDomain);
			logger.info(strSql);
			rSet=pstmt.executeQuery();
			while (rSet.next())
			{
				lAutoIncrement++;
				sbDetail.append("<tr><td>").append(strDomain).append("</td>")
					.append("<td>自增长字段</td>")
					.append("<td>").append(rSet.getString("table_name")).append(".").append(rSet.getString("column_name")).append("</td>")
					.append("<td></td></tr>");
			}
			sbTotal.append("<tr><td>").append(strDomain).append("</td><td>").append(lTriger).append("</td>")
			.append("<td>").append(lProduce).append("</td>")
			.append("<td>").append(lView).append("</td>")
			.append("<td>").append(lAutoIncrement).append("</td></tr>");
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}finally
		{
			JpfDbUtils.doClear(conn);
		}
	}
	/**
	 * 
	 */
	public DbChecks(String strConfigFileName)
	{
		// TODO Auto-generated constructor stub
		try
		{
			StringBuffer sbTotal=new StringBuffer();
			StringBuffer sbDetail=new StringBuffer();
			
			JpfFileUtil.checkFile(strConfigFileName);
<<<<<<< HEAD
			NodeList nl = JpfXmlUtil.getNodeList("dbsource", strConfigFileName);
=======
			NodeList nl = JpfXmlUtil.GetNodeList("dbsource", strConfigFileName);
>>>>>>> origin/master
			String strDefaultMail=""; 


			if(1==nl.getLength())
			{
				Element el = (Element) nl.item(0);
				strDefaultMail= JpfXmlUtil.getParStrValue(el, "dbmails");
			}else {
				logger.error("error source db info");
			}
			nl = JpfXmlUtil.getNodeList("dbcompare", strConfigFileName);
			logger.debug(nl.getLength());
			String strJdbcUrl="";
			for (int j = 0; j < nl.getLength(); j++)
			{
				// System.out.println(nl.item(j).getNodeValue());
				Element el = (Element) nl.item(j);
				strJdbcUrl = JpfXmlUtil.getParStrValue(el, "dburl");
				String strDbUsr = JpfXmlUtil.getParStrValue(el, "dbusr");
				String strDbPwd = JpfXmlUtil.getParStrValue(el, "dbpwd");
				String strDomain = JpfXmlUtil.getParStrValue(el, "dbdomain");
				strDefaultMail = JpfXmlUtil.getParStrValue(el, "dbmails")+","+strDefaultMail;
				logger.info(strDefaultMail);
				DbDescInfo cDbDescInfo = new DbDescInfo(strJdbcUrl, strDbUsr, strDbPwd);
				doCheck(cDbDescInfo,strDomain,sbTotal,sbDetail);
			}	
			sendMail(sbTotal,sbDetail,strDefaultMail,strJdbcUrl);
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
	}
	public  void sendMail(StringBuffer sbTotal, StringBuffer sbDetail,String strMailers, String strDbInfo) throws Exception
	{
		logger.debug("write Result File...");

		String strMailText = JpfFileUtil.getFileTxt("compare_check.html");
<<<<<<< HEAD
		strMailText = strMailText.replaceAll("#wupf1", JpfDateTimeUtil.getCurrDate());
=======
		strMailText = strMailText.replaceAll("#wupf1", JpfDateTimeUtil.GetCurrDate());
>>>>>>> origin/master
		strMailText = strMailText.replaceAll("#wupf3", strDbInfo);
		strMailText = strMailText.replaceAll("#wupf4", sbDetail.toString());
		strMailText = strMailText.replaceAll("#diffs", sbTotal.toString());
		// strMailText+=sbAlterSql.toString();
		JpfMail.sendMail(strMailers, strMailText, "GBK", "数据库非法的内容： 比对库" + strDbInfo);
	}
	/**
	 * @param args
	 * 被测试类名：TODO
	 * 被测试接口名:TODO
	 * 测试场景：TODO
	 * 前置参数：TODO
	 * 入参：
	 * 校验值：
	 * 测试备注：
	 * update 2015年5月6日
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		DbChecks cDbChecks=new DbChecks(args[0]);
	}

}
