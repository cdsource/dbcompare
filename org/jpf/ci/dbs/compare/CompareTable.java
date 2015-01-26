/** 
 * @author ��ƽ�� 
 * E-mail:wupf@asiainfo.com 
 * @version ����ʱ�䣺2015��1��15�� ����4:00:30 
 * ��˵�� 
 * ���ļ�¼��sSql��ѯ������Ӳ�ѯcharacter_maximum_length����󳤶ȣ��ֶΣ�����IS_NULLABLE�ֶ���ΪcompareTables() �����жγ��ȱȽϲ���
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
    		compareTables(); // �Ƚ����ݿ�
    		CompareUtil.writeFile(sb); // д���ļ�
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

			// �������ݿ�����
			trans_product = DbInfo.GetInstance().getTransaction_product();
			Map<String, Table> map_product = getTables(trans_product);
			// �������ݿ�����
			trans_develop =  DbInfo.GetInstance().getTransaction_develop();
			Map<String, Table> map_develop = getTables(trans_develop);
			// ����������Map
			for (Iterator iter_table = map_develop.keySet().iterator(); iter_table.hasNext();)
			{
				String key_table = (String) iter_table.next();
				System.out.println("key_table:"+key_table);
				Table table_develop = map_develop.get(key_table);// ��ÿ������еı�
				Table table_product = map_product.get(key_table);// ���Դ��������л��ͬ����
				if (table_product == null)
				{ // �����ñ�Ϊ�գ�˵���������ڣ�����������
					CompareUtil.append(table_develop, null, 2,sb);
				} else
				{ // ����ͬ���ж��ֶΡ��ֶ����͡��ֶγ���
					for (Iterator iter_column = table_develop.columns.keySet().iterator(); iter_column.hasNext();)
					{
						String key_column = (String) iter_column.next();
						Column column_develop = (Column) table_develop.columns.get(key_column);// ��ÿ������е���
						Column column_product = (Column) table_product.columns.get(key_column);// ���Դ��������л��ͬ����
						if (column_product == null)
						{// �������Ϊ�գ�˵���������ڣ�����������
							CompareUtil.append(table_develop, column_develop, 4,sb);
						} else
						{// ˵�����߶�����
							if (!column_develop.getDataType().equals(column_product.getDataType()))// �ֶ����Ͳ�һ��
								CompareUtil.append(table_develop, column_develop, 5,sb);
							if (column_develop.getNullable().equals(column_product.getNullable()))// �ֶγ��Ȳ�һ��
								CompareUtil.append(table_develop, column_develop, 6,sb);
						}
					}
				}
			}
			// ����������Map
			for (Iterator iter_table = map_product.keySet().iterator(); iter_table
					.hasNext();)
			{
				String key_table = (String) iter_table.next();
				Table table_product = map_product.get(key_table);// ���Դ��������л��ͬ����
				Table table_develop = map_develop.get(key_table);// ��ÿ������еı�
				if (table_develop == null)
				{ // �����ñ�Ϊ�գ�˵���������ڣ�����������
					CompareUtil.append(table_product, null, 1,sb);
				} else
				{ // ����ͬ���ж��ֶΡ��ֶ����͡��ֶγ���
					for (Iterator iter_column = table_product.columns
							.keySet().iterator(); iter_column.hasNext();)
					{
						String key_column = (String) iter_column.next();
						Column column_product = (Column) table_product.columns.get(key_column);// ����������е���
						Column column_develop = (Column) table_develop.columns.get(key_column);// ���Դӿ������л��ͬ����
						if (column_develop == null)
						{// �������Ϊ�գ�˵���������ڣ�����������
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
			{// һ���±�
				tableName = rs.getString("table_name");
				table = new Table(tableName);
				/*Column column = new Column(rs.getString("Column_Name"),
						rs.getString("COLUMN_TYPE"), rs.getString("IS_NULLABLE"));*/
				//�Ķ��˴�
				Column column = new Column(rs.getString("Column_Name"),
						rs.getString("COLUMN_TYPE"), rs.getString("character_maximum_length"));
				table.columns.put(column.getColumnName(), column);
				//
				map.put(rs.getString("table_name"), table);
			} else
			{// �Ѵ��ڵı������ֶ�
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
