/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年1月15日 下午4:00:30 
 * 类说明 
 * 更改记录：sSql查询语句增加查询character_maximum_length（最大长度）字段，代替IS_NULLABLE字段作为compareTables() 方法中段长度比较部分
 */

package org.jpf.ci.dbs.compare;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompareTable
{
	private static final Logger logger = LogManager.getLogger();

	public StringBuffer[] sb = { new StringBuffer(), new StringBuffer(),
			new StringBuffer(), new StringBuffer(), new StringBuffer(),
			new StringBuffer() };

	//private String sSql = " select TABLE_NAME,COLUMN_NAME,IS_NULLABLE,COLUMN_TYPE ,character_maximum_length  from information_schema.COLUMNS where table_schema ='ad' order By table_name,column_name";
	////////////////////////////////MyTest
	private String sSql = " select TABLE_NAME,COLUMN_NAME,IS_NULLABLE,COLUMN_TYPE , character_maximum_length from information_schema.COLUMNS where table_schema ='testliu' order By table_name,column_name";
	


	public static void main(String[] args) throws Exception
	{
		CompareTable cCompareTable = new CompareTable();
	}
    public CompareTable()
    {
    	try
		{
    		compareTables(); // 比较数据库
    		CompareUtil.writeFile(sb); // 写入文件
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}

		System.out.println("game over");
    }
	public void compareTables() throws Exception
	{
		Connection trans_product = null;
		Connection trans_develop = null;
		try
		{

			// 生产数据库连接
			trans_product = DbInfo.GetInstance().getTransaction_product();
			Map<String, Table> map_product = getTables(trans_product);
			// 开发数据库连接
			trans_develop =  DbInfo.GetInstance().getTransaction_develop();
			Map<String, Table> map_develop = getTables(trans_develop);
			// 遍历开发库Map
			for (Iterator iter_table = map_develop.keySet().iterator(); iter_table.hasNext();)
			{
				String key_table = (String) iter_table.next();
				System.out.println("key_table:"+key_table);
				Table table_develop = map_develop.get(key_table);// 获得开发库中的表
				Table table_product = map_product.get(key_table);// 尝试从生产库中获得同名表
				if (table_product == null)
				{ // 如果获得表为空，说明开发存在，生产不存在
					CompareUtil.append(table_develop, null, 2,sb);
				} else
				{ // 表相同，判断字段、字段类型、字段长度
					for (Iterator iter_column = table_develop.columns.keySet().iterator(); iter_column.hasNext();)
					{
						String key_column = (String) iter_column.next();
						Column column_develop = (Column) table_develop.columns.get(key_column);// 获得开发库中的列
						Column column_product = (Column) table_product.columns.get(key_column);// 尝试从生产库中获得同名列
						if (column_product == null)
						{// 如果列名为空，说明开发存在，生产不存在
							CompareUtil.append(table_develop, column_develop, 4,sb);
						} else
						{// 说明两者都存在
							if (!column_develop.getDataType().equals(column_product.getDataType()))// 字段类型不一致
								CompareUtil.append(table_develop, column_develop, 5,sb);
							if (column_develop.getNullable().equals(column_product.getNullable()))// 字段长度不一致
								CompareUtil.append(table_develop, column_develop, 6,sb);
						}
					}
				}
			}
			// 遍历生产库Map
			for (Iterator iter_table = map_product.keySet().iterator(); iter_table
					.hasNext();)
			{
				String key_table = (String) iter_table.next();
				Table table_product = map_product.get(key_table);// 尝试从生产库中获得同名表
				Table table_develop = map_develop.get(key_table);// 获得开发库中的表
				if (table_develop == null)
				{ // 如果获得表为空，说明开发存在，生产不存在
					CompareUtil.append(table_product, null, 1,sb);
				} else
				{ // 表相同，判断字段、字段类型、字段长度
					for (Iterator iter_column = table_product.columns
							.keySet().iterator(); iter_column.hasNext();)
					{
						String key_column = (String) iter_column.next();
						Column column_product = (Column) table_product.columns.get(key_column);// 获得生产库中的列
						Column column_develop = (Column) table_develop.columns.get(key_column);// 尝试从开发库中获得同名列
						if (column_develop == null)
						{// 如果列名为空，说明生产存在，开发不存在
							CompareUtil.append(table_product, column_product, 3,sb);
						}
					}
				}
			}
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		} finally
		{
			DbUtils.DoClear(trans_product);
			DbUtils.DoClear(trans_develop);
		}
	}

	public Map<String, Table> getTables(Connection transaction) throws Exception
	{
		Statement stmt = transaction.createStatement();
		logger.info(sSql);
		ResultSet rs = stmt.executeQuery(sSql);
		Map<String, Table> map = new HashMap<String, Table>();
		String tableName = "";
		Table table = null;
		while (rs.next())
		{
			if (!tableName.equals(rs.getString("table_name")))
			{// 一张新表
				tableName = rs.getString("table_name");
				table = new Table(tableName);
				/*Column column = new Column(rs.getString("Column_Name"),
						rs.getString("COLUMN_TYPE"), rs.getString("IS_NULLABLE"));*/
				//改动此处
				Column column = new Column(rs.getString("Column_Name"),
						rs.getString("COLUMN_TYPE"), rs.getString("character_maximum_length"));
				table.columns.put(column.getColumnName(), column);
				//
				map.put(rs.getString("table_name"), table);
			} else
			{// 已存在的表，增加字段
				Column column = new Column(rs.getString("Column_Name"),
						rs.getString("COLUMN_TYPE"), rs.getString("IS_NULLABLE"));
				table.columns.put(column.getColumnName(), column);
			}
		}
		if (null != rs)
			rs.close();
		// transaction.finalize();
		return map;
	}


}
