/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2013年12月10日 下午3:32:01 
 * 类说明 
 */

package org.jpf.ci.dbs;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfFileUtil;

/**
 * 
 */
public class DbChangeCheck
{
	private static final Logger logger = LogManager.getLogger(DbChangeCheck.class);
	
	public DbChangeCheck()
	{
		
	}
	public static void CheckTableName(String strTableName)throws Exception
	{
		String _tmpTableName = strTableName.replaceAll("`", "");
		if ((_tmpTableName.split("\\.").length != 2))
		{
			throw new Exception("error table name,no database selected:=" + strTableName);
		}
		String strOldDbName = _tmpTableName.split("\\.")[1];
		String regex = ".*_[0-9].*";
		if (strOldDbName.matches(regex))
		{
			throw new Exception(" sub table should not appear in DDL :=" + _tmpTableName);
		}
		
	}
	
	public static void CheckFileName(String strFilePathName,String strTableName)throws Exception
	{
		String _tmpTableName = strTableName.replaceAll("`", "");
		String strOldDbName = _tmpTableName.split("\\.")[0];
		String strDomainName = JpfFileUtil.GetFilePath(strFilePathName);
		strDomainName = strDomainName.substring(strDomainName.lastIndexOf(java.io.File.separator) + 1);
		logger.info("strDomainName=" + strDomainName);
		if (!strOldDbName.equalsIgnoreCase(strDomainName))
		{
			throw new Exception("错误的域名：SQL语句域名和文件目录不匹配");
		}
		CheckFileName(strFilePathName);
		
	}
	/**
	 * @todo 文件名称不能有空格
	 * @param strFilePathName
	 * @throws Exception
	 * update 2014年4月3日
	 */
	public static void CheckFileName(String strFilePathName)throws Exception
	{
		String regex = ".*\\s.*";
		if (strFilePathName.matches(regex))
		{
			throw new Exception("错误的文件名：文件名称不能有空格");
		}
	}	
	/**
	 * @todo 文件名称不能有空格
	 * @param strFilePathName
	 * @throws Exception
	 * update 2014年4月3日
	 */
	public static void CheckFileName2(String strFilePathName,String strDomainName)throws Exception
	{
		String regex = "^Telenor_REQ_20140326_0052_sd.sql";
		if (strFilePathName.matches(regex))
		{
			throw new Exception("错误的文件名：文件名称不能有空格");
		}
	}	
}
