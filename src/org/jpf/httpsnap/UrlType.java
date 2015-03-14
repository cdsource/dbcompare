/** 
* @author 鍚村钩绂�
* E-mail:wupf@asiainfo-linkage.com 
* @version 鍒涘缓鏃堕棿锛�012-2-6 涓婂崍10:43:20 
* 绫昏鏄�
*/ 

package org.wupf.httpsnap;

import java.sql.Timestamp;

/**
 * 
 */
public class UrlType
{
	String strUrl="";
	int iState=0;
	public static void main(String[] args)
	{
		UrlType cUrlType=new UrlType();
		String tsStr = "2011-05-09 11:49:45";  
		Timestamp ts = new Timestamp(System.currentTimeMillis()); 
        try {  
           ts = Timestamp.valueOf(tsStr);  
            System.out.println(ts);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
	}
	
	public UrlType()
	{
		StringBuffer sBuffer=new StringBuffer();
		a(sBuffer);
		System.out.println(sBuffer.toString());
	}
	
	private void a(StringBuffer sBuffer)
	{
		sBuffer.append("b");
	}
}

