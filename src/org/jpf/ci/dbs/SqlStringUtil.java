/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2014年3月30日 下午11:13:13 
* 类说明 
*/ 

package org.jpf.ci.dbs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class SqlStringUtil
{
	private static final Logger logger = LogManager.getLogger(SqlStringUtil.class);
	/**
	 * 
	 */
	public SqlStringUtil()
	{
		// TODO Auto-generated constructor stub
	}
	public static String TrimSql(String strSql)
	{
		if (strSql.endsWith("\n"))
		{
			strSql = strSql.substring(0, strSql.length() - 1);
		}
		if (strSql.endsWith("\r"))
		{
			strSql = strSql.substring(0, strSql.length() - 1);
		}
		return strSql;
	}
	
	public static String RemoveSqlNote(String strSqlText)
	{
		Pattern pattern = Pattern.compile("[///*].*[//*/ ]");
		Matcher  matcher = pattern.matcher(strSqlText);
		strSqlText=matcher.replaceAll("");  
		strSqlText=strSqlText.trim();
		logger.debug("FormatSql={}" , strSqlText);
		return  		strSqlText;
	}
}
