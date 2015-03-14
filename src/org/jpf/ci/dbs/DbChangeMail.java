/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2013��12��13�� ����10:17:11 
* ��˵�� 
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
	private String[] strMailTitle = {"DB���ִ�гɹ�(�Զ�����)","DB���ִ��ʧ��(�Զ�����)","DB���ִ�гɹ����и澯(�Զ�����)"};
	
	private String strNextDateTime="";
	//0:�ɹ���1 ʧ�ܣ�2 �и澯
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
		/* �ж��Ƿ���windows */
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
			if (line.startsWith("Last Changed Author:")||line.startsWith("����޸ĵ�����:"))
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
