/**
 * 
 */
package org.jpf.threads;

import org.apache.log4j.Logger;




/**
 * @author 吴平福
 *
 * @version 创建时间：2010-9-26 下午12:55:58
 */
public class PfThreadPool {
	private static final Logger logger = Logger.getLogger(PfThreadPool.class);
	private static PfThread[] cPfThreads;
	   
	public PfThreadPool()
	{
		
	}
	
	public static int CreatePool()
	{
		return 0;
	}
	
	   public static String showThreadAlive()
	   {
	      if (cPfThreads == null)
	      {
	         return "没有启动线程";
	      }
	      StringBuffer sb = new StringBuffer();
	      for (int i = 0; i < cPfThreads.length; i++)
	      {
	         if (isThreadAlive(cPfThreads[i].cAppThreadThread))
	         {
	            sb.append(cPfThreads[i].strThreadName).append(";");
	         }

	      }

	      return sb.toString();
	   }	
	   private static boolean isThreadAlive(AppThread cAppThread)
	   {
	      if (cAppThread != null && cAppThread.isAlive())
	      {
	         return true;
	      }
	      return false;
	   }	 
	   /**
	    * 返回线程数量
	    * @return int
	    */
	   public static int showThreadCount()
	   {
	      if (cPfThreads != null)
	      {
	         return cPfThreads.length;
	      }
	      return 0;
	   }	  
	   /**
	    * 返回正在运行的线程数量
	    * @return
	    */
	   public static int showThreadAliveCount()
	   {
	      int iCount = 0;
	      if (cPfThreads != null)
	      {
	         for (int i = 0; i < cPfThreads.length; i++)
	         {
	            if (isThreadAlive(cPfThreads[i].cAppThreadThread))
	            {
	               iCount++;
	            }

	         }
	      }
	      return iCount;

	   }	   
	   /**
	    * 启动线程，外部调用
	    * @param lCodeValue long
	    * @throws Exception
	    */
	   public static void startThread(long lCodeValue) throws Exception
	   {

	   	boolean bFind=false;
	      for (int i = 0; i < cPfThreads.length; i++)
	      {
	         if (lCodeValue == cPfThreads[i].lThreadId)
	         {
	         	if(cPfThreads[i].strThreadClassName==null || cPfThreads[i].strThreadClassName.equalsIgnoreCase(""))
	         	{
	         		bFind=true;
	         		break;
	         		
	         	}
	            if (!isThreadAlive(cPfThreads[i].cAppThreadThread))
	            {
	            	cPfThreads[i].cAppThreadThread = getThread(cPfThreads[i].strThreadClassName);
	            	cPfThreads[i].cAppThreadThread.start();
	               logger.info(cPfThreads[i].strThreadClassName + " 启动");
	            }
	            bFind=true;
	            break;

	         }

	      }
	      if(!bFind)
	      {
	         throw new Exception("无法找到对应线程，请重启服务");
	      }

	   }	   
	   /**
	    *
	    * @param strClassName String
	    * @return WbassThread
	    * @throws Exception
	    */
	   private static AppThread getThread(String strClassName) throws Exception
	   {

	      return null;
	   }	   
}
