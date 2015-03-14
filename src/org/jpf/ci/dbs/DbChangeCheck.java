/** 
 * @author ��ƽ�� 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version ����ʱ�䣺2013��12��10�� ����3:32:01 
 * ��˵�� 
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
			throw new Exception("�����������SQL����������ļ�Ŀ¼��ƥ��");
		}
		CheckFileName(strFilePathName);
		
	}
	/**
	 * @todo �ļ����Ʋ����пո�
	 * @param strFilePathName
	 * @throws Exception
	 * update 2014��4��3��
	 */
	public static void CheckFileName(String strFilePathName)throws Exception
	{
		String regex = ".*\\s.*";
		if (strFilePathName.matches(regex))
		{
			throw new Exception("������ļ������ļ����Ʋ����пո�");
		}
	}	
	/**
	 * @todo �ļ����Ʋ����пո�
	 * @param strFilePathName
	 * @throws Exception
	 * update 2014��4��3��
	 */
	public static void CheckFileName2(String strFilePathName,String strDomainName)throws Exception
	{
		String regex = "^Telenor_REQ_20140326_0052_sd.sql";
		if (strFilePathName.matches(regex))
		{
			throw new Exception("������ļ������ļ����Ʋ����пո�");
		}
	}	
}
