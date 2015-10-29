/** 
* @author 吴平福 
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年5月29日 下午7:00:58 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

import java.sql.Connection;

import org.jpf.utils.JpfDbUtils;

/**
 * 
 */
public class CompareSelfCheck
{

	/**
	 * @todo: 检查数据库是否存在并创建
	 */
<<<<<<< HEAD
	public static void checkSaveSqlDb(Connection conn_develop)
=======
	public static  void CheckSaveSqlDb(Connection conn_develop)
>>>>>>> origin/master
	{
		try
		{
			String strSql = "CREATE DATABASE IF NOT EXISTS dd default charset utf8 COLLATE utf8_general_ci;";
<<<<<<< HEAD
			JpfDbUtils.execUpdateSql(conn_develop, strSql);
			// strSql = "drop table  IF EXISTS dd.dbchangehis;";
			// JpfDbUtils.ExecUpdateSql(conn_develop, strSql);
			strSql = "CREATE TABLE if not exists dd.dbchangehis (table_name  VARCHAR(128), exectype smallint,domain VARCHAR(45), changesql text NOT NULL,state_date DATETIME NOT NULL,reversesql VARCHAR(45),execmsg VARCHAR(512));";
			JpfDbUtils.execUpdateSql(conn_develop, strSql);
=======
			JpfDbUtils.ExecUpdateSql(conn_develop, strSql);
			// strSql = "drop table  IF EXISTS dd.dbchangehis;";
			// JpfDbUtils.ExecUpdateSql(conn_develop, strSql);
			strSql = "CREATE TABLE if not exists dd.dbchangehis (table_name  VARCHAR(128), exectype smallint,domain VARCHAR(45), changesql text NOT NULL,state_date DATETIME NOT NULL,reversesql VARCHAR(45),execmsg VARCHAR(512));";
			JpfDbUtils.ExecUpdateSql(conn_develop, strSql);
>>>>>>> origin/master

		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
	}
}
