/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2012-1-16 下午2:07:04 
 * 类说明 
 */

package org.jpf.memcached;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.aiqcs.utils.db.DBManager;

/**
 * 
 */
public class MemCache
{
`
	Hashtable<String, String[]> hashtable = new Hashtable<String, String[]>();
	Hashtable<String, String[]> hashtable2 = new Hashtable<String, String[]>();
	int columnCount = 0;
	String[] columnName = null;

	/**
	 * 加载内容
	 * 
	 * @return
	 */
	public int startUp() throws Exception
	{
		Connection conn=null;
		try
		{
			hashtable.clear();
			/*
			conn=DBManager.getConnection();
			ResultSet rs = conn.createStatement().executeQuery(strSql);
			ResultSetMetaData metadata = rs.getMetaData();
			columnCount = metadata.getColumnCount();
			columnName = new String[columnCount];
			for (int i = 0; i < columnCount; i++)
			{
				columnName[i] = metadata.getColumnName(i + 1);
			}
			while (rs.next())
			{
				String[] result = new String[columnCount];
				for (int i = 0; i < columnCount; i++)
				{
					result[i] = rs.getString(i + 1);
				}
				hashtable.put(rs.getString(strKey), result);
			}
			rs.close();
			logger.debug(strName + ":加载记录数" + hashtable.size());	
			*/
			//
			CycleThread cCycleThread=new CycleThread();
			cCycleThread.start();
		} catch (Exception ex)
		{
			// TODO: handle exception
		    LOGGER.error(ex);
		}finally
		{
			doClose(conn);
		}
		return 0;
	}

	String strSql = "";
	int cycyeTime = 60;
	String strName = "";
	String strKey = "";

	long iHaveCount = 0;
	long iMissCount = 0;

	public void setKey(String strKey)
	{
		this.strKey = strKey;
	}

	public void setSql(String strSql)
	{
		this.strSql = strSql;
	}

	public void setRefreshTime(int CycyeTime)
	{
		this.cycyeTime = CycyeTime;
	}

	public void setName(String strName)
	{
		this.strName = strName;
	}
    /**
     * 
     * @return
     */
	public String getName()
	{
		return strName;
	}
    /**
     * 
     * @param conn
     */
	private void doClose(Connection conn)
	{
		try
		{
			if(conn!=null)
				conn.close();
		} catch (Exception ex)
		{
			// TODO: handle exception
		}
	}
	/**
	 * 
	 * @return
	 */
	public final int refreshCache()
	{
		return 0;
	}

	/**
	 * 
	 * @param strKey
	 * @return
	 * @throws Exception
	 */
	public String[][] getCacheWithHead(String strKey) throws Exception
	{
		String[][] result = new String[2][columnCount];
		result[0] = columnName;
		Object o = hashtable.get(strKey);
		if (o != null)
		{
			result[1] = (String[]) o;
			iHaveCount++;
		} else
		{
			iMissCount++;
			return null;
		}
		return result;
	}

	/**
	 * 
	 * @param strKey
	 * @return
	 * @throws Exception
	 */
	public String[] getCache(String strKey) throws Exception
	{
		String[] result = new String[columnCount];

		Object o = hashtable.get(strKey);
		if (o != null)
		{
			result = (String[]) o;
			iHaveCount++;
		} else
		{
			iMissCount++;
			return null;
		}
		return result;
	}

	public String[][] getAllCache() throws Exception
	{
		String[][] result = new String[hashtable.size()][columnCount];
		long start = System.currentTimeMillis();

		Enumeration<String[]> en = hashtable.elements();

		while (en.hasMoreElements())
		{
			Object key_num = en.nextElement();
			//System.out.print("DEBUG: " + key_num);
			//System.out.print(" = ");
			//System.out.println(hashtable.get(key_num));

		}

		long end = System.currentTimeMillis();

		LOGGER.debug("Enumeration elements costs " + (end - start) + " milliseconds");

		return result;
	}
    /**
     * 
     */
	class CycleThread extends Thread
	{
		/**
		 * 
		 */
		public CycleThread()
		{
		    LOGGER.debug(strName + "守护线程启动...");
		}
        /*
         * 
         */
		private void doWork()
		{
			Connection conn=null;
			try
			{
				conn=DBManager.getConnection();
				hashtable2.clear();
				ResultSet rs = conn.createStatement().executeQuery(strSql);
				ResultSetMetaData metadata = rs.getMetaData();
				columnCount = metadata.getColumnCount();
				columnName = new String[columnCount];
				for (int i = 0; i < columnCount; i++)
				{
					columnName[i] = metadata.getColumnName(i + 1);
				}
				while (rs.next())
				{
					String[] result = new String[columnCount];
					for (int i = 0; i < columnCount; i++)
					{
						result[i] = rs.getString(i + 1);
					}
					hashtable2.put(rs.getString(strKey), result);
				}
				rs.close();
				LOGGER.debug(strName + ":刷新加载记录数" + hashtable2.size());
				if(hashtable2.size()!=hashtable.size())
				{
					hashtable.clear();
					hashtable=hashtable2;
					hashtable2.clear();
					LOGGER.info(strName + ":更新缓存" );
				}
			} catch (Exception ex)
			{
				// TODO: handle exception
			}finally
			{
				doClose(conn);
			}
		}
       /**
        * 
        */
		public void run()
		{
			while (true)
			{
				try
				{
					doWork();
					sleep(cycyeTime * 1000);
					
				} catch (Exception ex)
				{
					// TODO: handle exception
				    LOGGER.error(ex);
				}

			}
		}
	}
}
