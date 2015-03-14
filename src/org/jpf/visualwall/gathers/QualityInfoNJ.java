/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2014年11月28日 下午2:24:04 
* 类说明 
*/ 

package org.jpf.visualwall.gathers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class QualityInfoNJ extends AbstractQualityInfo
{
	private static final Logger logger =  LogManager.getLogger();
	/**
	 * 
	 */
	public QualityInfoNJ()
	{
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetDBUsr()
	 */
	@Override
	String GetDBUsr()
	{
		// TODO Auto-generated method stub
		return "sonar_crm";
	}

	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetDBPwd()
	 */
	@Override
	String GetDBPwd()
	{
		// TODO Auto-generated method stub
		return "sonar_123";
	}

	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetDBUrl()
	 */
	@Override
	String GetDBUrl()
	{
		// TODO Auto-generated method stub
		return "jdbc:mysql://10.11.20.111:3901/sonar_crm";
	}


	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetAreaName()
	 */
	@Override
	String GetAreaName()
	{
		// TODO Auto-generated method stub
		return "NJ";
	}
	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetQuerySql()
	 */
	@Override
	String GetQuerySql()
	{

		// TODO Auto-generated method stub
		String strSql="select t1.id prj_id,DATE_FORMAT(t2.build_date,'%Y-%m-%d') build_date,"
				+" t1.name prj_name, max(case t3.metric_id when 95 then t3.value end)blocker_violations,"
				+" max(case t3.metric_id when 96 then t3.value end)critical_violations,"
				+" max(case t3.metric_id when 97 then t3.value end)major_violations,"
				+" max(case t3.metric_id when 3 then t3.value end)prj_lines"
				+" ,MIN(CASE t3.metric_id WHEN 30 THEN t3.value  END)tests" 
				+" ,MAX(CASE t3.metric_id WHEN 35 THEN t3.value/100  END)test_success_density" 
				+" ,MAX(CASE t3.metric_id WHEN 43 THEN t3.value/100  END)line_coverage" 
				+" ,MAX(CASE t3.metric_id WHEN 50 THEN t3.value/100  END)branch_coverage" 
				+" from projects t1,snapshots t2,project_measures t3"
				+" where t1.scope='PRJ'  and t1.enabled=1 and t1.id=t2.project_id and t1.name<>'cs-ct'"
				+" and t2.islast=1  and t2.id=t3.snapshot_id  AND t3.rule_id IS NULL  and t3.metric_id in (3,30,35,43,50,95,96,97)"
				+" group by t1.id order by t1.name ";
		logger.info(strSql);
		return strSql;
	}

	public static void main(String[] args)
	{
		QualityInfoNJ cQualityInfoNJ=new QualityInfoNJ();
	}

	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetPrjCount()
	 */
	@Override
	int GetPrjCount()
	{
		// TODO Auto-generated method stub
		return 17;
	}

	/* (non-Javadoc)
	 * @see org.jpf.walls.AbstractQualityInfo#GetExcludePrjSql()
	 */
	@Override
	String GetExcludePrjSql()
	{
		// TODO Auto-generated method stub
		return 		" and prj_name<>'cs-ct' and prj_name <>'cs-ob'";
	}



}
