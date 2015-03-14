/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2013-4-16 下午4:34:45 
 * 类说明 
 */

package org.jpf.ci;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jpf.utils.JpfFileUtil;

/**
 * 
 */
public class AutoCxxJobs
{

	/**
	 * @param args
	 *            被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注：
	 *            update 2013-4-16
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		AutoCxxJobs cAutoJobs = new AutoCxxJobs();

	}

	private String getFileTxt(String fileName) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		File f = new File(fileName);

		InputStreamReader read = new InputStreamReader(new FileInputStream(f), "UTF-8");

		BufferedReader reader = new BufferedReader(read);

		String line;

		while ((line = reader.readLine()) != null)
		{

			// System.out.println(line);
			sb.append(line).append("\n");

		}
		reader.close();
		read.close();
		return sb.toString();
	}

	public AutoCxxJobs()
	{
		/*
		doWork("public","");
		doWork("dbe_v2","cpf");
		doWork("billing_overseas","");
		doWork("billing","");
		doWork("mediation","");
		doWork("user_mdb","rating_billing");
		doWork("util","");
		
		doWork("balance_overseas","");
		*/
		//doWork("rating","rating_billing");
		//doWork("mediation","");
		//doWork("rating","rating_billing");
		//doWork("billing","");
		//doWork("balance_overseas","");
		//doWork("billing_overseas","");
		//doWork("public","");
		//doWork("balance","");
		//doWork("dbe_v2","cpf");
		doWork("dbe","cpf");
		//doWork("util","");
	}
	private void doWork(String strProdNameString,String strSubString)
	{
		try
		{

			String strConfig = getFileTxt("cxxjobs.txt");
			String strProp = getFileTxt("cxxprop.txt");
			System.out.println(strProp);
			String strCheckPathString="D:\\ob60\\";
			if(strSubString.length()>0)
			{
				strCheckPathString=strCheckPathString+strSubString+"/";
			}
			File f1 = new File(strCheckPathString+ strProdNameString);
			if (f1.isDirectory())
			{
				File[] f2 = f1.listFiles();

				for (int i = 0; i < f2.length; i++)
				{
					if (f2[i].isDirectory())
					{
						System.out.println(f2[i].getName());
						String tmpString = strConfig;
						String myString="/source/ob_dev/openbilling60";
						if (strSubString.length()>0)
						{
							myString=myString +"/"+strSubString;
						}
						tmpString = tmpString.replaceAll("/source/ob_dev/openbilling60/public/xc",
								myString + "/"+strProdNameString + "/" + f2[i].getName());
						tmpString = tmpString.replaceAll("/source/ob_dev/openbilling/public xc",
								myString +" "+ strProdNameString + " " + f2[i].getName());
						tmpString = tmpString.replaceAll("/runsonar.sh","/runsonar.sh");
						// http://10.10.10.141/svn/svnfiles/source/ob_dev/openbilling60/public/xc
						String filePathString = "d:\\wupf\\" + strProdNameString + "\\" + strProdNameString + "."
								+ f2[i].getName() + "\\";
						JpfFileUtil.SaveFile(
								filePathString + "config.xml",
								new StringBuffer(tmpString));

						tmpString = strProp;
						tmpString = tmpString.replaceAll("ob60:balance", ""+strProdNameString + ":" + f2[i].getName());
						tmpString = tmpString.replaceAll("sonar.projectName=balance",
								"sonar.projectName=" + strProdNameString+":"+f2[i].getName());
						tmpString = tmpString.replaceAll("sonar.sources=balance", "sonar.sources=" + f2[i].getName());
						tmpString = tmpString.replaceAll("wupf", "" + f2[i].getName());
						// http://10.10.10.141/svn/svnfiles/source/ob_dev/openbilling60/public/xc
						filePathString = "d:\\wupf\\" + strProdNameString + "\\" + strProdNameString + "."
								+ f2[i].getName() + ".properties";
						JpfFileUtil.SaveFile(
								filePathString,
								new StringBuffer(tmpString));
					}
				}
			}
			System.out.println("game over");
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
	}
}
