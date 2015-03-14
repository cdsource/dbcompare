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
 * <p>Description: ��ȡ�����ļ���װ</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: asiainfo</p>
 *
 * @author ��ƽ��
 * @version 4.0
 */
public class ConfigProp
{
	private static final Logger logger = LogManager.getLogger();

   public ConfigProp()
   {
   }

   /**
    * <p>ͨ��key���ƻ�������ļ��������Ϣ</p>
    * @param key key����
    * @return String �����ļ���Ϣ
    */
   public static String GetStrFromConfig(String strFileName,String strKey)
   {
      try {
         Properties property = new Properties();
         property.load(new FileInputStream(strFileName));
         return property.getProperty(strKey, "");
      } catch (FileNotFoundException e) {
         System.err.println("�����ļ�"+strFileName+"�Ҳ�������");
         e.printStackTrace();
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      return "";
   }

   /**
    * <p>ͨ��key���ƻ�������ļ��������Ϣ</p>
    * @param key key����
    * @return String �����ļ���Ϣ
    */
   public static String GetStrFromConfigWithException(String strFileName,String strKey) throws
      Exception
   {
      Properties property = new Properties();
      JpfFileUtil.CheckFile(strFileName);
      property.load(new FileInputStream(strFileName));
      String m_str = property.getProperty(strKey);
      if (m_str == null || "".equalsIgnoreCase(m_str)) {
         throw new Exception("������û���ҵ�:" + strKey);
      }
      return m_str;
   }

   /**
    * <p>ͨ��key���ƻ�������ļ��������Ϣ</p>
    * @param key key����
    * @sDefaultValue Ĭ��ֵ
    * @return String �����ļ���Ϣ
    */
   public static String GetStrFromConfig(String strFileName,String sKey, String sDefaultValue)
   {
      String m_str = GetStrFromConfig(strFileName,sKey);
      if (null == m_str || "".equals(m_str)) {
         //����WARN
         //JSLog.printLog(logger, JSLog.LOG_WARN, sKey + "ȱ��ֵ,���������ļ�");\
         logger.warn(sKey + "ȱ��ֵ,���������ļ�");
         return sDefaultValue;
      }
      else {
         return m_str;
      }
   }

   /**
    * <p>ͨ��key���ƻ�������ļ��������Ϣ</p>
    * @param key key����
    * @sDefaultValue Ĭ��ֵ
    * @return int �����ļ���Ϣ
    */
   public static int GetIntFromConfig(String strFileName,String sKey, int sDefaultValue)
   {
      String m_str = GetStrFromConfig(strFileName,sKey);
      if (null == m_str || "".equals(m_str)) {
         //JSLog.printLog(logger, JSLog.LOG_WARN, sKey + "ȱ��ֵ,���������ļ�");
         logger.warn(sKey + "ȱ��ֵ,���������ļ�");
         return sDefaultValue;
      }
      else {
         int i = StringUtil.getStrToInt(m_str);
         if (i < sDefaultValue) {
            //JSLog.printLog(logger, JSLog.LOG_WARN, sKey + "ȱ��ֵ,���������ļ�");
            logger.warn(sKey + "ȱ��ֵ,���������ļ�");
            return sDefaultValue;
         }
         else {
            return i;
         }
      }
   }

   /**
    * <p>ͨ��key���ƻ�������ļ��������Ϣ</p>
    * @param key key����
    * @return int �����ļ���Ϣ
    */
   public static int GetIntFromConfig(String strFileName,String sKey)
   {
      String m_str = GetStrFromConfig(strFileName,sKey);
      if (null == m_str || "".equals(m_str)) {
         //JSLog.printLog(logger, JSLog.LOG_ERROR, sKey + "ȱ��ֵ,���������ļ�");
         logger.warn(sKey + "ȱ��ֵ,���������ļ�");
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
			config.store(fout, description);// �����ļ�
			fout.close();
		} catch (IOException ex)
		{
			throw new Exception("�޷�����ָ���������ļ�:" + fileName);
		}
	}
}
