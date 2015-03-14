/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2013-4-16 下午9:07:08 
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
public class AutoJavaJobs
{

	/**
	 * @param args
	 *            被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注：
	 *            update 2013-4-16
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		AutoJavaJobs cAutoJavaJobs = new AutoJavaJobs();
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
	
	public AutoJavaJobs()
	{
		doWork("topup_payment","");
		doWork("productmgnt","");
	}
	private void doWork(String strProdNameString,String strSubString )
	{
		try
		{

			String strConfig = getFileTxt("javajobs.txt");

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
					System.out.println(f2[i].getName());
					if (f2[i].isDirectory())
					{
						File mFile=new File(f2[i]+"/pom.xml");
						if (mFile.exists())
						{
						String tmpString = strConfig;
						
						String myString="";
						if (strSubString.length()>0)
						{
							myString="/source/ob_dev/openbilling60/" +strSubString+"/";
						}
						
						tmpString = tmpString.replaceAll("/source/ob_dev/openbilling60/infosystem/ims-core",
								myString + strProdNameString + "/" + f2[i].getName());
						// http://10.10.10.141/svn/svnfiles/source/ob_dev/openbilling60/public/xc
						String filePathString = "d:\\wupf\\" + strProdNameString + "\\" + strProdNameString + "."
								+ f2[i].getName() + "\\";
						JpfFileUtil.SaveFile(
								filePathString + "config.xml",
								new StringBuffer(tmpString));
						}
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
