/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2013-5-4 上午9:59:44 
 * 类说明 
 */

package org.jpf.ci;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class SonarUsr
{
	private static final Logger logger = LogManager.getLogger(SonarUsr.class);

	/**
	 * @param args
	 *            被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注：
	 *            update 2013-5-4
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		if (3 == args.length)
		{
			SonarUsr cSonarUsr = new SonarUsr(args[0], args[1], args[2]);
		}
	}

	public SonarUsr(String strPrjName, String strPath, String strType)
	{
		// 扫描本地目录，从数据库中获取记录
		Connection conn = null;
		if (strPrjName == null || "".equalsIgnoreCase(strPrjName))
		{
			logger.info("error prj name :" + strPrjName);
			return;
		}
		strPrjName=strPrjName.trim();
		if (strPath == null || "".equalsIgnoreCase(strPath))
		{
			logger.info("error path name :" + strPath);
			return;
		}
		File file = new File(strPath);
		if (!file.exists() || !file.isDirectory())
		{
			logger.info("path is not exist :" + strPath);
			return;
		}
		try
		{
			String strSql = "select * "
					+ " from projects t1"
					+ " where t1.scope='PRJ' and t1.enabled=1 and t1.qualifier='TRK' and t1.name='" + strPrjName
					+ "'";

			logger.debug("strSql=" + strSql);
			conn = AppConn.GetInstance().GetConn();
			conn.setAutoCommit(false);
			PreparedStatement pStmt = conn.prepareStatement(strSql);
			ResultSet rs = pStmt.executeQuery();
			int iCount = 0;
			if (rs.next())
			{

				logger.info("current prj :" + rs.getString("name"));
				logger.info("current prj id :" + rs.getInt("id"));
				logger.info(rs.getString("language"));
			
				strSql = "delete from hz_sonar_user2 where build_date<DATE_SUB(CURDATE(), INTERVAL 30 DAY) and  prj_name='" + strPrjName+"'";
				logger.debug("strSql=" + strSql);
				PreparedStatement pStmt3 = conn.prepareStatement(strSql);
				int i = pStmt3.executeUpdate();
				logger.info(" delete count=" + i);
				pStmt3.close();
				conn.commit();

				SvnUsr cSvnUsr = new SvnUsr(rs.getString("name"), strPath, strType);
				String strCond = "";
				if (strType.equalsIgnoreCase("0"))
				{
					strCond = "t3.root_id=" + rs.getString("id");
				} else
				{
					strCond = "t3.root_id in (select id from projects where root_id=" + rs.getString("id") + ")";
				}
				strSql = "insert into hz_sonar_user2 select '"
						+ rs.getString("name")
						+ "',null,DATE_FORMAT(t2.build_date,'%Y-%m-%d') build_date,t2.project_id,t3.kee,"
						+ "t1.* from 	(select snapshot_id,line_coverage,tests,brach_coverage,blocker_violations"
						+ ",critical_violations,major_violations,cover_branch,un_cover_branch from"
						+ " (select snapshot_id,rule_id,max(case t1.metric_id when 43 then t1.value  end)line_coverage"
						+ ",max(case t1.metric_id when 50 then t1.value  end)brach_coverage"
						+ ",max(case t1.metric_id when 30 then t1.value  end)tests"
						+ ",max(case t1.metric_id when 80 then t1.value  end)blocker_violations"
						+ ",max(case t1.metric_id when 81 then t1.value  end)critical_violations"
						+ ",max(case t1.metric_id when 82 then t1.value  end)major_violations"
						+ ",max(case t1.metric_id when 46 then t1.value  end)cover_branch"
						+ ",max(case t1.metric_id when 48 then t1.value  end)un_cover_branch"
						+ " from project_measures t1 where 	metric_id in (43,50,30,80,81,82,46,48) and rule_id is null"
						+ " group by rule_id,snapshot_id)t1)t1,snapshots t2,projects t3"
						+ " where t1.snapshot_id=t2.id 	and t2.islast=1 	and t2.project_id=t3.id and " + strCond
						+ " and t3.scope='FIL' and t3.enabled=1 and t3.kee not like '%Test%' ";

				logger.info("strSql=" + strSql);
				PreparedStatement pStmt2 = conn.prepareStatement(strSql);
				i = pStmt2.executeUpdate();
				logger.info(" insert hz_sonar_user2 count=" + i);
				
				//
				strSql="update hz_sonar_user2 t1 set kee=SUBSTRING(kee,  INSTR(kee, ':') + 1) where prj_name='"+strPrjName+"'";
				logger.info("strSql=" + strSql);
				i = pStmt2.executeUpdate(strSql);
				logger.info(" update hz_sonar_user2 count=" + i);		
				
				strSql="update hz_sonar_user2 t1 set kee=SUBSTRING(kee,  INSTR(kee, ':') + 1) where prj_name='"+strPrjName+"'";
				logger.info("strSql=" + strSql);
				i = pStmt2.executeUpdate(strSql);
				logger.info(" update hz_sonar_user2 count=" + i);	
				
				strSql="delete from hz_dev where prj_name='"+strPrjName
						+"' and file_path  in ( select * from (select max(file_path) from hz_dev where prj_name='"
						+strPrjName+"' group by file_path having count(file_path)>1) b)";
				logger.info("strSql=" + strSql);
				i = pStmt2.executeUpdate(strSql);
				logger.info(" delete from hz_dev count=" + i);	
				
				strSql="update hz_sonar_user2 t1 set t1.login = (select t2.login from hz_dev t2 where t1.kee=t2.file_path and t2.prj_name='"+strPrjName+"'"
						+") where t1.prj_name='"+strPrjName+"'";
				logger.info("strSql=" + strSql);
				i = pStmt2.executeUpdate(strSql);
				logger.info(" update hz_sonar_user2 count=" + i);	
				
				/*
				strSql="delete from hz_usr_exist where prj_name='"+rs.getString("name")+"'";
				logger.info("strSql=" + strSql);
				i = pStmt2.executeUpdate(strSql);
				logger.info(" delete hz_usr_exist count=" + i);
				
				strSql="insert into hz_usr_exist select t4.file_path,'"+rs.getString("name")+"',t4.login,t3.kee from hz_sonar_user2 t3,hz_dev t4"
						+" where trim(t3.kee) =trim(t4.file_path) and t4.prj_name='"+rs.getString("name")+"' and t3.prj_name=t4.prj_name ";
				logger.info("strSql=" + strSql);
				i = pStmt2.executeUpdate(strSql);
				logger.info(" insert hz_usr_exist count=" + i);
				
				strSql="update hz_sonar_user2 t1 set t1.login = (select t2.login from hz_dev t2 where t1.kee  like concat('%',t2.file_path) and t2.prj_name='"
						+rs.getString("name")+"' 	and t2.file_path not in 	(select file_path from hz_usr_exist where prj_name='"+rs.getString("name")+"' ) )"
						+" where t1.prj_name='"+rs.getString("name")+"' and DATE_FORMAT(build_date,'%Y-%m-%d')='"+DateTimeUtil.getToday()+"'";
				logger.info("strSql=" + strSql);
				i = pStmt2.executeUpdate(strSql);
				logger.info(" update hz_sonar_user2 count=" + i);

				strSql="update hz_sonar_user2 t1 set t1.login = (select t2.login from hz_usr_exist t2 where t1.kee=t2.kee and t2.prj_name='"
						+rs.getString("name")+"')	where t1.prj_name='"+rs.getString("name")+"' and DATE_FORMAT(build_date,'%Y-%m-%d')='"
						+DateTimeUtil.getToday()+"' and kee in (select kee from hz_usr_exist where prj_name='"+rs.getString("name")+"')";
				logger.info("strSql=" + strSql);
				i = pStmt2.executeUpdate(strSql);
				logger.info(" update hz_sonar_user2 count=" + i);		
				*/

				
				iCount += i;
				conn.commit();
				pStmt2.close();

			}

			rs.close();
			pStmt.close();

			logger.debug("写入文件完成,共写入行数：" + iCount);
			// putfile.delete();
		} catch (Exception ex)
		{
			logger.error(ex);
			ex.printStackTrace();
		} finally
		{
			AppConn.DoClear(conn);
		}

	}
}
