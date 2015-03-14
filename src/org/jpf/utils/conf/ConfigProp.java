package org.jpf.utils.conf;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfFileUtil;
import org.jpf.utils.StringUtil;

/**
 *
 * <p>Title: NGBOSS--WBASS</p>
 *
 * <p>Description: 读取配置文件封装</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: asiainfo</p>
 *
 * @author 吴平福
 * @version 4.0
 */
public class ConfigProp
{
	private static final Logger logger = LogManager.getLogger();

   public ConfigProp()
   {
   }

   /**
    * <p>通过key名称获得配置文件的相关信息</p>
    * @param key key名称
    * @return String 配置文件信息
    */
   public static String GetStrFromConfig(String strFileName,String strKey)
   {
      try {
         Properties property = new Properties();
         property.load(new FileInputStream(strFileName));
         return property.getProperty(strKey, "");
      } catch (FileNotFoundException e) {
         System.err.println("配置文件"+strFileName+"找不到！！");
         e.printStackTrace();
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      return "";
   }

   /**
    * <p>通过key名称获得配置文件的相关信息</p>
    * @param key key名称
    * @return String 配置文件信息
    */
   public static String GetStrFromConfigWithException(String strFileName,String strKey) throws
      Exception
   {
      Properties property = new Properties();
      JpfFileUtil.CheckFile(strFileName);
      property.load(new FileInputStream(strFileName));
      String m_str = property.getProperty(strKey);
      if (m_str == null || "".equalsIgnoreCase(m_str)) {
         throw new Exception("配置项没有找到:" + strKey);
      }
      return m_str;
   }

   /**
    * <p>通过key名称获得配置文件的相关信息</p>
    * @param key key名称
    * @sDefaultValue 默认值
    * @return String 配置文件信息
    */
   public static String GetStrFromConfig(String strFileName,String sKey, String sDefaultValue)
   {
      String m_str = GetStrFromConfig(strFileName,sKey);
      if (null == m_str || "".equals(m_str)) {
         //加入WARN
         //JSLog.printLog(logger, JSLog.LOG_WARN, sKey + "缺少值,请检查配置文件");\
         logger.warn(sKey + "缺少值,请检查配置文件");
         return sDefaultValue;
      }
      else {
         return m_str;
      }
   }

   /**
    * <p>通过key名称获得配置文件的相关信息</p>
    * @param key key名称
    * @sDefaultValue 默认值
    * @return int 配置文件信息
    */
   public static int GetIntFromConfig(String strFileName,String sKey, int sDefaultValue)
   {
      String m_str = GetStrFromConfig(strFileName,sKey);
      if (null == m_str || "".equals(m_str)) {
         //JSLog.printLog(logger, JSLog.LOG_WARN, sKey + "缺少值,请检查配置文件");
         logger.warn(sKey + "缺少值,请检查配置文件");
         return sDefaultValue;
      }
      else {
         int i = StringUtil.getStrToInt(m_str);
         if (i < sDefaultValue) {
            //JSLog.printLog(logger, JSLog.LOG_WARN, sKey + "缺少值,请检查配置文件");
            logger.warn(sKey + "缺少值,请检查配置文件");
            return sDefaultValue;
         }
         else {
            return i;
         }
      }
   }

   /**
    * <p>通过key名称获得配置文件的相关信息</p>
    * @param key key名称
    * @return int 配置文件信息
    */
   public static int GetIntFromConfig(String strFileName,String sKey)
   {
      String m_str = GetStrFromConfig(strFileName,sKey);
      if (null == m_str || "".equals(m_str)) {
         //JSLog.printLog(logger, JSLog.LOG_ERROR, sKey + "缺少值,请检查配置文件");
         logger.warn(sKey + "缺少值,请检查配置文件");
         return -1;
      }
      else {
         return StringUtil.getStrToInt(m_str);
      }
   }
	public static void saveFile(String fileName, String description,String strItems) throws Exception
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(fileName);
			Properties config = new Properties();
			String[] strKeyValue=strItems.split(";");
			for(int i=0;i<strKeyValue.length;i++)
			{
				String[] strItem=strKeyValue[i].split("=");
				config.setProperty(strItem[0],strItem[1]); 
			}
			config.store(fout, description);// 保存文件
			fout.close();
		} catch (IOException ex)
		{
			throw new Exception("无法保存指定的配置文件:" + fileName);
		}
	}
}
