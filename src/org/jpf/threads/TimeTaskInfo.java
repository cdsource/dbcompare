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
 * @author 吴平福
 * @version 4.0
 */
public class TimeTaskInfo
{
   public TimeTaskInfo()
   {
   }
   //别名
   public String TaskName;
   //线程名称
   public String TaskThreadName;
   //1 天 2 月
   public int TaskType;
   //启动时间
   public String TaskTime;
   //当前工作状态
   public int iState;
   //执行结果
   public int iWorkResult;
   //最后执行时间
   public String sLastWorkDay;
   //当天执行情况
   public boolean bIsRun;

}
