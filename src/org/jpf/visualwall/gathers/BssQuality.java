/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2014��12��2�� ����10:13:39 
* ��˵�� 
*/ 

package org.jpf.visualwall.gathers;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDbUtils;

/**
 * 
 */
public class BssQuality
{
	private static final Logger logger =  LogManager.getLogger();
	/**
	 * 
	 */
	public BssQuality()
	{
		// TODO Auto-generated constructor stub
		DoWork();
	}
    
	private void DoWork()
	{
		Connection conn=null;
		try
		{
			QualityInfoHZ  cQualityInfoHZ=new QualityInfoHZ();
			QualityInfoFZ cQualityInfoFZ=new QualityInfoFZ();
			QualityInfoNJ cQualityInfoNJ=new QualityInfoNJ();
			QualityInfoBJ cQualityInfoBJ=new QualityInfoBJ();
			
			//conn=WallsDbConn.GetInstance().GetDestConn();
			//String strSql="select  * from bss_prj where branch_coverage is null;";
			//WallsDbConn.GetInstance().ExecUpdateSql(conn, strSql);
			/*
			String strSql="truncate table bss_rpt_bak";
			QualityDb.ExecUpdateSql(conn, strSql);
			
			strSql="insert into bss_rpt_bak select distinct * from bss_rpt";
			QualityDb.ExecUpdateSql(conn, strSql);
			
			strSql="truncate table bss_rpt";
			QualityDb.ExecUpdateSql(conn, strSql);
			
			strSql="insert into bss_rpt select * from bss_rpt_bak";
			QualityDb.ExecUpdateSql(conn, strSql);
            */
			logger.info("game over");
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			logger.error(ex);
		}finally
		{
			JpfDbUtils.DoClear(conn);
		}
	}
	/**
	 * @param args
	 * ������������TODO
	 * �����Խӿ���:TODO
	 * ���Գ�����TODO
	 * ǰ�ò�����TODO
	 * ��Σ�
	 * У��ֵ��
	 * ���Ա�ע��
	 * update 2014��12��2��
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		BssQuality cBssQuality=new BssQuality();
	}

}
