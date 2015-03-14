/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2013年12月13日 上午10:17:11 
* 类说明 
*/ 

package org.jpf.ci.dbs;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.mails.SonarMail;
import org.jpf.utils.JpfFileUtil;



/**
 * 
 */
public class DbChangeMail
{
	private static final Logger logger =LogManager.getLogger(DbChange.class);
	private String[] strMailTitle = {"DB变更执行成功(自动发送)","DB变更执行失败(自动发送)","DB变更执行成功但有告警(自动发送)"};
	
	private String strNextDateTime="";
	//0:成功：1 失败，2 有告警
	StringBuffer sBuffer=new StringBuffer();
	String strMsg="";
	
	public DbChangeMail(String strNextDateTime)
	{
		this.strNextDateTime = strNextDateTime;
	}
	public void ClearMail()
	{
		sBuffer.setLength(0);
		strMsg="";
	}
	public void SendMail(String strFileName,int iResult)
	{
		try
		{
			String strAuthor=GetFileAuthor(strFileName);
			SonarMail.sendMail(strAuthor+",xuedl",
					strMailTitle[iResult] +"<br>"+strMsg
							, "GBK", strMailTitle[iResult]);
			ClearMail();
			DbChangeInfo cDbChangeInfo=new DbChangeInfo(strFileName,strAuthor,iResult);
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			logger.error(ex);
		}

	}	
	private String GetFileAuthor(String strFileName) throws Exception
	{
		/* 判断是否是windows */
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");

		if (os.trim().toLowerCase().startsWith("windows"))
		{
			return "";
		}
		String strResult = "";
		// logger.info(strFileName);
		String strFilePath = JpfFileUtil.GetFilePath(strFileName);
		// logger.info(strFilePath);
		strFileName = JpfFileUtil.GetFileName(strFileName);
		// logger.info(strFileName);
		String strCmd = "cd " + strFilePath + ";svn info " + strFileName;

		String[] cmd = new String[] { "/bin/sh", "-c", strCmd };
		Process process = Runtime.getRuntime().exec(cmd);
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);

		String line;
		while ((line = input.readLine()) != null)
		{
			line = line.trim();

			// MakeSql(line);
			// Sleep(1);
			if (line.startsWith("Last Changed Author:")||line.startsWith("最后修改的作者:"))
			{

				int i = line.indexOf(":");
				strResult = line.substring(i + 1).trim();
				logger.info(strResult);
			}
		}
		process.waitFor();
		// int iRetValue = process.exitValue();
		// setstrRshRet(iRetValue);
		
		return strResult;
	}
}
