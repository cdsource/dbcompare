/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2011-10-26 下午10:37:04 
 * 类说明 
 */

package org.jpf.memcached;

import org.apache.log4j.Logger;

/**
 * 
 */
public class JpfCacheUtil
{
	private static final Logger LOGGER = Logger.getLogger(JpfCacheUtil.class);

	/**
	 * 
	 * @param TABLE_NAME
	 * @param strKeyValue
	 * @param strColumnName
	 * @return
	 */
	public static long getCacheInfoAsLong(String TABLE_NAME, String strKeyValue, String strColumnName)
	{
		String tmpStr = getCacheInfoAsString(TABLE_NAME, strKeyValue, strColumnName);
		if (tmpStr != null && !tmpStr.equalsIgnoreCase(""))
		{
			return Long.parseLong(tmpStr);
		}
		return -1;
	}

	/**
	 * 
	 * @param TABLE_NAME
	 * @param strKeyValue
	 * @param strColumnName
	 * @return
	 */
	public static String getCacheInfoAsString(String TABLE_NAME, String strKeyValue, String strColumnName)
	{
		try
		{

			String[][] result = MemCacheMgr.getCacheWithHead(TABLE_NAME, strKeyValue);
			int i = 0;
			for (i = 0; i < result[0].length; i++)
			{
				if (result[0][i].equalsIgnoreCase(strColumnName))
				{
					break;
				}
			}
			return result[1][i];
		} catch (Exception ex)
		{
			// TODO: handle exception
		    LOGGER.error(ex);

		}
		return null;
	}

	/**
	 * 
	 * @param TABLE_NAME
	 * @param strKeyValue
	 * @param iPos
	 * @return
	 */
	public static String getCacheInfoAsString(String TABLE_NAME, String strKeyValue, int iPos)
	{
		try
		{
			String[][] result = MemCacheMgr.getCacheWithHead(TABLE_NAME, strKeyValue);
			return result[1][iPos];
		} catch (Exception ex)
		{
			// TODO: handle exception
		    LOGGER.error(ex);
		}
		return null;
	}

	public static String[][] getCacheList(String TABLE_NAME) throws Exception
	{
		return MemCacheMgr.getAllCache(TABLE_NAME);
	}
}
