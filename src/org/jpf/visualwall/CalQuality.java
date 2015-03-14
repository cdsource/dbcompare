/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2014年3月23日 上午12:56:12 
* 类说明 
*/ 

package org.jpf.visualwall;

import java.sql.Connection;





import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.ci.AppConn;
import org.jpf.utils.JpfDbUtils;

/**
 * 
 */
public class CalQuality
{
	private static final Logger logger =  LogManager.getLogger(CalQuality.class);
	/**
	 * 
	 */
	public CalQuality()
	{
		// TODO Auto-generated constructor stub
		Connection conn=null;
		logger.info("CalQuality");
		try
		{
			conn=AppConn.GetInstance().GetConn();
			conn.setAutoCommit(false);
			java.sql.Statement stmt=conn.createStatement();
			String strSql="delete from hz_rpt where is_last=1 and DATE_FORMAT(build_date,'%Y-%m-%d')=CURRENT_DATE";
			logger.info(strSql);
			int i=stmt.executeUpdate(strSql);
			logger.info("rows count={}",i);
			
			strSql="update hz_rpt set is_last=0 where is_last=1 and DATE_FORMAT(build_date,'%Y-%m-%d')<CURRENT_DATE";
			logger.info(strSql);
			i=stmt.executeUpdate(strSql);
			logger.info("rows count={}",i);
			
			strSql="insert into hz_rpt(prj_id,build_date,prj_name,is_last,branch_coverage,branch_coverage_c) "
					+" select t1.id,DATE_FORMAT(t2.build_date,'%Y-%m-%d') build_date,t1.name,1,"
					+" max(case t3.metric_id when 50 then t3.value end)branch_coverage "
					+" ,max(case t3.metric_id when 50 then t3.variation_value_1 end)branch_coverage2"
					+" from projects t1,snapshots t2,project_measures t3,hz_prj t4"
					+" where t1.scope='PRJ' and t1.id=t4.prj_id and t1.enabled=1"
					+" and t1.id=t2.project_id  and t2.islast=1  and t2.id=t3.snapshot_id  AND t3.rule_id IS NULL  and"
					+" t3.metric_id in (50)   group by t1.id,t4.prj_mail order by t1.name";
			logger.info(strSql);
			i=stmt.executeUpdate(strSql);
			logger.info("rows count={}",i);
			
			strSql="delete from hz_rpt where is_last=1 and prj_id=0;";
			logger.info(strSql);
			i=stmt.executeUpdate(strSql);
			logger.info("rows count={}",i);
			
			strSql="insert into hz_rpt(prj_id,build_date,prj_name,is_last,branch_coverage,branch_coverage_c)"
					+" select 0,CURRENT_DATE,'avg',1,sum(branch_coverage)/22,sum(branch_coverage_c)/22 from hz_rpt "
					+" where is_last=1 and prj_id<>0";
			logger.info(strSql);
			i=stmt.executeUpdate(strSql);
			logger.info("rows count={}",i);
			conn.commit();
			System.out.println("game over");
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}finally
		{
			JpfDbUtils.DoClear(conn);
		}
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
	 * update 2014年3月23日
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		CalQuality cCalQuality=new CalQuality();
	}

}
