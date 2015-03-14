/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2012-2-5 下午9:17:37 
 * 类说明 
 */

package org.wupf.httpsnap;

import org.apache.log4j.Logger;

/**
 * 
 */
public class ThreadSubNode extends Thread
{
	private static final Logger logger = Logger.getLogger(ThreadSubNode.class);

	public ThreadSubNode()
	{

	}

	public void run()
	{
		try
		{
			long sTime = System.currentTimeMillis();
			HttpInfo cHttpInfo=new HttpInfo();
			String strUrl="http://www.360buy.com/product/507187.html";
			String strHtml=cHttpInfo.GetHttpInfo(strUrl);
			System.out.println(strHtml);
			long eTime = System.currentTimeMillis();
			System.out.println(" 数据生成总用时(单位MS):" + (eTime - sTime));
		} catch (Exception ex)
		{
			// TODO: handle exception
			logger.error(ex);
		}
	}
	
	public static void main(String[] args)
	{
		ThreadSubNode cThreadSubNode=new ThreadSubNode();
		cThreadSubNode.start();
	}
}
