/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年2月14日 上午1:26:12 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDbUtils;
import org.jpf.visualwall.WallsDbConn;

/**
 * 
 */
public class CompareSubTables extends AbstractDbCompare
{
	private static final Logger logger = LogManager.getLogger();

	private void CheckSubTables(LinkedHashMap<String, Table> map_dev)
	{

		for (Iterator iter_table = map_dev.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();

			if (!CompareUtil.IsSubTable(key_table))
			{
				// 分表
				// logger.info(key_table);
				iter_table.remove();
			}
		}

	}

	//用于PDM与PDM比对
	public void  DoWork( CompareInfo cCompareInfo) throws Exception
	{
		// PDM数据库连接
		String strPdmDomain = cCompareInfo.getDbDomain();
		TreeMap<String ,Table> map_pdm=new TreeMap<String,Table>();
		TreeMap<String ,Table> map_develop=new TreeMap<String,Table>();
		if(domain_table.containsKey(strPdmDomain))
		{
			map_pdm=domain_table.get(strPdmDomain);
		}
		 if(domain_table2.containsKey(strPdmDomain))
		    {
		    	map_develop=domain_table2.get(strPdmDomain);
		    }

	//	LinkedHashMap<String, Table> map_develop = GetTables(conn_develop, cCompareInfo.getDbDomain(), cCompareInfo);
		// logger.info(map_develop.size());
		// CheckSubTables(map_develop);
		// logger.info(map_develop.size());

		// 遍历开发库Map
		logger.info("begin compare tables... ");
		for (Iterator iter_table = map_develop.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();

			Table table_develop = map_develop.get(key_table);// 获得PDM中的表

			if (table_develop.getTable_type().equalsIgnoreCase("view"))
			{
				CompareUtil.append(null, table_develop, null, null, 2, sb, vSql);
				iCount2++;
				continue;
			}
			logger.debug("key_table=" + key_table);
			key_table = CompareUtil.GetParentTableName(key_table);
			logger.debug("parent_table=" + key_table);
			Table table_pdm = map_pdm.get(key_table);// 尝试从比对库中获得同名表
			if (table_pdm == null)
			{ // 如果获得表为空，说明PDM不存在，比对库存在
				CompareUtil.append(table_pdm, table_develop, null, null, 2, sb, vSql);
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
						CompareUtil.append(table_pdm, table_develop, column_develop, null, 4, sb, vSql);
						iCount4++;
					} else
					{// 说明两者都存在
						/*
						 * if (!column_develop.getDataType().equalsIgnoreCase(
						 * column_pdm.getDataType()))// 字段类型不一致 {
						 * CompareUtil.append(table_develop, column_pdm,
						 * column_develop, 5, sb); iCount5++; } if
						 * (!column_develop
						 * .getNullable().equalsIgnoreCase(column_pdm
						 * .getNullable()))// 是否为空不一致 {
						 * CompareUtil.append(table_develop, column_pdm,
						 * column_develop, 6, sb); iCount6++; }
						 */
					}
				}
			}
		}
		// 遍历生产库Map
		for (Iterator iter_table = map_pdm.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();

			Table table_pdm = map_pdm.get(key_table);// 尝试从比对库中获得同名表
			Table table_develop = map_develop.get(key_table);// 获得PDM中的表
			if (table_develop == null)
			{ // 如果获得表为空，说明PDM存在，比对库不存在
			
				//String mStrSql=CreateTables.GetCreateTableSql(conn_pdm, strPdmDomain,key_table);rin
				String mStrSql=CreateTableSql(strPdmDomain,table_pdm);
				ExecSqlInfo cExecSqlInfo=new ExecSqlInfo();
				cExecSqlInfo.setiType(1);
				cExecSqlInfo.setStrSql(mStrSql);
				cExecSqlInfo.setStrTable(key_table);
				vSql.add(cExecSqlInfo);
				CompareUtil.append(table_pdm, table_develop, null, null, 1, sb, vSql);
				
				iCount1++;
			} else
			{
				CompareFromPdm(table_pdm, table_develop);
			}

			for (Iterator iter_table2 = map_develop.keySet().iterator(); iter_table2.hasNext();)
			{
				String key_table2 = (String) iter_table2.next();

				if (CompareUtil.IsSubTable(key_table, key_table2))
				{
					// 进入分表
					table_develop = map_develop.get(key_table2);
					// 比较
					CompareFromPdm(table_pdm, table_develop);
				}
			}

		}
		map_pdm=null;
		map_develop=null;
		   
}
	public String  CreateTableSql(String strPdmDomain,Table table_pdm)
	{
		StringBuffer msql  = new StringBuffer();
		msql.append("create table "+strPdmDomain+"."+table_pdm.getTableName()+"(");
		msql.append("\r\n");
		for (Iterator iter_column = table_pdm.columns.keySet().iterator(); iter_column.hasNext();)
		{
			
			String key_column = (String) iter_column.next();
			Column column_pdm = (Column) table_pdm.columns.get(key_column);
			String isnull=column_pdm.getNullable();
			String columnDefault=column_pdm.getColumnDefault();
			if(isnull.equalsIgnoreCase("no"))
			{
				isnull="not null";
			}
			else
			{
				isnull="";
			}
			if(columnDefault==null)
			{
				columnDefault="";
			}
			if(columnDefault!="")
			{
				if(isNumberic(column_pdm.getDataType(),columnDefault))
				{
					columnDefault="default "+columnDefault;
				}
				else
				{
					columnDefault="default "+'\''+columnDefault+'\'';
				}
			}
			
			msql.append(column_pdm.getColumnName()+"  "+column_pdm.getDataType()+
					" "+column_pdm.getExtra()+"  "+isnull+
					" "+columnDefault+" ,");
			msql.append("\r\n");
		}
		String msqlt=msql.toString().substring(0,msql.lastIndexOf(","));
		msqlt+="\r\n"+")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		return msqlt;
	}
	 //判断一个字符串是否数字
	 public  boolean  isNumberic(String Type,String defaultType)
	    {
		 	String[] types=Type.split("\\(");
		 	String dataType=types[0];
	    	dataType=dataType.trim();
	    	 if (dataType.equalsIgnoreCase("numeric") || dataType.equalsIgnoreCase("int") || dataType .equalsIgnoreCase("tinyint") 
	                 || dataType.equalsIgnoreCase("smallint") || dataType.equalsIgnoreCase("bigint")
	                 || dataType.equalsIgnoreCase("decimal") || dataType.equalsIgnoreCase( "number") || defaultType.equalsIgnoreCase("null")
	                 || (dataType.equalsIgnoreCase( "date") && defaultType != "0000-00-00 00:00:00")
	                 || (dataType.equalsIgnoreCase( "datetime") && defaultType !="0000-00-00 00:00:00")
	                 || (dataType.equalsIgnoreCase("timestamp") && defaultType !="0000-00-00 00:00:00")
	                 || (defaultType.equalsIgnoreCase("0")))
	             {
	                 return true;
	             } 
	             else
	                 return false;
	    }
	 //用于PDM与数据库比对
	public void  DoWork(Connection conn_develop, CompareInfo cCompareInfo) throws Exception
	{
		// PDM数据库连接
		String strPdmDomain = cCompareInfo.getDbDomain();
		if (strPdmDomain.startsWith("ud"))
		{
			strPdmDomain = "ud";
		}
		if (strPdmDomain.startsWith("id")) {
			strPdmDomain = "id";
		}		
		if (strPdmDomain.startsWith("zd")) {
			strPdmDomain = "zd";
		}
		if (strPdmDomain.startsWith("md")) {
			strPdmDomain = "md";
		}
		TreeMap<String ,Table> map_pdm=new TreeMap<String,Table>();
		if(domain_table.containsKey(strPdmDomain))
		{
			map_pdm=domain_table.get(strPdmDomain);
			
		}
		LinkedHashMap<String, Table> map_develop = GetTables(conn_develop, cCompareInfo.getDbDomain(), cCompareInfo);
		// logger.info(map_develop.size());
		// CheckSubTables(map_develop);
		// logger.info(map_develop.size());

		// 遍历开发库Map
		logger.info("begin compare tables... ");
		for (Iterator iter_table = map_develop.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();

			Table table_develop = map_develop.get(key_table);// 获得PDM中的表

			if (table_develop.getTable_type().equalsIgnoreCase("view"))
			{
				CompareUtil.append(null, table_develop, null, null, 2, sb, vSql);
				iCount2++;
				continue;
			}
			logger.debug("key_table=" + key_table);
			key_table = CompareUtil.GetParentTableName(key_table);
			logger.debug("parent_table=" + key_table);
			Table table_pdm = map_pdm.get(key_table);// 尝试从比对库中获得同名表
			if (table_pdm == null)
			{ // 如果获得表为空，说明PDM不存在，比对库存在
				CompareUtil.append(table_pdm, table_develop, null, null, 2, sb, vSql);
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
						CompareUtil.append(table_pdm, table_develop, column_develop, null, 4, sb, vSql);
						iCount4++;
					} else
					{// 说明两者都存在
						/*
						 * if (!column_develop.getDataType().equalsIgnoreCase(
						 * column_pdm.getDataType()))// 字段类型不一致 {
						 * CompareUtil.append(table_develop, column_pdm,
						 * column_develop, 5, sb); iCount5++; } if
						 * (!column_develop
						 * .getNullable().equalsIgnoreCase(column_pdm
						 * .getNullable()))// 是否为空不一致 {
						 * CompareUtil.append(table_develop, column_pdm,
						 * column_develop, 6, sb); iCount6++; }
						 */
					}
				}
			}
		}
		// 遍历生产库Map
		for (Iterator iter_table = map_pdm.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();

			Table table_pdm = map_pdm.get(key_table);// 尝试从比对库中获得同名表
			Table table_develop = map_develop.get(key_table);// 获得PDM中的表
			if (table_develop == null)
			{ // 如果获得表为空，说明PDM存在，比对库不存在			
				//String mStrSql=CreateTables.GetCreateTableSql(conn_pdm, strPdmDomain,key_table);rin
				String mStrSql=CreateTableSql(strPdmDomain,table_pdm);
				ExecSqlInfo cExecSqlInfo=new ExecSqlInfo();
				cExecSqlInfo.setiType(1);
				cExecSqlInfo.setStrSql(mStrSql);
				cExecSqlInfo.setStrTable(key_table);
				vSql.add(cExecSqlInfo);
				CompareUtil.append(table_pdm, table_develop, null, null, 1, sb, vSql);
				
				iCount1++;
			} else
			{
				CompareFromPdm(table_pdm, table_develop);
			}

			for (Iterator iter_table2 = map_develop.keySet().iterator(); iter_table2.hasNext();)
			{
				String key_table2 = (String) iter_table2.next();

				if (CompareUtil.IsSubTable(key_table, key_table2))
				{
					// 进入分表
					table_develop = map_develop.get(key_table2);
					// 比较
					CompareFromPdm(table_pdm, table_develop);
				}
			}

		}
		map_pdm=null;
		map_develop=null;
	}
	//数据库与数据库比对
	public void DoWork(Connection conn_pdm, Connection conn_develop, CompareInfo cCompareInfo) throws Exception
	{

		// PDM数据库连接
		String strPdmDomain = cCompareInfo.getDbDomain();
		if (strPdmDomain.startsWith("ud"))
		{
			strPdmDomain = "ud";
		}
		LinkedHashMap<String, Table> map_pdm = GetTables(conn_pdm, strPdmDomain, cCompareInfo);
		// 开发数据库连接

		LinkedHashMap<String, Table> map_develop = GetTables(conn_develop, cCompareInfo.getDbDomain(), cCompareInfo);
		// logger.info(map_develop.size());
		// CheckSubTables(map_develop);
		// logger.info(map_develop.size());

		// 遍历开发库Map
		logger.info("begin compare tables... ");
		for (Iterator iter_table = map_develop.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();

			Table table_develop = map_develop.get(key_table);// 获得PDM中的表

			if (table_develop.getTable_type().equalsIgnoreCase("view"))
			{
				CompareUtil.append(null, table_develop, null, null, 2, sb, vSql);
				iCount2++;
				continue;
			}
			logger.debug("key_table=" + key_table);
			key_table = CompareUtil.GetParentTableName(key_table);
			logger.debug("parent_table=" + key_table);
			Table table_pdm = map_pdm.get(key_table);// 尝试从比对库中获得同名表
			if (table_pdm == null)
			{ // 如果获得表为空，说明PDM不存在，比对库存在
				CompareUtil.append(table_pdm, table_develop, null, null, 2, sb, vSql);
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
						CompareUtil.append(table_pdm, table_develop, column_develop, null, 4, sb, vSql);
						iCount4++;
					} else
					{// 说明两者都存在
						/*
						 * if (!column_develop.getDataType().equalsIgnoreCase(
						 * column_pdm.getDataType()))// 字段类型不一致 {
						 * CompareUtil.append(table_develop, column_pdm,
						 * column_develop, 5, sb); iCount5++; } if
						 * (!column_develop
						 * .getNullable().equalsIgnoreCase(column_pdm
						 * .getNullable()))// 是否为空不一致 {
						 * CompareUtil.append(table_develop, column_pdm,
						 * column_develop, 6, sb); iCount6++; }
						 */
					}
				}
			}
		}
		// 遍历生产库Map
		for (Iterator iter_table = map_pdm.keySet().iterator(); iter_table.hasNext();)
		{
			String key_table = (String) iter_table.next();

			Table table_pdm = map_pdm.get(key_table);// 尝试从比对库中获得同名表
			Table table_develop = map_develop.get(key_table);// 获得PDM中的表
			if (table_develop == null)
			{ // 如果获得表为空，说明PDM存在，比对库不存在
				String mStrSql=CreateTables.GetCreateTableSql(conn_pdm, strPdmDomain,key_table);
				ExecSqlInfo cExecSqlInfo=new ExecSqlInfo();
				cExecSqlInfo.setiType(1);
				cExecSqlInfo.setStrSql(mStrSql);
				cExecSqlInfo.setStrTable(key_table);
				vSql.add(cExecSqlInfo);
				CompareUtil.append(table_pdm, table_develop, null, null, 1, sb, vSql);
				
				iCount1++;
			} else
			{
				CompareFromPdm(table_pdm, table_develop);
			}

			for (Iterator iter_table2 = map_develop.keySet().iterator(); iter_table2.hasNext();)
			{
				String key_table2 = (String) iter_table2.next();

				if (CompareUtil.IsSubTable(key_table, key_table2))
				{
					// 进入分表
					table_develop = map_develop.get(key_table2);
					// 比较
					CompareFromPdm(table_pdm, table_develop);
				}
			}

		}
	}

	private void CompareFromPdm(Table table_pdm, Table table_develop) throws Exception
	{

		// 表相同，判断字段、字段类型、字段长度
		for (Iterator iter_column = table_pdm.columns.keySet().iterator(); iter_column.hasNext();)
		{
			String key_column = (String) iter_column.next();
			Column column_develop = (Column) table_develop.columns.get(key_column);// 获得开发库中的列
			Column column_pdm = (Column) table_pdm.columns.get(key_column);// 尝试从生产库中获得同名列
			if (column_develop == null)
			{// 如果列名为空，说明PDM存在，比对库不存在
				CompareUtil.append(table_pdm, table_develop, column_pdm, null, 3, sb, vSql);
				iCount3++;
			} else
			{// 说明两者都存在
				if (!column_pdm.getDataType().equalsIgnoreCase(column_develop.getDataType()))// 字段类型不一致
				{
					//判断pdm中是否存在没有指明数据长度的数据类型
					if((column_pdm.getDataLength()==0)&&((column_pdm.getDataType().contains("int"))||
							(column_pdm.getDataType().contains("decimal"))||
							(column_pdm.getDataType().contains("number"))||
							(column_pdm.getDataType().contains("char"))))
					{
						logger.error("PDM have error datatype: "+column_pdm.getDataType());
					}
					else
					{
						//数据类型为decimal或number数据类型，但是精度为零，统一去掉精度
						String pdmType=column_pdm.getDataType();
						String developType=column_develop.getDataType();
						 if((developType.contains("decimal"))||(developType.contains("number")))
							{
		
								if(developType.contains(","))
								{
									developType=developType.replaceAll(",0\\)", ")");
								}
							}
						 if((pdmType.contains("decimal"))||(pdmType.contains("number")))
							{
		
								if(pdmType.contains(","))
								{
									pdmType=pdmType.replaceAll(",0\\)", ")");
								}
							}
						 if (!pdmType.equalsIgnoreCase(developType))
						 {
							 CompareUtil.append(table_pdm, table_develop, column_pdm, column_develop, 5, sb, vSql);
								iCount5++;
						 }
					}
				}
				if (!column_pdm.getNullable().equalsIgnoreCase(column_develop.getNullable()))// 是否为空不一致
				{
					CompareUtil.append(table_pdm, table_develop, column_pdm, column_develop, 6, sb, vSql);
					iCount6++;
				}
				if (column_develop.getColumnDefault() == null && column_pdm.getColumnDefault() != null)
				{
					CompareUtil.append(table_pdm, table_develop, column_pdm, column_develop, 7, sb, vSql);
					iCount7++;
				}
				if (column_develop.getColumnDefault() != null && column_pdm.getColumnDefault() == null)
				{
					CompareUtil.append(table_pdm, table_develop, column_pdm, column_develop, 7, sb, vSql);
					iCount7++;
				}
				if (column_develop.getColumnDefault() != null && column_pdm.getColumnDefault() != null)
				{
					if (!column_develop.getColumnDefault().equalsIgnoreCase(column_pdm.getColumnDefault()))// 是否为空不一致
					{
						CompareUtil.append(table_pdm, table_develop, column_pdm, column_develop, 7, sb, vSql);
						iCount7++;
					}
				}

				// 是否自增长不同
				if (column_develop.getExtra() == null && column_pdm.getExtra() != null)
				{
					CompareUtil.append(table_pdm, table_develop, column_pdm, column_develop, 8, sb, vSql);
					iCount8++;
				}
				if (column_develop.getExtra() != null && column_pdm.getExtra() == null)
				{
					CompareUtil.append(table_pdm, table_develop, column_pdm, column_develop, 8, sb, vSql);
					iCount8++;
				}
				if (column_develop.getExtra() != null && column_pdm.getExtra() != null)
				{
					if (!column_develop.getExtra().equalsIgnoreCase(column_pdm.getExtra()))
					{
						CompareUtil.append(table_pdm, table_develop, column_pdm, column_develop, 8, sb, vSql);
						iCount8++;
					}
				}

				// 比较顺序
				if (column_develop.getOrdinal_position() != column_pdm.getOrdinal_position())
				{
					/*
					logger.debug(table_develop.tableName + ":column_develop:" + column_develop.getColumnName() + " "
							+ column_develop.getOrdinal_position());
					logger.debug(table_pdm.tableName + ":column_pdm:" + column_pdm.getColumnName() + " "
							+ column_pdm.getOrdinal_position());
					*/		
					if (!CompareUtil.GetParentTableName(table_develop.getTableName()).equalsIgnoreCase(
							table_develop.getTableName()))
					{
						CompareUtil.append(table_pdm, table_develop, column_pdm, column_develop, 9, sb, vSql);
						iCount9++;
					}
				}
				
				//比较CHARACTER_SET_NAME 字符集				
				if (column_develop.getCHARACTER_SET_NAME() == null && column_pdm.getCHARACTER_SET_NAME() != null)
				{
					CompareUtil.append(table_pdm, table_develop, column_pdm, column_develop, 10, sb, vSql);
					iCount10++;
				}
				if (column_develop.getCHARACTER_SET_NAME() != null && column_pdm.getCHARACTER_SET_NAME() == null)
				{
					CompareUtil.append(table_pdm, table_develop, column_pdm, column_develop, 10, sb, vSql);
					iCount10++;
				}
				if (column_develop.getCHARACTER_SET_NAME() != null && column_pdm.getCHARACTER_SET_NAME() != null)
				{
					if (!column_develop.getCHARACTER_SET_NAME().equalsIgnoreCase(column_pdm.getCHARACTER_SET_NAME()))
					{
						CompareUtil.append(table_pdm, table_develop, column_pdm, column_develop, 10, sb, vSql);
						iCount10++;
					}
				}
			}
		}

	}

	public LinkedHashMap<String, Table> GetTables(Connection transaction, String inDomain, CompareInfo cCompareInfo)
			throws Exception
	{
		logger.info("GetTables:" + inDomain);
		String sSql = " select t1.TABLE_NAME,t1.COLUMN_NAME,t1.IS_NULLABLE,t1.COLUMN_TYPE,t1.Column_Default,t2.table_type,t1.extra,t1.ordinal_position,t1.CHARACTER_SET_NAME,COLLATION_NAME  "
				+ " from (select * from information_schema.COLUMNS where  table_schema=? )t1, (select * from information_schema.tables where  table_schema=?) t2 "
				+ " where t1.table_name=t2.table_name ";
		if (cCompareInfo.getStrExcludeTable() != null && cCompareInfo.getStrExcludeTable().length() > 0)
		{
			String[] excludeTables=cCompareInfo.getStrExcludeTable().split(",");
			for(int i=0;i<excludeTables.length;i++)
			{
				sSql += " and t1.table_name not like '" + excludeTables[i] + "%' ";
			}
		}
		sSql += " order By table_name,ordinal_position";
		PreparedStatement pstmt = transaction.prepareStatement(sSql);
		pstmt.setString(1, inDomain);
		pstmt.setString(2, inDomain);
		logger.debug(sSql);
		pstmt.setQueryTimeout(JpfDbUtils.ExecSqlTimeOut);

		ResultSet rs = pstmt.executeQuery();
		LinkedHashMap<String, Table> map = new LinkedHashMap<String, Table>();
		String tableName = "";
		Table table = null;
		while (rs.next())
		{
			if (!tableName.equals(rs.getString("table_name").toLowerCase().trim()))
			{// һ���±�
				String columnType="";
				if(rs.getString("COLUMN_TYPE")!=null)
				{
					columnType=rs.getString("COLUMN_TYPE").replaceAll("\\s*", "");
					columnType=columnType.toLowerCase().trim();
				}
				tableName = rs.getString("table_name").toLowerCase().trim();
				table = new Table(tableName, rs.getString("table_type").toLowerCase().trim());

				Column column = new Column(rs.getString("Column_Name").toLowerCase().trim(),
						columnType, rs.getString("IS_NULLABLE").toLowerCase()
								.trim(), CompareUtil.ShowDefaultValue(rs.getString("Column_Default")));
				column.setExtra(rs.getString("extra"));
				column.setOrdinal_position(rs.getInt("ordinal_position"));
				column.setCHARACTER_SET_NAME(rs.getString("CHARACTER_SET_NAME"));
				column.setCOLLATION_NAME(rs.getString("COLLATION_NAME"));
				table.columns.put(column.getColumnName(), column);
				map.put(tableName, table);
			} else
			{// �Ѵ��ڵı������ֶ�
				
				String columnType="";
				if(rs.getString("COLUMN_TYPE")!=null)
				{
					columnType=rs.getString("COLUMN_TYPE").replaceAll("\\s*", "");
					columnType=columnType.toLowerCase().trim();
				}
				Column column = new Column(rs.getString("Column_Name").toLowerCase().trim(),
						columnType, rs.getString("IS_NULLABLE").toLowerCase().trim(), CompareUtil.ShowDefaultValue(rs.getString("Column_Default")));
				column.setExtra(rs.getString("extra"));
				column.setOrdinal_position(rs.getInt("ordinal_position"));
				column.setCHARACTER_SET_NAME(rs.getString("CHARACTER_SET_NAME"));
				column.setCOLLATION_NAME(rs.getString("COLLATION_NAME"));
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

	protected void InsertResult(CompareInfo cCompareInfo)
	{
		Connection conn = null;
		try
		{
			conn = WallsDbConn.GetInstance().GetConn();
			String strSql = "delete from dbci where dbinfo='" + cCompareInfo.getDevJdbcUrl()
					+ "' and diffdate=current_date";
			//logger.info(strSql);
			JpfDbUtils.ExecUpdateSql(conn, strSql);
			strSql = "insert into dbci(dbmail,dbinfo,diffdate,diff1,diff2,diff3,diff4,diff5,diff6,diff7,diff8,diff9,diff10,referdbinfo) values( '"
					+ cCompareInfo.getStrMails()
					+ "','"
					+ cCompareInfo.getDevJdbcUrl()+"/"+cCompareInfo.getDbDomain()
					+ "',CURRENT_DATE,"
					+ iCount1					+ ","
					+ iCount2					+ ","
					+ iCount3					+ ","
					+ iCount4					+ ","
					+ iCount5					+ ","
					+ iCount6					+ ","
					+ iCount7					+ ","
					+ iCount8 + "," 
					+ iCount9 + "," 
					+ iCount10 + ",'"+cCompareInfo.getPdmJdbcUrl()+"')";
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
		return "<tr><td>" + iCount1 + "</td><td>"
				+ iCount2 + "</td><td>"
				+ iCount3 + "</td><td>"
				+ iCount4 + "</td><td>"
				+ iCount5 + "</td><td>"
				+ iCount6 + "</td><td>"
				+ iCount7 + "</td><td>"
				+ iCount8 + "</td><td>"
				+ iCount9 + "</td><td>"
				+ iCount10 + "</td></tr>";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jpf.ci.dbs.compare.AbstractDbCompare#GetMailTitle()
	 */
	@Override
	String GetMailTitle()
	{
		// TODO Auto-generated method stub
		return "数据库表比对带分表结果(自动发出)";
	}
	/* (non-Javadoc)
	 * @see org.jpf.ci.dbs.compare.AbstractDbCompare#GetExecSqlHtmlName()
	 */
	@Override
	String GetExecSqlHtmlName()
	{
		// TODO Auto-generated method stub
		return "compare_table2.html";
	}
}
