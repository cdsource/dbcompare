/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2010-11-22 ����01:39:10 
* ��˵�� 
*/ 

package org.jpf.frame.threads;

import java.util.*;

/**
 *
 * <p>Title: </p>
 * <p>Description: �̳߳�</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: asiainfo</p>
 * @author wupflove2003@hotmail.com
 * @version 1.0
 */
class ThreadPoolManager
{

  private int maxThread;
  public Vector<SimpleThread> vector;
  public void setMaxThread(int threadCount)
  {
    maxThread = threadCount;
    
  }

  public ThreadPoolManager(int threadCount)
  {
    setMaxThread(threadCount);
    System.out.println("Starting thread pool...");
    vector = new Vector<SimpleThread>();
    for (int i = 1; i <= threadCount; i++)
    {
      SimpleThread thread = new SimpleThread(i);
      vector.addElement(thread);
      thread.start();
    }
  }

  public void process(String argument)
  {
    int i;
    for (i = 0; i < vector.size(); i++)
    {
      SimpleThread currentThread = (SimpleThread) vector.elementAt(i);
      if (!currentThread.isRunning())
      {
        System.out.println("Thread " + (i + 1) + " is processing:" + argument);
        currentThread.setArgument(argument);
        currentThread.setRunning(true);
        return;
      }
    }
    if (i == vector.size())
    {
      System.out.println("pool is full, try in another time.");
    }
  }
} //end of class ThreadPoolManager
