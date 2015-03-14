/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2012-8-13 下午1:19:46 
* 类说明 
*/ 

package org.jpf.svn;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.jpf.utils.FileUtil;
import org.jpf.utils.JpfStringUtil;


/**
 * 
 */
public class CheckFileName
{
	private static final Logger LOGGER = Logger.getLogger(CheckFileName.class);
	
	public CheckFileName(String strFilePath,boolean verbose)
	{
		DoWork(strFilePath,verbose);
	}
	/**
	 * 
	 */
	public CheckFileName(String strFilePath)
	{
		// TODO Auto-generated constructor stub
		DoWork(strFilePath,false);
	}
   private void DoWork(String strFilePath,boolean verbose)
   {
	   	if (verbose)
	   		LOGGER.debug("check file path:="+strFilePath);
	   if (strFilePath==null || "".equalsIgnoreCase(strFilePath))
		{
			LOGGER.error("input param is null");
			return;
		}
		try
		{
			Vector<String> vector=new Vector<String>();
			FileUtil.GetFiles(strFilePath, vector);
			if (verbose)
		   		LOGGER.info("check file count:="+vector.size());
			for(int i=0;i<vector.size();i++)
			{
				String tmpString=(String)vector.get(i);
				if(JpfStringUtil.IsChinese(tmpString))
				{
					LOGGER.info(tmpString);
					FileUtil.DelFile(tmpString);
				}
				if (i % 100 == 0)
				{
					System.out.println(i);
				}
			}
			LOGGER.info("game over");
		} catch (Exception ex)
		{
			// TODO: handle exception
			LOGGER.error(ex);
		}
   }
	/**
	 * @param args
	 * 被测试类名：TODO
	 * 被测试接口名:TODO
	 * 测试场景：TODO
	 * 前置参数：TODO
	 * 入参：
	 * 校验值：
	 * 测试备注：
	 * update 2012-8-13
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub


			if(args.length==1)
			{
				CheckFileName cCheckFileName=new CheckFileName(args[0].trim(),true);
			}else
			{
			CheckFileName cCheckFileName=new CheckFileName("D:/Jenkins/workspace/easyframe");
			}
	}

}
