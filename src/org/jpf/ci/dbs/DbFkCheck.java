/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2014年7月5日 下午12:53:35 
* 类说明 
*/ 

package org.jpf.ci.dbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDbUtils;

/**
 * 
 */
public class DbFkCheck
{
	private static final Logger logger = LogManager.getLogger(DbFkCheck.class);
	/**
	 * 
	 */
	public DbFkCheck()
	{
		// TODO Auto-generated constructor stub

	}

	/**
	 * @param args
	 * 被测试类名：TODO
	 * 被测试接口名:TODO
	 * 测试场景：TODO
	 * 前置参数：TODO
	 * 入参：
	 * 校验值：
	 * 测试备注：
	 * update 2014年7月5日
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		DbFkCheck cDbFkCheck=new DbFkCheck();
		cDbFkCheck.CheckSqlError("alter table BD.RS_SYS_COUNTRY_GROUP add constraint FK_COUNTRY_GROUP_RF_COUNTRY foreign key (COUNTRY_ID, TENANT_ID) references SD.SYS_COUNTRY (COUNTRY_ID, TENANT_ID) on delete restrict on update restrict;");
	}
	/**
	 * 
	 * @return
	 * 被测试类名：TODO
	 * 被测试接口名:TODO
	 * 测试场景：TODO
	 * 前置参数：TODO
	 * 入参：
	 * 校验值：
	 * 测试备注：
	 * update 2014年7月4日
	 */
	public String CheckSqlError(String _strSql)
	{
		if (_strSql==null || _strSql.length()<1)
		{
			return "";
		}
		_strSql=_strSql.toLowerCase().trim();
		if (!_strSql.startsWith("alter"))
		{
			return "";
		}
		String strReturn="";
		//alter table BD.RS_SYS_COUNTRY_GROUP add constraint FK_COUNTRY_GROUP_RF_COUNTRY foreign key (COUNTRY_ID, TENANT_ID) references SD.SYS_COUNTRY (COUNTRY_ID, TENANT_ID) on delete restrict on update restrict;
		String[] strCols=_strSql.split(" ");
		String strTableName1=strCols[2];
		String strTableName2="";
		
		for(int i=2;i<strCols.length;i++)
		{
			if (strCols[i].equalsIgnoreCase("references"))
			{
				strTableName2=strCols[i+1];
				break;
			}
		}
		String[] strCols2=_strSql.split("\\(");
		String strCol1=strCols2[1];
		int i=strCol1.indexOf(")");
		strCol1=strCol1.substring(0,i);
		
		String strCol2=strCols2[2];
		i=strCol2.indexOf(")");
		strCol2=strCol2.substring(0,i);
		
		logger.debug(strCol1);
		logger.debug(strCol2);
		
		strReturn="<br>"+GetColType(strTableName1,strCol1);
		strReturn+="<br>"+GetColType(strTableName2,strCol2);
		return strReturn;
	}
	private String GetColType(String _strTable,String _strCol)
	{
		StringBuffer strReturn=new StringBuffer();
		strReturn.append(FormatStrByLength( _strTable,30));
		Connection conn=null;
		try
		{
			conn=DbChangeConn.GetInstance().GetConn(1);
			String strSql="desc "+_strTable;
			PreparedStatement pstmt = conn.prepareStatement(strSql);
			
			ResultSet rSet= pstmt.executeQuery();
			String[] strCols=_strCol.trim().split(",");
			while(rSet.next())
			{
				for(int i=0;i<strCols.length;i++)
				{
					if (rSet.getString(1).toLowerCase().trim().equalsIgnoreCase(strCols[i].trim()))
					{
						strReturn.append(FormatStrByLength(rSet.getString(1),20).append(FormatStrByLength(rSet.getString(2),20)));
					}
				}
				//logger.debug(rSet.getString(1)+" "+rSet.getString(2));
			}
			 /*
			ResultSetMetaData rsmd = pstmt.getMetaData();
			for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
				logger.debug(rsmd.getColumnName(i)+ "  " +rsmd.getColumnTypeName(i)+"  "+rsmd.getColumnClassName(i));
				
			
			}
			*/
			logger.debug(strReturn.toString());
		} catch (Exception ex)
		{
			// TODO: handle exception
		}finally
		{
			JpfDbUtils.DoClear(conn);
		}
		return strReturn.toString();
	}
	private StringBuffer FormatStrByLength(String _strIn,int _iLength)
	{
		StringBuffer sb=new StringBuffer();
		sb.append(_strIn);
		while (sb.length()<_iLength)
		{
			sb.append(" ");
		}
		return sb;
	}
}
