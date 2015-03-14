/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2014年12月15日 下午10:14:48 
* 类说明 
*/ 

package org.jpf.visualwall.gathers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class QualityInfoBJ extends AbstractQualityInfo
{
	private static final Logger logger =  LogManager.getLogger();

	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetDBUsr()
	 */
	@Override
	String GetDBUsr()
	{
		// TODO Auto-generated method stub
		return "sonar";
	}

	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetDBPwd()
	 */
	@Override
	String GetDBPwd()
	{
		// TODO Auto-generated method stub
		return "sonar";
	}

	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetDBUrl()
	 */
	@Override
	String GetDBUrl()
	{
		// TODO Auto-generated method stub
		return "jdbc:mysql://10.1.228.11:4306/sonar";
	}

	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetQuerySql()
	 */
	@Override
	String GetQuerySql()
	{
		// TODO Auto-generated method stub
		String strSql="select t1.id prj_id,DATE_FORMAT(t2.build_date,'%Y-%m-%d') build_date,"
				+" t1.name prj_name, max(case t3.metric_id when 80 then t3.value end)blocker_violations,"
				+" max(case t3.metric_id when 81 then t3.value end)critical_violations,"
				+" max(case t3.metric_id when 82 then t3.value end)major_violations"
				+" ,max(case t3.metric_id when 3 then t3.value end)prj_lines"
				+" ,MIN(CASE t3.metric_id WHEN 30 THEN t3.value  END)tests" 
				+" ,MAX(CASE t3.metric_id WHEN 35 THEN t3.value/100  END)test_success_density" 
				+" ,MAX(CASE t3.metric_id WHEN 43 THEN t3.value/100  END)line_coverage" 
				+" ,MAX(CASE t3.metric_id WHEN 50 THEN t3.value/100  END)branch_coverage" 
				+" from projects t1,snapshots t2,project_measures t3"
				+" where t1.scope='PRJ'   and t1.enabled=1 and t1.id=t2.project_id"
				+" and t2.islast=1  and t2.id=t3.snapshot_id  AND t3.rule_id IS NULL  and t3.metric_id in (3,30,35,43,50,80,81,82)"
				+" group by t1.id order by t1.name ";
		logger.info(strSql);
		return strSql;
	}

	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetAreaName()
	 */
	@Override
	String GetAreaName()
	{
		// TODO Auto-generated method stub
		return "BJ";
	}

	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetPrjCount()
	 */
	@Override
	int GetPrjCount()
	{
		// TODO Auto-generated method stub
		return 14;
	}
	public static void main(String[] args)
	{
		QualityInfoBJ cQualityInfoBJ=new QualityInfoBJ();
	}

	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetExcludePrjSql()
	 */
	@Override
	String GetExcludePrjSql()
	{
		// TODO Auto-generated method stub
		return "";
	}
}
