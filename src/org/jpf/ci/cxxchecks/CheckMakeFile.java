/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2013-4-23 下午7:27:36 
 * 类说明 
 */

package org.jpf.cxxchecks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfFileUtil;

/**
 * 
 */
public class CheckMakeFile
{
	private static final Logger logger = LogManager.getLogger();
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		if (args.length == 1)
		{
			CheckMakeFile cCheckMakeFile = new CheckMakeFile(args[0]);
		} else
		{
			CheckMakeFile cCheckMakeFile = new CheckMakeFile("D:\\ob60\\public");
		}
		logger.info("game over");
	}
	public CheckMakeFile(String strFilePath)
	{
		doCheckMatch(strFilePath);
	}
	/**
	 * @todo:检查CPP文件不在makefile 里面 
	 * update 2013-4-23
	 */
	public void doCheckCpp(String strFilePath)
	{
		
	}
	/**
	 * @todo:检查是否有*.cpp,*.h
	 * @param strFilePath
	 * 被测试类名：TODO
	 * 被测试接口名:TODO
	 * 测试场景：TODO
	 * 前置参数：TODO
	 * 入参：
	 * 校验值：
	 * 测试备注：
	 * update 2013-4-23
	 */
	public void doCheckMatch(String strFilePath)
	{
		try
		{
			StringBuffer sbBuffer = new StringBuffer();
			File file = new File(strFilePath);
			if (file.exists() && file.isDirectory())
			{
				Vector<String> vector = new Vector<String>();
				JpfFileUtil.GetFiles(strFilePath, vector);
				for (int i = 0; i < vector.size(); i++)
				{
					String tmpString = (String) vector.get(i);
					if (tmpString.indexOf("Makefile") > 0)
					{
						File file2 = new File(tmpString);
						InputStreamReader read = new InputStreamReader(new FileInputStream(file2), "UTF-8");

						BufferedReader reader = new BufferedReader(read);

						String line;
						int iRowNum = 0;
						while ((line = reader.readLine()) != null)
						{
							iRowNum++;
							// System.out.println(line);
							if (line.indexOf("*.") > 0)
							{
								sbBuffer.append(line);
								logger.info("File Name:" + tmpString);
								logger.info("Line Number:" + iRowNum);
							}
						}
						reader.close();
						read.close();
					}
				}
			} else
			{
				logger.info("Error Input Path");
			}
		} catch (Exception ex)
		{
			// TODO: handle exception
			logger.error(ex);
		}

	}
}
