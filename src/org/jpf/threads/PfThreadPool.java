/**
 * 
 */
package org.jpf.threads;

import org.apache.log4j.Logger;




/**
 * @author ��ƽ��
 *
 * @version ����ʱ�䣺2010-9-26 ����12:55:58
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
	         return "û�������߳�";
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
	    * �����߳�����
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
	    * �����������е��߳�����
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
	    * �����̣߳��ⲿ����
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
	               logger.info(cPfThreads[i].strThreadClassName + " ����");
	            }
	            bFind=true;
	            break;

	         }

	      }
	      if(!bFind)
	      {
	         throw new Exception("�޷��ҵ���Ӧ�̣߳�����������");
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
