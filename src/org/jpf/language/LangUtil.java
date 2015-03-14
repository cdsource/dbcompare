package org.jpf.language;

import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * <p>Title: WBASS</p>
 * <p>Description: WBASS</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: asiainfo</p>
 * @author 吴平福
 * @version 2.0
 */

public class LangUtil
{
   //默认简体中文 zh_CN
   private static String default_locale = "zh_CN";
   private static final String BackLangFileName = "BackLangFileName";
   private static HashMap MutiMap;
   public LangUtil()
   {

   }

   /**
    * @todo 根据不同语言取值
    * @param strKey String
    * @param strLanguage String
    * @return String
    */
   private static String getMutiStr(String strKey, String strLanguage)
   {
      MutiLang m_ml = (MutiLang) MutiMap.get(strKey);
      if (m_ml != null)
      {
         String m_str = (String) m_ml.m_map.get(strLanguage);
         if (m_str != null)
         {
            return m_str;
         } else
         {
            return "can not find Language:" + strLanguage + ";key:" + strKey;
         }
      }
      return "can not find key:" + strKey;
   }

   /**
    * @todo 设置MAP值
    * @param in_map HashMap
    */
   public static void setKeyLangMap(HashMap in_map)
   {
      MutiMap = in_map;
   }

   /**
    * @todo 设置后台程序的语言,该方法在ACCPROC中调用
    * @param in_Locale String
    */
   public static void setLocale(String in_Locale)
   {
      if (in_Locale != null && !"".equals(in_Locale))
      {
         default_locale = in_Locale;
      }
   }

   /**
    * @todo SERVLET取字符集
    * @param request HttpServletRequest
    * @param langKey String
    * @return String
    */
   public static String getBackLang(String langKey)
   {
      try
      {
         ResourceBundle m_rb = ResourceBundle.getBundle(BackLangFileName + "_" +
            default_locale);
         return m_rb.getString(langKey).trim();
      } catch (MissingResourceException e)
      {
         e.printStackTrace();
         return "LangUtil: MissingResource Error";
      } catch (Exception e)
      {
         e.printStackTrace();
         return "LangUtil: Get Info Error";
      }

   }

   /**
    * @todo JSP页面读取字符集
    * @param request HttpServletRequest
    * @param langKey1 String
    * @param langKey2 String
    * @return String
    */
   public static String getLang2(HttpServletRequest request, String langKey1,
                                 String langKey2)
   {
      StringBuffer sb = new StringBuffer();
      sb.append(getLang(request, langKey1) +
                " ").append(getLang(request, langKey2));
      return sb.toString();
   }

   /**
    * @todo JSP页面读取字符集
    * @param request HttpServletRequest
    * @param langKey String
    * @return String
    */
   public static String getLang(HttpServletRequest request, String langKey)
   {
      try
      {
         HttpSession m_session = request.getSession();
         String m_UserLang = (String) m_session.getAttribute("USER_LANG");
         return getMutiStr(langKey, m_UserLang);

      } catch (Exception e)
      {
         e.printStackTrace();
         return "LangUtil: Get Info Error";
      }

   }

   public static void main(String[] args)
   {

   }

}
