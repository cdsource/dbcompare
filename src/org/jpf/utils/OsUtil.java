/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2014年5月30日 下午3:54:44 
* 类说明 
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
