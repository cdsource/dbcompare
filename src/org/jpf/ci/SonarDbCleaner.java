/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2013-4-30 ����6:17:16 
* ��˵�� 
*/ 

package org.jpf.ci;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDbUtils;



/**
 * 
 */
public class SonarDbCleaner
{
	int iCount=30;
	private static final Logger logger = LogManager.getLogger(SonarDbCleaner.class);
	/**
	 * @todo:show action
	 */
	public SonarDbCleaner()
	{

			try
			{
				this.iCount=4;
				DoCheck();
				System.out.println("game over");
			} catch (Exception ex)
			{
				// TODO: handle exception
				logger.error(ex);
			}
	
	}
	/**
	 * @todo:delete action
	 */
	public SonarDbCleaner(String strCount)
	{
		if (strCount!=null && !strCount.equalsIgnoreCase(""))
		{
			try
			{
				this.iCount=Integer.parseInt(strCount);
				DoDel();
			} catch (Exception ex)
			{
				// TODO: handle exception
				logger.error(ex);
			}
		}else {
			logger.error("��������룺����������");
		}
	}
	private void DoCheck()
	{
		// ɨ�豾��Ŀ¼�������ݿ��л�ȡ��¼
		Connection conn = null;
		try
		{
			String strSql = "select count(*)count,project_id,t2.long_name from snapshots t1,projects t2 where t1.project_id=t2.id"
					+" and t1.scope='FIL'  group by t1.project_id,t2.long_name"
					+" having count(*)>"+iCount
					+" order by count desc ";
			logger.debug("strSql=" + strSql);
			
			conn = AppConn.GetInstance().GetConn();
			PreparedStatement pStmt = conn.prepareStatement(strSql);
			ResultSet rs = pStmt.executeQuery();
		
			int iRowCount = 0;
			while (rs.next())
			{
				logger.debug("current prj:" + rs.getString("long_name"));
				strSql="select * from snapshot_sources where exists (select * from (select snapshot_sources.id from snapshots,snapshot_sources"
						+" where snapshots.id=snapshot_sources.snapshot_id AND snapshots.scope='FIL'"
						+" and snapshots.project_id="+rs.getInt("project_id")
						+" order by id desc limit "+iCount
						+","+rs.getInt("count")
						+" ) as a where a.id=snapshot_sources.id)";
				logger.debug("strSql=" + strSql);
				PreparedStatement pStmt2 = conn.prepareStatement(strSql);
				ResultSet rs2 = pStmt2.executeQuery();
				while (rs2.next())
				{
					System.out.println("׼��ɾ��snapshot_ID="+rs2.getInt("snapshot_id"));
				}
				rs2.close();
				//pStmt2.close();
				strSql="select * from measure_data where exists (select * from (select measure_data.id from snapshots,measure_data"
						+" where snapshots.id=measure_data.snapshot_id AND snapshots.scope='FIL'"
						+" and snapshots.project_id="+rs.getInt("project_id")
						+" order by id desc limit "+iCount
						+","+rs.getInt("count")
						+" ) as a where a.id=measure_data.id)";
				logger.debug("strSql=" + strSql);
				
				rs2 = pStmt2.executeQuery();
				while (rs2.next())
				{
					System.out.println("׼��ɾ��snapshot_ID="+rs2.getInt("snapshot_id"));
				}
				rs2.close();
				pStmt2.close();
				iRowCount++;
				
			}
			rs.close();
			pStmt.close();
			// logger.debug("д���ļ����,��д��������" + iCount);
			// putfile.delete();
		} catch (Exception ex)
		{
			logger.error(ex);
			ex.printStackTrace();
		} finally
		{
			JpfDbUtils.DoClear(conn);
		}		
	}
	
	private void DoDel()
	{
		// ɨ�豾��Ŀ¼�������ݿ��л�ȡ��¼
		Connection conn = null;
		try
		{
			String strSql = "select count(*)count,project_id,t2.long_name from snapshots t1,projects t2 where t1.project_id=t2.id"
					+" and t1.scope='FIL'  group by t1.project_id,t2.long_name"
					+" having count(*)>"+iCount
					+" order by count desc ";
			logger.debug("strSql=" + strSql);
			
			conn = AppConn.GetInstance().GetConn();
			PreparedStatement pStmt = conn.prepareStatement(strSql);
			ResultSet rs = pStmt.executeQuery();
			logger.debug("strSql=" + strSql);
			int iRowCount = 0;
			while (rs.next())
			{
				logger.debug("current prj:" + rs.getString("long_name"));
				strSql="select * from snapshot_sources where exists (select * from (select snapshot_sources.id from snapshots,snapshot_sources"
						+" where snapshots.id=snapshot_sources.snapshot_id AND snapshots.scope='FIL'"
						+" and snapshots.project_id="+rs.getInt("project_id")
						+" order by id desc limit "+iCount
						+","+rs.getInt("count")
						+")  as a where a.id=snapshot_sources.id)";
				logger.debug("strSql=" + strSql);
				PreparedStatement pStmt2 = conn.prepareStatement(strSql);
				ResultSet rs2 = pStmt2.executeQuery();
				while (rs2.next())
				{
					System.out.println(rs2.getInt("id")+";"+rs2.getInt("snapshot_id"));
				}
				rs2.close();
				pStmt2.close();
				iRowCount++;
				
			}
			rs.close();
			pStmt.close();
			// logger.debug("д���ļ����,��д��������" + iCount);
			// putfile.delete();
		} catch (Exception ex)
		{
			logger.error(ex);
			ex.printStackTrace();
		} finally
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
	 * update 2013-4-30
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		if(1==args.length)
		{
			SonarDbCleaner cSonarDbCleaner=new SonarDbCleaner(args[0]);
		}
		if(0==args.length)
		{
			SonarDbCleaner cSonarDbCleaner=new SonarDbCleaner();
		}
	}

}
