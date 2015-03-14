/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2014��5��5�� ����12:31:08 
* ��˵�� 
*/ 

package org.jpf.ci.dbs;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class SvnInfoCmd
{
	private static final Logger logger = LogManager.getLogger();
	/**
	 * 
	 */
	public SvnInfoCmd()
	{
		// TODO Auto-generated constructor stub
	}
	private static String strKey="����޸ĵ�ʱ��:";
	private static String strKey2="Last Changed Date:";
	public static String GetFileSvnDateTime(String strFileName)
	{
		String strCmd="svn info "+strFileName;
		return runexec(strCmd);
	}
	public static String runexec(String strCmd)
	{
		String strReValue="";
		try
		{
			logger.info(strCmd);
			String[] cmd = new String[] { "/bin/sh", "-c", strCmd };
			Process process = Runtime.getRuntime().exec(cmd);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			
			String line;
			while ((line = input.readLine()) != null)
			{

				//Sleep(1);
				//logger.info(line);
				if (line.trim().startsWith(strKey))
				{
					strReValue=line.trim().substring(strKey.length()+1);
					strReValue=strReValue.trim().substring(0,strReValue.indexOf("+")).trim();
					
				}else {
					if (line.trim().startsWith(strKey2))
					{
						strReValue=line.trim().substring(strKey2.length()+1);
						strReValue=strReValue.trim().substring(0,strReValue.indexOf("+")).trim();
						
					}
				}

			}
			process.waitFor();
			int iRetValue = process.exitValue();
		} catch (Exception e)
		{
			e.printStackTrace();

		}
		return strReValue;
	}
}
