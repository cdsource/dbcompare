/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年2月8日 下午10:09:38 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.sql.ResultSet;


/**
 * 
 */
public class CheckSameTableName extends AbstractCheckTable
{
	private final String TitleName="CheckSameTableName";
	//private final String CheckCountSql = "select * from information_schema.TABLES where table_name in (select table_name from information_schema.TABLES t1 group by table_name having count(*)>1) order by table_name";
	private final String CheckCountSql = "select * from information_schema.TABLES where table_schema=? and table_name in (select table_name from information_schema.TABLES t1 group by table_name having count(*)>1) order by table_name";
	/**
	 * 
	 */
	public CheckSameTableName()
	{

	}

	/**
	 * @param args
	 *            被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注：
	 *            update 2015年2月8日
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		CheckSameTableName cCheckSameTableName = new CheckSameTableName();
	}



	/* (non-Javadoc)
	 * @see org.jpf.ci.dbs.compare.AbstractCheckTable#GetCheckSql()
	 */
	@Override
	String GetCheckSql()
	{
		// TODO Auto-generated method stub
		return CheckCountSql;
	}

	/* (non-Javadoc)
	 * @see org.jpf.ci.dbs.compare.AbstractCheckTable#GetCheckTitle()
	 */
	@Override
	String GetCheckTitle()
	{
		// TODO Auto-generated method stub
		return TitleName;
	}

	/* (non-Javadoc)
	 * @see org.jpf.ci.dbs.compare.AbstractCheckTable#FormatOutput(java.sql.ResultSet)
	 */
	@Override
	void FormatOutput(ResultSet rs) throws Exception
	{
		// TODO Auto-generated method stub
		String strTableName = "";
		while (rs.next())
		{
			if (strTableName.equalsIgnoreCase(rs.getString("table_name")))
			{
				sb.append(rs.getString("table_schema")).append(":").append(rs.getString("table_name")).append("\n");
			}else {
				strTableName=rs.getString("table_name");
				sb.append("\n").append(rs.getString("table_schema")).append(":").append(rs.getString("table_name")).append("\n");
			}
		}
	}
}
