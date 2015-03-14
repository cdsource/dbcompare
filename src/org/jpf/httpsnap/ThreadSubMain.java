/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2012-2-5 下午9:54:04 
 * 类说明 
 */

package org.wupf.httpsnap;

/**
 * 获取店铺的主线程
 */
public class ThreadSubMain extends Thread
{
	public ThreadSubMain()
	{

	}

	boolean IsRunning = true;

	public void run()
	{
		while (IsRunning)
		{

			try
			{
				DoWork();
			} catch (Exception ex)
			{
				// TODO: handle exception
			}
		}
	}
	private void DoWork()
	{
		try
		{
			
		} catch (Exception ex)
		{
			// TODO: handle exception
		}
	}
}
