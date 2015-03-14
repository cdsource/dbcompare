/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2012-1-16 下午2:03:20 
* 类说明 
*/ 

package org.jpf.memcached;

import org.apache.log4j.Logger;


import java.sql.Connection;
import java.util.Vector;



/**
 * 
 */
public class MemCacheMgr
{
	private static final Logger LOGGER = Logger.getLogger(MemCacheMgr.class);
	
	private static Vector<MemCache> v=new Vector<MemCache>();
	public MemCacheMgr()
	{
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static int addCache(String strSql,int iCycleTime,String strTableName,String strKey)
	{
		Connection conn=null;
		try
		{

		    MemCache cMcache=new MemCache();
			cMcache.setSql(strSql);
			cMcache.setRefreshTime(iCycleTime);
			cMcache.setName(strTableName);
			cMcache.setKey(strKey);
			cMcache.startUp();
			v.add(cMcache);			
		} catch (Exception ex)
		{
			// TODO: handle exception
		    LOGGER.error(ex);
			return -1;
		}finally
		{
			try
			{
				if (conn!=null)
					conn.close();
			} catch (Exception ex2)
			{
				// TODO: handle exception
			}
		}

		return 0;
	}
	
	public static int addCache(JpfCacheInterface cJpfCacheInterface)
	{
		
		return addCache(cJpfCacheInterface.getSqlStr(),cJpfCacheInterface.getCycleTime(),cJpfCacheInterface.getTableName(),cJpfCacheInterface.getKeyCol());
	}	
	/**
	 * 
	 * @param strName
	 * @param strKey
	 * @return
	 * @throws Exception
	 */
	public static String[][] getCacheWithHead(String strName,String strKey)throws Exception
	{
		String[][] result=null;
		for(int i=0;i<v.size();i++)
		{
		    MemCache cMcache=v.get(i);
			if (cMcache.getName().equalsIgnoreCase(strName))
			{
				result=cMcache.getCacheWithHead(strKey);
				if(result==null)
				{
				    LOGGER.debug(strName+":"+strKey+" miss find data");
				}
			}
		}
		
		return result;
	}
	/**
	 * 
	 * @param strName
	 * @param strKey
	 * @return
	 * @throws Exception
	 */
	public static String[] getCache(String strName,String strKey)throws Exception
	{
		String[] result=null;
		for(int i=0;i<v.size();i++)
		{
		    MemCache cMcache=v.get(i);
			if (cMcache.getName().equalsIgnoreCase(strName))
			{
				result=cMcache.getCache(strKey);
				if(result==null)
				{
				    LOGGER.debug(strName+":"+strKey+" miss find data");
				}
			}
		}
		
		return result;
	}	
	
	/**
	 * 
	 * @param strName
	 * @return
	 * @throws Exception
	 */
	public static String[][] getAllCache(String strName)throws Exception
	{
		String[][] result=null;
		for(int i=0;i<v.size();i++)
		{
		    MemCache cMcache=v.get(i);
			if (cMcache.getName().equalsIgnoreCase(strName))
			{
				result=cMcache.getAllCache();
				if(result==null)
				{
				    LOGGER.debug(strName+": miss find data");
				}
			}
		}		
		return result;		
	}
}
