/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2012-4-4 ����9:42:38 
* ��˵�� 
*/ 

package org.jpf.memcached;

/**
 * 
 */
public interface JpfCacheInterface
{
	/*
	public final static String TABLE_NAME="OP_LOGIN";
	public final static String SQL_STRING= "select  op_id,op_login,op_name,op_mail,op_stat from op_login";
	public final static String KEYCOL_STRING="OP_ID";
	public final static int CYCLE_TIME=600;
	*/
	public String getTableName();

	public String getSqlStr();

	public String getKeyCol();

	public int getCycleTime();
}
