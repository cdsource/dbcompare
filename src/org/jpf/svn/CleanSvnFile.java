/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2012-8-14 下午4:04:05 
* 类说明 
*/ 

package org.jpf.svn;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.jpf.utils.FileUtil;

/**
 * 
 */
public class CleanSvnFile
{
	private static final Logger LOGGER = Logger.getLogger(CleanSvnFile.class);
	/**
	 * 
	 */
	public CleanSvnFile(String strFilePath,boolean verbose)
	{
		// TODO Auto-generated constructor stub
		DoWork(strFilePath,verbose);
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
					
					//if(JpfStringUtil.IsChinese(tmpString))
					if(tmpString.indexOf(".svn")>0)
					{
						//LOGGER.info(tmpString);
						tmpString=tmpString.substring(0,tmpString.indexOf(".svn")+4);
						LOGGER.info(tmpString);
						
						FileUtil.DelDirWithFiles(tmpString);
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
	 * update 2012-8-14
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		CleanSvnFile cCleanSvnFile=new CleanSvnFile("D:\\conbuild\\clang",true);
	}

}
