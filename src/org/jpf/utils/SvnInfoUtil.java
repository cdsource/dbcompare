/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo.com 
* @version ����ʱ�䣺2015��3��24�� ����9:22:16 
* ��˵�� 
*/ 

package org.jpf.utils;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Properties;

/**
 * 
 */
public class SvnInfoUtil
{

	/**
	 * 
	 */
	public SvnInfoUtil()
	{
		// TODO Auto-generated constructor stub
	}
	public static String GetSvnFileAuthorDate(String strSvnUrlFileName) throws Exception
	{
		/* �ж��Ƿ���windows */
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");

		if (os.trim().toLowerCase().startsWith("windows"))
		{
			return "";
		}
		String strResult = "";
		// logger.info(strFileName);
		String strCmd = "svn info " + strSvnUrlFileName+" | sed -n '8,$p'";

		String[] cmd = new String[] { "/bin/sh", "-c", strCmd };
		Process process = Runtime.getRuntime().exec(cmd);
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);

		String line;
		while ((line = input.readLine()) != null)
		{
			strResult+=line.trim();

		}
		process.waitFor();
		
		return strResult;
	}
}
