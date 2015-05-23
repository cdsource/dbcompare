/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年1月15日 下午4:00:30 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDbUtils;
import org.jpf.visualwall.WallsDbConn;

public class CompareTable extends AbstractDbCompare
{
	private static final Logger logger = LogManager.getLogger();

	public void DoWork(Connection conn_pdm, Connection conn_develop) throws Exception
	{
		String strPdmDomain=strDomain;
		if (strDomain.startsWith("ud"))
		{
			strPdmDomain="ud";
		}
		// PDM数据库连接
		LinkedHashMap<String, Table> map_pdm = GetTables(conn_pdm,strPdmDomain);
		// 开发数据库连接

		LinkedHashMap<String, Table> map_develop = GetTables(conn_develop,strDomain);
		// 遍历开发库Map
		for (Iterator iter_table = map_develop.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();

			Table table_develop = map_develop.get(key_table);// 获得PDM中的表
			if (table_develop.getTable_type().equalsIgnoreCase("view"))
			{
				CompareUtil.append(table_develop, null, null, 2, sb);
				iCount2++;
				continue;
			}

			Table table_pdm = map_pdm.get(key_table);// 尝试从比对库中获得同名表
			if (table_pdm == null)
			{ // 如果获得表为空，说明PDM不存在，比对库存在
				CompareUtil.append(table_develop, null, null, 2, sb);
				iCount2++;

			} else
			{ // 表相同，判断字段、字段类型、字段长度
				for (Iterator iter_column = table_develop.columns.keySet().iterator(); iter_column.hasNext();)
				{
					String key_column = (String) iter_column.next();
					Column column_develop = (Column) table_develop.columns.get(key_column);// 获得开发库中的列
					Column column_pdm = (Column) table_pdm.columns.get(key_column);// 尝试从生产库中获得同名列
					if (column_pdm == null)
					{// 如果列名为空，说明PDM不存在，比对库存在
						CompareUtil.append(table_develop, column_develop, null, 4, sb);
						iCount4++;
					} else
					{// 说明两者都存在
						if (!column_develop.getDataType().equalsIgnoreCase(column_pdm.getDataType()))// 字段类型不一致
						{
							CompareUtil.append(table_develop, column_pdm, column_develop, 5, sb);
							iCount5++;
						}
						if (!column_develop.getNullable().equalsIgnoreCase(column_pdm.getNullable()))// 是否为空不一致
						{
							CompareUtil.append(table_develop, column_pdm, column_develop, 6, sb);
							iCount6++;
						}
						if (column_develop.getColumnDefault()==null && column_pdm.getColumnDefault()!=null)
						{
							CompareUtil.append(table_develop, column_pdm, column_develop, 7, sb);
							iCount7++;
						}
						if (column_develop.getColumnDefault()!=null && column_pdm.getColumnDefault()==null)
						{
							CompareUtil.append(table_develop, column_pdm, column_develop, 7, sb);
							iCount7++;
						}						
						if (column_develop.getColumnDefault() != null && column_pdm.getColumnDefault() != null)
						{
							if (!column_develop.getColumnDefault().equalsIgnoreCase(column_pdm.getColumnDefault()))// 是否为空不一致
							{
								CompareUtil.append(table_develop, column_pdm, column_develop, 7, sb);
								iCount7++;
							}
						}
						
						if (column_develop.getExtra()==null && column_pdm.getExtra()!=null)
						{
							CompareUtil.append(table_develop, column_pdm, column_develop, 8, sb);
							iCount8++;
						}
						if (column_develop.getExtra()!=null && column_pdm.getExtra()==null)
						{
							CompareUtil.append(table_develop, column_pdm, column_develop, 8, sb);
							iCount8++;
						}						
						if (column_develop.getExtra() != null && column_pdm.getExtra() != null)
						{
							if (!column_develop.getExtra().equalsIgnoreCase(column_pdm.getExtra()))// 是否为空不一致
							{
								CompareUtil.append(table_develop, column_pdm, column_develop, 8, sb);
								iCount8++;
							}
						}
					}
				}
			}
		}
		// 遍历生产库Map
		for (Iterator iter_table = map_pdm.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();

			Table table_develop = map_develop.get(key_table);// 获得PDM中的表
			Table table_pdm = map_pdm.get(key_table);// 尝试从比对库中获得同名表
			if (table_develop == null)
			{ // 如果获得表为空，说明PDM存在，比对库不存在
				CompareUtil.append(table_pdm, null, null, 1, sb);
				iCount1++;
			} else
			{ // 表相同，判断字段、字段类型、字段长度
				for (Iterator iter_column = table_pdm.columns.keySet().iterator(); iter_column.hasNext();)
				{
					String key_column = (String) iter_column.next();
					Column column_develop = (Column) table_develop.columns.get(key_column);// 获得开发库中的列
					Column column_pdm = (Column) table_pdm.columns.get(key_column);// 尝试从生产库中获得同名列
					if (column_develop == null)
					{// 如果列名为空，说明PDM存在，比对库不存在
						CompareUtil.append(table_develop, column_pdm, null, 3, sb);
						iCount3++;
					} else
					{// 说明两者都存在
						/*
						 * if (!column_pdm.getDataType().equalsIgnoreCase(
						 * column_develop.getDataType()))// 字段类型不一致 {
						 * CompareUtil.append(table_develop,column_pdm,
						 * column_develop, 5, sb); iCount5++; } if
						 * (!column_pdm.getNullable
						 * ().equalsIgnoreCase(column_develop.getNullable()))//
						 * 是否为空不一致 { CompareUtil.append(table_develop,
						 * column_pdm, column_develop,6, sb); iCount6++; }
						 */
					}
				}
			}
		}

	}

