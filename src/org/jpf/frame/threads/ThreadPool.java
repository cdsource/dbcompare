/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2010-11-22 ����01:38:46 
* ��˵�� 
*/ 

package org.jpf.frame.threads;

import java.io.*;

/**
 *
 * <p>Title: </p>
 * <p>Description:�̳߳� </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: asiainfo</p>
 * @author wupflove2003@hotmail.com
 * @version 1.0
 */
public class ThreadPool
{
  public static void main(String[] args)
  {
    try
    {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      String s;
      ThreadPoolManager manager = new ThreadPoolManager(10);
      while ( (s = br.readLine()) != null)
      {
        manager.process(s);
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
