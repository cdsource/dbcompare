package org.jpf.threads;

/**
 * <p>Title: NGBOSS--WBASS</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: asiainfo</p>
 *
 * @author ��ƽ��
 * @version 4.0
 */
public class TimeTaskInfo
{
   public TimeTaskInfo()
   {
   }
   //����
   public String TaskName;
   //�߳�����
   public String TaskThreadName;
   //1 �� 2 ��
   public int TaskType;
   //����ʱ��
   public String TaskTime;
   //��ǰ����״̬
   public int iState;
   //ִ�н��
   public int iWorkResult;
   //���ִ��ʱ��
   public String sLastWorkDay;
   //����ִ�����
   public boolean bIsRun;

}
