package org.jpf.ci.dbs.compare;

/**
 * @author liu
 * 
 * @version 2015年1月22日创建
 * 设置连接连个数据库name，password，url
 */
public class VarStatic {
	public static String URL1 = "jdbc:mysql://localhost:3306/";
    public static String dbuser1 = "root";
	public static String dbpass1 = "123456";
	
	public static  String URL2 = "jdbc:mysql://localhost:3306/";
	public static String dbuser2 = "root";
	public static  String dbpass2 = "123456";
	
	public static String getURL1() {
		return URL1;
	}
	public static void setURL1(String uRL1) {
		URL1 = uRL1;
	}
	public static String getDbuser1() {
		return dbuser1;
	}
	public static void setDbuser1(String dbuser1) {
		VarStatic.dbuser1 = dbuser1;
	}
	public static String getDbpass1() {
		return dbpass1;
	}
	public static void setDbpass1(String dbpass1) {
		VarStatic.dbpass1 = dbpass1;
	}
	public static String getURL2() {
		return URL2;
	}
	public static void setURL2(String uRL2) {
		URL2 = uRL2;
	}
	public static String getDbuser2() {
		return dbuser2;
	}
	public static void setDbuser2(String dbuser2) {
		VarStatic.dbuser2 = dbuser2;
	}
	public static String getDbpass2() {
		return dbpass2;
	}
	public static void setDbpass2(String dbpass2) {
		VarStatic.dbpass2 = dbpass2;
	}
}
