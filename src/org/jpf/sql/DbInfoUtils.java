/**
 * 
 */
package org.jpf.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @author ��ƽ��
 *
 * @version ����ʱ�䣺2010-10-15 ����03:48:19
 */
public final class DbInfoUtils {
	/**
	 * ��ȡ�������
	 * @param conn
	 * @param TableName
	 * @return
	 * @throws Exception
	 */
	public static String GetTablePK(Connection conn,String TableName)throws Exception
	{
		StringBuffer sb=new StringBuffer();
		String strSql = "select * from user_cons_columns where constraint_name=(select constraint_name from user_constraints where table_name=upper('"+TableName+"') and constraint_type   ='P')";   
		
		ResultSet rs = DBUtil.ExecSqlQuery(conn, strSql);
		while (rs.next())
		{
			sb.append(rs.getString("COLUMN_NAME")).append(",");
		}
		rs.close();
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	/**
	 * ��ȡ�������
	 * 
	 * @return
	 */
	public static  String GetTableColumn(Connection conn,String TableName) throws Exception
	{
		StringBuffer sb = new StringBuffer();

			String strSql = "select * from " + TableName;
			
			ResultSet rs = DBUtil.ExecSqlQuery(conn, strSql);
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++)
			{
				sb.append(rsmd.getColumnName(i)).append(",");
			}
			rs.close();

		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}	
}
