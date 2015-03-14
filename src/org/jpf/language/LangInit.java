package org.jpf.language;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;


/**
 * <p>������֧�֣�װ��</p>
 * <p>Title: WBASS</p>
 * <p>Description: WBASS</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: asiainfo</p>
 * @author ��ƽ��
 * @version 2.0
 */
public class LangInit
{
   //���Դ��MAP
   HashMap MutiMap = new HashMap();

   //��̨�����ļ���
   private String PropPath = "";
   private static final String KEYSIGN = "=";
   //this.getServletContext().getRealPath("/")+"/WEB-INF/classes//

   public LangInit(String inPath, String inLocale)
   {
      PropPath = inPath;
      //���ú�̨����
      LangUtil.setLocale(inLocale);
   }

   /**
    * @todo init
    * @throws ServletException
    */
   public void init() throws Exception
   {
      try
      {
         //��ȡǰ̨
         ReadLangFile(PropPath + "WBASSLANG_zh_CN.properties", "zh_CN");
         ReadLangFile(PropPath + "WBASSLANG_en.properties", "en");
         LangUtil.setKeyLangMap(MutiMap);
         System.out.println("Load language properties file success!");
      } catch (Exception ex)
      {
         ex.printStackTrace();
      }
   }

   /**
    * @todo ���ļ��ж�ȡ���Բ�װ��
    * @param in_FileName String
    * @param in_LanguageType String
    * @throws Exception
    */
   private void ReadLangFile(String in_FileName, String in_LanguageType) throws
      Exception
   {
      BufferedReader in = new BufferedReader(new FileReader(in_FileName)); //����BufferedReader���󣬲�ʵ����Ϊbr
      String Line;
      int m = -1;
      while ( (Line = in.readLine()) != null)
      {
         Line = Line.trim();
         //�ж��Ƿ����
         if (Line.length() == 0)
         {
            continue;
         }
         //�ж��ǲ���ע����
         if (Line.substring(0, 1).equals("#"))
         {
            continue;
         }

         //System.out.println("line:" + Line);
         //�ж��Ƿ��Ƿ���"="���ϼ�-ֵ����
         m = Line.indexOf(KEYSIGN);
         if (m >= 0)
         {
            PutLang2(Line.trim().substring(0, m), in_LanguageType,
                     Line.trim().substring(m + KEYSIGN.length()));
         }
      }

      in.close();

   }

   /**
    * @todo ��ʼ��װ��
    * @param strKey String
    * @param strLanguage String
    * @param strKeyValue String
    */
   private void PutLang(String strKey, String strLanguage, String strKeyValue)
   {
      MutiLang m_ml = new MutiLang();
      m_ml.m_map.put(strLanguage, strKeyValue);
      MutiMap.put(strKey, m_ml);

   }

   /**
    * @todo ����װ��
    * @param strKey String
    * @param strLanguage String
    * @param strKeyValue String
    */
   private void PutLang2(String strKey, String strLanguage, String strKeyValue)
   {
      MutiLang m_ml = (MutiLang) MutiMap.get(strKey);
      if (m_ml == null)
      {
         PutLang(strKey, strLanguage, strKeyValue);
      } else
      {
         m_ml.m_map.put(strLanguage, strKeyValue);
      }

   }

}
