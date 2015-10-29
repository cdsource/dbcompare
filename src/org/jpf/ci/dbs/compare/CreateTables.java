/** 
* @author 吴平福 
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年5月28日 上午10:41:08 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.ResultSet;
import org.jpf.utils.JpfDbUtils;

/**
 * 
 */
public class CreateTables
{

	/**
	 * 
	 */
	public CreateTables()
	{
		// TODO Auto-generated constructor stub
	}
<<<<<<< HEAD
	public static String getCreateTableSql(Connection conn_pdm,String strDomainName,String strTableName)
=======
	public static String GetCreateTableSql(Connection conn_pdm,String strDomainName,String strTableName)
>>>>>>> origin/master
	{
		String strSql="";
		try
		{
			if (strTableName.indexOf(".")>0)
			{
				strTableName=strTableName.substring(strTableName.indexOf(".")+1,strTableName.length());
			}
			String mStrSql="show create table "+strDomainName+"."+strTableName;

<<<<<<< HEAD
	        ResultSet rs=JpfDbUtils.execSqlQuery(conn_pdm, mStrSql);
=======
	        ResultSet rs=JpfDbUtils.ExecSqlQuery(conn_pdm, mStrSql);
>>>>>>> origin/master
	        if (rs.next())
	        {
	        	strSql=rs.getString("Create Table").toUpperCase()+";";
	        	strSql=strSql.replaceFirst("CREATE TABLE `", "CREATE TABLE "+strDomainName.toUpperCase()+"."+"`");
	        }
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
		return strSql;
	}
}
