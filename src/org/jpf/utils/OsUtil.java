/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2014��5��30�� ����3:54:44 
* ��˵�� 
*/ 

package org.jpf.utils;

import java.util.Properties;

/**
 * 
 */
public class OsUtil
{

	/**
	 * 
	 */
	public OsUtil()
	{
		// TODO Auto-generated constructor stub
	}
	public static boolean IsWindows()
	{
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		//System.out.println(os);
		if (os.trim().toLowerCase().startsWith("windows"))
		{
			return true;
		}
		return false;
	}
}