	private String GetColumnType(String inStr)
	{
		if (inStr.equalsIgnoreCase("decimal(1,0)") || inStr.equalsIgnoreCase("decimal(2,0)"))
		{
			return "tinyint(2)";
		}
		if (inStr.equalsIgnoreCase("decimal(3,0)"))
		{
			return "smallint(3)";
		}
		if (inStr.equalsIgnoreCase("decimal(4,0)"))
		{
			return "smallint(4)";
		}
		if (inStr.equalsIgnoreCase("decimal(5,0)"))
		{
			return "int(5)";
		}
		if (inStr.equalsIgnoreCase("decimal(6,0)"))
		{
			return "int(6)";
		}
		if (inStr.equalsIgnoreCase("decimal(7,0)"))
		{
			return "int(7)";
		}
		if (inStr.equalsIgnoreCase("decimal(8,0)"))
		{
			return "int(8)";
		}
		if (inStr.equalsIgnoreCase("decimal(9,0)"))
		{
			return "int(9)";
		}
		if (inStr.equalsIgnoreCase("decimal(10,0)"))
		{
			return "bigint(10)";
		}
		if (inStr.equalsIgnoreCase("decimal(11,0)"))
		{
			return "bigint(11)";
		}
		if (inStr.equalsIgnoreCase("decimal(12,0)"))
		{
			return "bigint(12)";
		}
		if (inStr.equalsIgnoreCase("decimal(13,0)"))
		{
			return "bigint(13)";
		}
		if (inStr.equalsIgnoreCase("decimal(14,0)"))
		{
			return "bigint(14)";
		}
		if (inStr.equalsIgnoreCase("decimal(15,0)"))
		{
			return "bigint(15)";
		}
		if (inStr.equalsIgnoreCase("decimal(16,0)"))
		{
			return "bigint(16)";
		}
		if (inStr.equalsIgnoreCase("decimal(17,0)"))
		{
			return "bigint(17)";
		}
		if (inStr.equalsIgnoreCase("decimal(18,0)"))
		{
			return "bigint(18)";
		}
		return inStr;
	}
    
	
	public LinkedHashMap<String, Table> GetTables(Connection transaction,String inDomain) throws Exception
	{
		String sSql = " select t1.TABLE_NAME,t1.COLUMN_NAME,t1.IS_NULLABLE,t1.COLUMN_TYPE,t1.Column_Default,t2.table_type,t1.extra "
				+ " from information_schema.COLUMNS t1, information_schema.tables t2 "
				+ " where t1.table_name=t2.table_name and t1.table_schema=t2.table_schema  and t1.table_schema=? and t1.table_name not REGEXP '[a-zA-Z]_[0-9]' ";
		if (strExcludeTable != null && strExcludeTable.length() > 0)
		{
			sSql += " and t1.table_name not like %'" + strExcludeTable + "%' ";
		}
		sSql += " order By t1.table_name,t1.column_name";
		PreparedStatement pstmt = transaction.prepareStatement(sSql);
		pstmt.setString(1, inDomain);
		logger.debug(sSql);
		System.out.println("Domain=" + inDomain);

		ResultSet rs = pstmt.executeQuery();
		LinkedHashMap<String, Table> map = new LinkedHashMap<String, Table>();
		String tableName = "";
		Table table = null;
		while (rs.next())
		{
			String strColumnType = rs.getString("COLUMN_TYPE").toLowerCase().trim();
			// strColumnType=GetColumnType(strColumnType);
			if (!tableName.equals(rs.getString("table_name").toLowerCase().trim()))
			{// һ���±�
				tableName = rs.getString("table_name").toLowerCase().trim();
				table = new Table(tableName, rs.getString("table_type").toLowerCase().trim());

				Column column = new Column(rs.getString("Column_Name").toLowerCase().trim(),
						strColumnType, rs.getString("IS_NULLABLE").toLowerCase().trim(),  CompareUtil.ShowDefaultValue( rs.getString("Column_Default")));
				column.setExtra(rs.getString("extra"));
				table.columns.put(column.getColumnName(), column);
				map.put(tableName, table);
			} else
			{// �Ѵ��ڵı������ֶ�
				Column column = new Column(rs.getString("Column_Name").toLowerCase().trim(),
						strColumnType, rs.getString("IS_NULLABLE").toLowerCase().trim(),CompareUtil.ShowDefaultValue( rs.getString("Column_Default")));
				column.setExtra(rs.getString("extra"));
				table.columns.put(column.getColumnName(), column);
			}
		}
		if (null != rs)
			rs.close();
		// transaction.finalize();
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jpf.ci.dbs.compare.AbstractDbCompare#GetHtmlName()
	 */
	@Override
	String GetHtmlName()
	{
		// TODO Auto-generated method stub
		return "compare_table.html";
	}

	protected void InsertResult(String strDbUrl)
	{
		Connection conn = null;
		try
		{
			conn = WallsDbConn.GetInstance().GetConn();
			String strSql = "delete from dbci where dbinfo='" + strDbUrl + "' and diffdate=current_date";
			logger.info(strSql);
			JpfDbUtils.ExecUpdateSql(conn, strSql);
			strSql = "insert into dbci(dbmail,dbinfo,diffdate,diff1,diff2,diff3,diff4,diff5,diff6,diff7,diff8,areaname) values( '"
					+ strMails + "','" + strDbUrl + "',CURRENT_DATE," + iCount1 + ","
					+ iCount2 + "," + iCount3 + "," + iCount4 + "," + iCount5 + "," + iCount6 +","+iCount7+","+iCount8+ ",'HZ')";
			logger.info(strSql);
			JpfDbUtils.ExecUpdateSql(conn, strSql);
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		} finally
		{
			JpfDbUtils.DoClear(conn);
		}
	}
	protected String ShowResult()
	{
		return "<tr><td>"+iCount1+"</td><td>"
				+iCount2+"</td><td>"
				+iCount3+"</td><td>"
				+iCount4+"</td><td>"
				+iCount5+"</td><td>"
				+iCount6+"</td><td>"
				+iCount7+"</td><td>"
				+iCount8+"</td></tr>";
	}

	/* (non-Javadoc)
	 * @see org.jpf.ci.dbs.compare.AbstractDbCompare#GetMailTitle()
	 */
	@Override
	String GetMailTitle()
	{
		// TODO Auto-generated method stub
		return "数据库比对结果(自动发出)";
	}
}
