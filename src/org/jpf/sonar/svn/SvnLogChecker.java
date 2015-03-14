/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2013-7-22 上午11:41:08 
 * 类说明 
 */

package org.jpf.sonar.svn;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDateTimeUtil;


/**
 * 
 */
public class SvnLogChecker
{
	private static final Logger logger =  LogManager.getLogger(SvnLogChecker.class);
	private String strCmd = "";
	private int strRshRet;
	
	private StringBuffer sbBuffer = new StringBuffer();

	/**
	 * @param args
	 *            被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注：
	 *            update 2013-7-22
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		/*
		String tmpStr="[ERROR]2";
		String regEx="\\[(ERROR)|(error)\\]";
		//String regEx = "[^0-9a-zA-Z]ERROR[^0-9a-zA-Z][ ]|(?i)[ ](exception|fatal|fail(ed|ure)|un(defined|resolved))[ ]"; // 表示a或F
		//String regEx = "(?i)[ ](error|exception|fatal|fail(ed|ure)|un(defined|resolved))[ ]"; // 表示a或F
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(tmpStr);
		if (mat.find())
				System.out.println("ok");
		System.out.println("game over");
		*/
		if (2 == args.length)
		{
			SvnLogChecker cSvnLogChecker = new SvnLogChecker(args[0], args[1]);
		} else
		{
			SvnLogChecker cSvnLogChecker = new SvnLogChecker(JpfDateTimeUtil.GetDay("yyyy-MM-dd", -1),
					JpfDateTimeUtil.GetDay("yyyy-MM-dd", 0));
		}
		
	}

	private void test()
	{
		String tmpStr = "解决单元测试日志量太大问题。sonar###UT###10min";
		// System.out.println(tmpStr);
		boolean isok = false;
		String regEx = "sonar###vi###[0-9]+min"; // 表示a或F
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(tmpStr.toLowerCase());
		if (mat.find())
			isok = true;
		regEx = "sonar###ut###[0-9]+min"; // 表示a或F
		pat = Pattern.compile(regEx);
		mat = pat.matcher(tmpStr.toLowerCase());
		if (mat.find())
			isok = true;
		regEx = "sonar###cc###[0-9]+min"; // 表示a或F
		pat = Pattern.compile(regEx);
		mat = pat.matcher(tmpStr.toLowerCase());
		if (mat.find())
			isok = true;

		if (!isok)
		{
			System.out.println(tmpStr);
			/*
			 * SonarMail.sendMail("", tmpStrings[1].trim() + ":" +
			 * tmpStrings[2].substring(0, 20).trim() + ":" + tmpStr, "GBK",
			 * "SONAR SVN LOG违规(自动发出)");
			 */
		}
	}

	public SvnLogChecker(String strStartDate, String strEndDate)
	{
		if (strStartDate.length() > 0 && strEndDate.length() > 0)
		{
			logger.info(" args[0]:=" + strStartDate);
			logger.info(" args[1]:=" + strEndDate);
			String[] strSvnUrls={"openbilling60_cmcc/billing/","openbilling60_cmcc/balance/","openbilling60_cmcc/mediation/"
					,"openbilling60_cmcc/rating_billing/","openbilling60/infosystem/","openbilling60/topup_payment/"
					,"openbilling60/productmgnt/","openbilling60/so/redomanager/","openbilling60/so/sysmgnt/","openbilling60/so/billsamplingtool/","openbilling60/so/jobmanager/"
					,"openbilling60/balance/","openbilling60/balance_overseas/","openbilling60/billing/","openbilling60/util/"
					,"openbilling60/mediation/","openbilling60/rating_billing/rating/","openbilling60/rating_billing/user_mdb/"
					,"openbilling60/public/","openbilling60/rating_billing/cpf/dbe_v2/","openbilling60/cpf/dbe/"
					,"openbilling60/easyframe/","openbilling60/rating_billing/cpf/dbm_v2/"};
			for(int i=0;i<strSvnUrls.length;i++)
			{
				strCmd = "svn log --username build --password 123 -v -r {" + strStartDate + "}:{" + strEndDate
						+ "} http://10.10.10.141/svn/svnfiles/source/ob_dev/"+strSvnUrls[i];
				logger.info(strCmd);
				// test();
				runSvnCheck();
					
			}

		} else
		{
			logger.info("error input: args[0]" + strStartDate);
		}
	}

	private String runSvnCheck()
	{
		try
		{
			String[] cmd = new String[] { "/bin/sh", "-c", strCmd };
			Process process = Runtime.getRuntime().exec(cmd);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			String line;

			while ((line = input.readLine()) != null)
			{
				// System.out.println(line);
				// MakeSql(line);
				sbBuffer.append(line).append("\n");
				/*
				 * line=line.trim(); if(line.equals(
				 * "------------------------------------------------------------------------"
				 * )) { isNew=true; }else { isNew=false; }
				 * 
				 * if(!isNew) { if(line.startsWith("r")) {
				 * strUser=line.split("\\|")[1]; }
				 * 
				 * }
				 */
			}
			process.waitFor();
			int iRetValue = process.exitValue();
			setstrRshRet(iRetValue);
			SendMail();
			// System.out.println(sBuffer);
			return "";
		} catch (IOException e)
		{
			e.printStackTrace();

		} catch (Exception e)
		{
			e.printStackTrace();

		}
		return "";
	}

	private void setstrRshRet(int value)
	{
		this.strRshRet = value;
	}



	private void SendMail()
	{
		
		
		String[] strResults = sbBuffer.toString().split(
				"------------------------------------------------------------------------");
		for (int i = 0; i < strResults.length; i++)
		{
			// System.out.println(strResults[i]);
			String[] tmpStrings = strResults[i].split("\\|");
			if (tmpStrings.length > 3)
			{
				// System.out.println(tmpStrings[1]);
				// strShowString=tmpStrings[1];
				String tmpStr = tmpStrings[3];
				if (tmpStr.indexOf("\n\n") > 0)
				{
					// System.out.println(tmpStr.substring(tmpStr.indexOf(strSourceString)));
					String tmpFileInfo=tmpStr.substring(0,tmpStr.indexOf("\n\n"));
					tmpStr = tmpStr.substring(tmpStr.indexOf("\n\n")).trim();
					if (tmpStr.length() > 0)
					{
						// System.out.println(tmpStrings[1].trim() + ":" +
						// tmpStrings[2].substring(0, 20).trim() + ":"+ tmpStr);
						//int j = tmpStr.toLowerCase().indexOf("sonar");
						//System.out.println(tmpFileInfo.indexOf("/test/"));
						
						if (tmpStr.toLowerCase().indexOf("sonar")>= 0 || tmpFileInfo.indexOf("/test/")>0)
						{
							
							boolean isok = false;
							String regEx = "sonar###vi###[0-9]+min"; // 表示a或F
							Pattern pat = Pattern.compile(regEx);
							Matcher mat = pat.matcher(tmpStr.toLowerCase());
							if (mat.find())
								isok = true;
							regEx = "sonar###ut###[0-9]+min"; // 表示a或F
							pat = Pattern.compile(regEx);
							mat = pat.matcher(tmpStr.toLowerCase());
							if (mat.find())
								isok = true;
							regEx = "sonar###cc###[0-9]+min"; // 表示a或F
							pat = Pattern.compile(regEx);
							mat = pat.matcher(tmpStr.toLowerCase());
							if (mat.find())
							isok = true;
							// System.out.println(tmpStrings[1].trim() + ":" +
							// tmpStrings[2].substring(0, 20).trim() + ":"+
							// tmpStr);
							if (!isok)
							{
								System.out.println(tmpStrings[0].trim() + ";" + tmpStrings[1].trim() + ";"
										+ tmpStrings[2].substring(0, 20).trim() + ";"
										+ tmpStr);
								if (tmpFileInfo.indexOf("/test/")>0)
								{
								System.out.println(tmpFileInfo);
								}
								tmpStr += "<p>规范写法：</p><p>修改违规10分钟：sonar###VI###10min</p><p>增加单元测试10分钟：sonar###UT###10min</p><p>修改圈复杂度（包含NCSS数目）10分钟：sonar###CC###10min</p>";
								
								SonarMail.sendMail(tmpStrings[1].trim(), strResults[i]
										+ tmpStr, "GBK", "SONAR SVN LOG违规(自动发出)");
								/*
								SonarMail.sendMail("", strResults[i]
										+ tmpStr, "GBK", "SONAR SVN LOG违规(自动发出)");
								*/		
							}

						}
					}
					// strShowString+=":"+tmpStr.substring(tmpStr.indexOf("\n\n"));
				}
			} else
			{
				// System.out.println(strResults[i]);
			}

		}
		sbBuffer.setLength(0);
	}
}
