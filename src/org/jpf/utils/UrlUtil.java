/**
 * 
 */
package org.jpf.utils;

/**
 * @author 吴平福
 *
 * @version 创建时间：2010-7-21 下午03:48:38
 */
public class UrlUtil
{
   private static final String URLOUTRIGHTSTR = "#";
   private static final String URLOUTLEFTSTR = "http://";
   //private static final String URLKEYSTR = "?";
   
   public static String GetUrlName(String m_url)
   {
      if (m_url.endsWith(URLOUTRIGHTSTR))
      {
         m_url = m_url.substring(0, m_url.length() - URLOUTRIGHTSTR.length());
      }
      if (m_url.substring(0,
                          URLOUTLEFTSTR.length()).equalsIgnoreCase(URLOUTLEFTSTR))
      {
         m_url = m_url.substring(URLOUTLEFTSTR.length(), m_url.length());
      }

      int i = m_url.indexOf("/");
      m_url = m_url.substring(i);

      return m_url;
   }
}
