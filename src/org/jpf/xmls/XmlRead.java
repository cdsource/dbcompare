package org.jpf.xmls;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.w3c.dom.NodeList;
/**
 * <p>Title: NGBOSS--WBASS</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: asiainfo</p>
 *
 * @author 吴平福
 * @version 4.0
 */
public class XmlRead
{
   public XmlRead()
   {
   }



   public static void main(String[] args)
   {
      try
      {

         String in_FileName =
            "D:\\openwbass\\openwbass1.0\\openwbass\\WEB-INF\\configfile\\wbass_cyclemgr.xml";
         String TAG_NAME_First = "CycleMgrConf";
         String TAG_NAME_Second = "ExportDr";
         Element el1 = XmlRead.readXMLFile(TAG_NAME_First, TAG_NAME_Second,in_FileName);
         //ArrayList m_str=XmlUtil.GetDrImportType("D:\\openjs\\openjs3.4\\openjs\\importexecl.xml");
         String mStr = XmlRead.getParStrValue(el1, "COLUMN_ERR_DR_WJ_VOICE");
         System.out.println(mStr);
      } catch (Exception ex)
      {
         ex.printStackTrace();
      }
   }

   /**
    * <p>Description:取string类型值</p>
    * @throws Exception
    */
   public static String getParStrValue(Element in_el, String in_ParName)
   {
      NodeList fields = in_el.getElementsByTagName(in_ParName);
      String m_value = "";
      if (fields.getLength() == 1)
      {
         if (fields.item(0).getFirstChild() != null)
         {
            m_value = fields.item(0).getFirstChild().getNodeValue();
         }
      }
      if (m_value == null)
      {
         m_value = "";
      }
      return m_value;
   }

   /**
    * <p>Description:取INT类型值</p>
    * @throws Exception
    */
   public static int getParIntValue(Element in_el, String in_ParName)
   {
      return Integer.parseInt(getParStrValue(in_el, in_ParName));
   }

   /**
    * <p>Description:取BOOL类型值</p>
    * @throws Exception
    */
   public static boolean getParBoolValue(Element in_el, String in_ParName)
   {
      String m_value = getParStrValue(in_el, in_ParName);
      if (m_value.equalsIgnoreCase("0"))
      {
         return false;
      }
      return true;
   }

   /**
    * <p>Description:从XML读取参数</p>
    * @throws Exception
    */
   public static Element readXMLFile(String in_FirstTagName, String in_SecondTagName,
                                     String in_FileName) throws Exception
   {
      Element ell = ParaseXMLFile(in_FirstTagName, in_FileName);
      NodeList childfields = ell.getElementsByTagName(in_SecondTagName);
      if (childfields.getLength() != 1)
      {
         throw new Exception("读取配置文件出错!没有找到节点" + in_SecondTagName);
      }
      return (Element) childfields.item(0);

   }

   /**
    * @todo 一级解析
    * @param in_TagName String
    * @param in_FileName String
    * @throws Exception
    * @return Element
    */
   public static Element ParaseXMLFile(String in_TagName, String in_FileName) throws
      Exception
   {
      //System.out.println(in_FileName);
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(in_FileName);
      Element root = doc.getDocumentElement();
      NodeList fields = root.getElementsByTagName(in_TagName);
      if (fields.getLength() != 1)
      {
         throw new Exception("读取配置文件出错!没有找到节点" + in_TagName);
      }
      return (Element) fields.item(0);

   }



   /**
    *
    * @param in_Tag String
    * @param in_FileName String
    * @throws Exception
    * @return NodeList
    */
   public static NodeList GetNodeList(String in_Tag, String in_FileName) throws
      Exception
   {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = null;
      Document doc = null;
      db = dbf.newDocumentBuilder();
      doc = db.parse(in_FileName);

      Element root = doc.getDocumentElement();
      return root.getElementsByTagName(in_Tag);
   }


}
