/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2015��1��16�� ����12:03:48 
* ��˵�� 
*/ 

package org.jpf.ci.dbs.compare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class TableCompare{
	
	public static StringBuffer[] sb = {new StringBuffer(),new StringBuffer(),new StringBuffer(),new StringBuffer(),new StringBuffer(),new StringBuffer()};
	
	public static String[] fileName = {
			"D://table//�������ڣ����������ڵı�.txt",
			"D://table//���������ڣ��������ڵı�.txt",
			"D://table//�������ڣ����������ڵ��ֶ�.txt",
			"D://table//���������ڣ��������ڵ��ֶ�.txt",
			"D://table//����ֶζ���ͬ�����ֶ����Ͳ�ͬ������.txt",
			"D://table//����ֶΡ��ֶ����Ͷ���ͬ�����ֶγ��Ȳ�ͬ������.txt",
			"D://table//������Աȷ�Ϻ���Ҫ���������ֶ�.txt"
			};


	public static void main(String[] args) throws Exception{
		compareTables(); //�Ƚ����ݿ�
		writeFile(); //д���ļ�
		creditColumnDDL();//����DDL�ļ�
	}
	
	/**
	 * ���ݱȽϳ�����TXT�����ɴ����ֶε�DDL���</br>
	 * ��ʽΪ��
	 * <li>����1,�ֶ���1</li>
	 * <li>����1,�ֶ���2</li>
	 * <li>����2,�ֶ���1</li>
	 * <li>����2,�ֶ���2</li>
	 * @author YUJIYU090
	 * */
	public static void creditColumnDDL() throws Exception{
		
		StringBuffer sb = new StringBuffer();
		
		//��ѯĳ�����ĳ���ֶεı������ֶ������ֶ����͡��������͡�NUM�͵���λ����NUMС��λ����ע��
		String sSql =  " Select Utc.Table_Name, Utc.Column_Name,Utc.Data_Type,"+
				       " Utc.Data_Length,Utc.Data_Precision,Utc.Data_Scale,Ucc.Comments" +
				       " From User_Tab_Columns Utc, User_Col_Comments Ucc"+
				       " Where Utc.Table_Name = Ucc.Table_Name"+
				       " And Utc.Column_Name = Ucc.Column_Name And Utc.Table_Name = ?"+
				       " And Utc.Column_Name = ?";
		
		File file = new File("D://table//������Աȷ�Ϻ���Ҫ���������ֶ�.txt");
		BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
		
		//��������
		Transaction trans_develop = DBUtil.getTransaction_develop();
		PreparedStatement ps = trans_develop.conn.prepareStatement(sSql);
		ResultSet rs= null;
		
		String line="";
		String[] arr=null;
		while((line=br.readLine()) != null){
			arr = (line.trim()).split(",");
			ps.setString(1, arr[0]);
			ps.setString(2, arr[1]);
			rs = ps.executeQuery();
			
			if(rs.next()){
				String tableName = DataConvert.toString(rs.getString("Table_Name"));
				String columnName = DataConvert.toString(rs.getString("Column_Name"));
				String dataType = DataConvert.toString(rs.getString("Data_Type"));
				String length = DataConvert.toString(rs.getString("Data_Length"));
				String precision = DataConvert.toString(rs.getString("Data_Precision"));
				String scale = DataConvert.toString(rs.getString("Data_Scale"));
				String comments = DataConvert.toString(rs.getString("Comments"));
				
				sb.append("-- Add/modify columns \n");
				sb.append("alter table "+tableName+" add "+columnName+" ");
				//�������ΪVARCHAR2
				if(dataType.equalsIgnoreCase("VARCHAR2")){
					sb.append(dataType+"("+length+");");
				}else{//��������Ϊ����
					if(precision.equals("")){ //ΪINTEGER
						sb.append("INTEGER;");
					}else{//ΪNUMBER
						sb.append(dataType+"("+precision+","+scale+");");
					}
				}
				if(!comments.equals("")){
					sb.append("\n-- Add comments to the columns\n");
					sb.append("comment on column "+tableName+"."+columnName);
					sb.append("\n");
					sb.append("is '"+comments+"';");
				}
				sb.append("\n\n");
				System.out.println(sb.toString());
				System.out.println();
				rs.close();
			}
		}
		
		File Outfile = new File("D://table//5.4С΢�����ֶ�DDL.sql");
		OutputStream os = new FileOutputStream(Outfile);
		os.write(sb.toString().getBytes());os.flush();os.close();
		
		if(ps!=null)ps.close();
		if(rs!=null)rs.close();
		trans_develop.finalize();
	}
	
	/**
	 * �Ƚ�������Ϳ���������ݱ������������ֶ������ֶ����͡��ֶγ���
	 * @author YUJIYU090
	 * */
	public static void compareTables() throws Exception{
		
		//1���������ڣ����������ڵı�--����
		//2�����������ڣ��������ڵı�--��Ҫ�˹��жϽű�
		//3���������ڣ����������ڵ��ֶ�--���˹��ж���δ���
		//4�����������ڣ��������ڵ��ֶ�--��Ҫ�˹��жϽű�
		//5������ֶζ���ͬ�����ֶ����Ͳ�ͬ������--��Ҫ�˹��жϽű�
		//6������ֶΡ��ֶ����Ͷ���ͬ�����ֶγ��Ȳ�ͬ������--��Ҫ�˹��жϽű�
		
		//��������
		Transaction trans_product = DBUtil.getTransaction_product();
		Map<String, Table> map_product = getTables(trans_product);

		//��������
		Transaction trans_develop = DBUtil.getTransaction_develop();
		Map<String, Table> map_develop = getTables(trans_develop);
		
		//����������Map
		for (Iterator<String> iter_table = map_develop.keySet().iterator();  iter_table.hasNext();) {
			String key_table = (String) iter_table.next();
			Table table_develop = map_develop.get(key_table);//��ÿ������еı�
			Table table_product = map_product.get(key_table);//���Դ��������л��ͬ����
			if(table_product == null){ //�����ñ�Ϊ�գ�˵���������ڣ�����������
				append(table_develop,null,2);
			}else{ //����ͬ���ж��ֶΡ��ֶ����͡��ֶγ���
				for(Iterator<String> iter_column = table_develop.columns.keySet().iterator();  iter_column.hasNext();) {
					String key_column = (String) iter_column.next();
					Column column_develop  = table_develop.columns.get(key_column);//��ÿ������е���
					Column column_product  = table_product.columns.get(key_column);//���Դ��������л��ͬ����
					if(column_product==null){//�������Ϊ�գ�˵���������ڣ�����������
						append(table_develop, column_develop, 4);
					}else{//˵�����߶�����
						if(!column_develop.dataType.equals(column_product.dataType))//�ֶ����Ͳ�һ��
							append(table_develop, column_develop, 5);
						if(column_develop.length!=column_product.length)//�ֶγ��Ȳ�һ��
							append(table_develop, column_develop, 6);
					}
				}
			}
		}
		
		//����������Map
		for (Iterator<String> iter_table = map_product.keySet().iterator();  iter_table.hasNext();) {
			String key_table = (String) iter_table.next();
			Table table_product = map_product.get(key_table);//���Դ��������л��ͬ����
			Table table_develop = map_develop.get(key_table);//��ÿ������еı�
			if(table_develop == null){ //�����ñ�Ϊ�գ�˵���������ڣ�����������
				append(table_product,null,1);
			}else{ //����ͬ���ж��ֶΡ��ֶ����͡��ֶγ���
				for(Iterator<String> iter_column = table_product.columns.keySet().iterator();  iter_column.hasNext();) {
					String key_column = (String) iter_column.next();
					Column column_product  = table_product.columns.get(key_column);//����������е���
					Column column_develop  = table_develop.columns.get(key_column);//���Դӿ������л��ͬ����
					if(column_develop==null){//�������Ϊ�գ�˵���������ڣ�����������
						append(table_product, column_product, 3);
					}
				}
			}
		}
	}

	/**
	 * �������ݿ����ӣ��������ݿ�������TABLE�����MAP
	 * @author YUJIYU090
	 * */
	public static Map<String, Table> getTables(Transaction transaction) throws Exception{
		
		String sSql = " select table_name,Column_Name,Data_Type,"+
		" DECODE(DATA_TYPE,'NUMBER',DATA_PRECISION,'VARCHAR2',DATA_LENGTH,'VARCHAR',DATA_LENGTH,'CHAR',DATA_LENGTH,0) Length,"+
		" NVL(DATA_SCALE, 0) SCALE,DECODE(NULLABLE, 'N', '1', '0') NULLABLE "+
		" from user_tab_columns where 1=1 And table_name Not Like 'BIN$%' Order By table_name,column_name";
		
		ASResultSet rs = transaction.getASResultSet(sSql);
		
		Map<String, Table> map = new HashMap<String, Table>();
		
		String tableName="";
		Table table = null;
		while(rs.next()){
			if(!tableName.equals(rs.getString("table_name"))){//һ���±�
				tableName = rs.getString("table_name");
				table = new Table(tableName);
				Column column = new Column(rs.getString("Column_Name"),rs.getString("Data_Type"),rs.getInt("Length"));
				table.columns.put(column.columnName, column);
				map.put(rs.getString("table_name"),table);
			}else{//�Ѵ��ڵı������ֶ�
				Column column = new Column(rs.getString("Column_Name"),rs.getString("Data_Type"),rs.getInt("Length"));
				table.columns.put(column.columnName, column);
			}
		}
		if(null!=rs) rs.close();
		transaction.finalize();
		return map;
	}
	
	/**
	 * @author YUJIYU090
	 * ���ݱ�ʾλ��׷�ӵ�����������StringBuffer
	 * */
	public static void append(Table table,Column column, int flag) throws Exception{
		switch (flag) {
			case 1: 
				sb[0].append(table.getTableName()+"\n");
				break;
			case 2: 
				sb[1].append(table.getTableName()+"\n");
				break;
			case 3: 
				sb[2].append(table.getTableName()+" | "+column.getColumnName()+"\n");
				break;
			case 4: 
				sb[3].append(table.getTableName()+" | "+column.getColumnName()+"\n");
				break;
			case 5: 
				sb[4].append(table.getTableName()+" | "+column.getColumnName()+" | "+column.getDataType()+"\n");
				break;
			case 6: 
				sb[5].append(table.getTableName()+" | "+column.getColumnName()+" | "+column.getLength()+"\n");
				break;
		}
	}
	
	/**
	 * @author YUJIYU090
	 * ��StringBuffer�е�ֵд���ļ���
	 * */
	public static void writeFile() throws Exception{
		for (int i = 0; i < fileName.length; i++) {
			File file = new File(fileName[i]);
			OutputStream os = new FileOutputStream(file);
			os.write(sb[i].toString().getBytes());os.flush();os.close();
		}
	}
}
	

