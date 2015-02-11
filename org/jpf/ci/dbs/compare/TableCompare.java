/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2015年1月16日 上午12:03:48 
* 类说明 
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
			"D://table//生产存在，开发不存在的表.txt",
			"D://table//生产不存在，开发存在的表.txt",
			"D://table//生产存在，开发不存在的字段.txt",
			"D://table//生产不存在，开发存在的字段.txt",
			"D://table//表和字段都相同，但字段类型不同的内容.txt",
			"D://table//表和字段、字段类型都相同，但字段长度不同的内容.txt",
			"D://table//开发人员确认后需要新增处理字段.txt"
			};


	public static void main(String[] args) throws Exception{
		compareTables(); //比较数据库
		writeFile(); //写入文件
		creditColumnDDL();//生成DDL文件
	}
	
	/**
	 * 根据比较出来的TXT，生成创建字段的DDL语句</br>
	 * 格式为：
	 * <li>表名1,字段名1</li>
	 * <li>表名1,字段名2</li>
	 * <li>表名2,字段名1</li>
	 * <li>表名2,字段名2</li>
	 * @author YUJIYU090
	 * */
	public static void creditColumnDDL() throws Exception{
		
		StringBuffer sb = new StringBuffer();
		
		//查询某个表的某个字段的表名、字段名、字段类型、长度类型、NUM型的总位数、NUM小数位数，注释
		String sSql =  " Select Utc.Table_Name, Utc.Column_Name,Utc.Data_Type,"+
				       " Utc.Data_Length,Utc.Data_Precision,Utc.Data_Scale,Ucc.Comments" +
				       " From User_Tab_Columns Utc, User_Col_Comments Ucc"+
				       " Where Utc.Table_Name = Ucc.Table_Name"+
				       " And Utc.Column_Name = Ucc.Column_Name And Utc.Table_Name = ?"+
				       " And Utc.Column_Name = ?";
		
		File file = new File("D://table//开发人员确认后需要新增处理字段.txt");
		BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
		
		//开发连接
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
				//如果类型为VARCHAR2
				if(dataType.equalsIgnoreCase("VARCHAR2")){
					sb.append(dataType+"("+length+");");
				}else{//否则类型为数型
					if(precision.equals("")){ //为INTEGER
						sb.append("INTEGER;");
					}else{//为NUMBER
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
		
		File Outfile = new File("D://table//5.4小微新增字段DDL.sql");
		OutputStream os = new FileOutputStream(Outfile);
		os.write(sb.toString().getBytes());os.flush();os.close();
		
		if(ps!=null)ps.close();
		if(rs!=null)rs.close();
		trans_develop.finalize();
	}
	
	/**
	 * 比较生产库和开发库的数据表，包括表名、字段名、字段类型、字段长度
	 * @author YUJIYU090
	 * */
	public static void compareTables() throws Exception{
		
		//1、生产存在，开发不存在的表--跳过
		//2、生产不存在，开发存在的表--需要人工判断脚本
		//3、生产存在，开发不存在的字段--需人工判断如何处理
		//4、生产不存在，开发存在的字段--需要人工判断脚本
		//5、表和字段都相同，但字段类型不同的内容--需要人工判断脚本
		//6、表和字段、字段类型都相同，但字段长度不同的内容--需要人工判断脚本
		
		//生产连接
		Transaction trans_product = DBUtil.getTransaction_product();
		Map<String, Table> map_product = getTables(trans_product);

		//开发连接
		Transaction trans_develop = DBUtil.getTransaction_develop();
		Map<String, Table> map_develop = getTables(trans_develop);
		
		//遍历开发库Map
		for (Iterator<String> iter_table = map_develop.keySet().iterator();  iter_table.hasNext();) {
			String key_table = (String) iter_table.next();
			Table table_develop = map_develop.get(key_table);//获得开发库中的表
			Table table_product = map_product.get(key_table);//尝试从生产库中获得同名表
			if(table_product == null){ //如果获得表为空，说明开发存在，生产不存在
				append(table_develop,null,2);
			}else{ //表相同，判断字段、字段类型、字段长度
				for(Iterator<String> iter_column = table_develop.columns.keySet().iterator();  iter_column.hasNext();) {
					String key_column = (String) iter_column.next();
					Column column_develop  = table_develop.columns.get(key_column);//获得开发库中的列
					Column column_product  = table_product.columns.get(key_column);//尝试从生产库中获得同名列
					if(column_product==null){//如果列名为空，说明开发存在，生产不存在
						append(table_develop, column_develop, 4);
					}else{//说明两者都存在
						if(!column_develop.dataType.equals(column_product.dataType))//字段类型不一致
							append(table_develop, column_develop, 5);
						if(column_develop.length!=column_product.length)//字段长度不一致
							append(table_develop, column_develop, 6);
					}
				}
			}
		}
		
		//遍历生产库Map
		for (Iterator<String> iter_table = map_product.keySet().iterator();  iter_table.hasNext();) {
			String key_table = (String) iter_table.next();
			Table table_product = map_product.get(key_table);//尝试从生产库中获得同名表
			Table table_develop = map_develop.get(key_table);//获得开发库中的表
			if(table_develop == null){ //如果获得表为空，说明开发存在，生产不存在
				append(table_product,null,1);
			}else{ //表相同，判断字段、字段类型、字段长度
				for(Iterator<String> iter_column = table_product.columns.keySet().iterator();  iter_column.hasNext();) {
					String key_column = (String) iter_column.next();
					Column column_product  = table_product.columns.get(key_column);//获得生产库中的列
					Column column_develop  = table_develop.columns.get(key_column);//尝试从开发库中获得同名列
					if(column_develop==null){//如果列名为空，说明生产存在，开发不存在
						append(table_product, column_product, 3);
					}
				}
			}
		}
	}

	/**
	 * 传入数据库连接，返回数据库中所有TABLE对象的MAP
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
			if(!tableName.equals(rs.getString("table_name"))){//一张新表
				tableName = rs.getString("table_name");
				table = new Table(tableName);
				Column column = new Column(rs.getString("Column_Name"),rs.getString("Data_Type"),rs.getInt("Length"));
				table.columns.put(column.columnName, column);
				map.put(rs.getString("table_name"),table);
			}else{//已存在的表，增加字段
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
	 * 根据标示位，追加到满足条件的StringBuffer
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
	 * 将StringBuffer中的值写入文件中
	 * */
	public static void writeFile() throws Exception{
		for (int i = 0; i < fileName.length; i++) {
			File file = new File(fileName[i]);
			OutputStream os = new FileOutputStream(file);
			os.write(sb[i].toString().getBytes());os.flush();os.close();
		}
	}
}
	

