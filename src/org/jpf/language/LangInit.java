package org.jpf.language;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;


/**
 * <p>对语言支持，装载</p>
 * <p>Title: WBASS</p>
 * <p>Description: WBASS</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: asiainfo</p>
 * @author 吴平福
 * @version 2.0
 */
public class LangInit
{
   //语言存放MAP
   HashMap MutiMap = new HashMap();

   //后台语言文件名
   private String PropPath = "";
   private static final String KEYSIGN = "=";
   //this.getServletContext().getRealPath("/")+"/WEB-INF/classes//

   public LangInit(String inPath, String inLocale)
   {
      PropPath = inPath;
      //设置后台语言
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
         //读取前台
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
    * @todo 从文件中读取语言并装载
    * @param in_FileName String
    * @param in_LanguageType String
    * @throws Exception
    */
   private void ReadLangFile(String in_FileName, String in_LanguageType) throws
      Exception
   {
      BufferedReader in = new BufferedReader(new FileReader(in_FileName)); //建立BufferedReader对象，并实例化为br
      String Line;
      int m = -1;
      while ( (Line = in.readLine()) != null)
      {
         Line = Line.trim();
         //判断是否空行
         if (Line.length() == 0)
         {
            continue;
         }
         //判断是不是注释行
         if (Line.substring(0, 1).equals("#"))
         {
            continue;
         }

         //System.out.println("line:" + Line);
         //判断是否是否含有"="符合键-值规则
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
    * @todo 初始化装入
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
    * @todo 正常装入
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
