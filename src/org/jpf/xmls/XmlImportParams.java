package org.jpf.xmls;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.w3c.dom.NodeList;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author 吴平福
 * @version 1.0
 */
public class XmlImportParams
{
   private static final String CHILD_TAG_NAME = "IMPORT";
   public XmlImportParams()
   {
   }

   public static void main(String[] args)
   {
      try
      {
        // XmlImportParams xu = new XmlImportParams();
         String in_FileName =
            "D:\\openjs\\openjs3.4\\openjs\\WEB-INF\\importrule.xml";
         String in_TagName = "IMPORT_XML";
         String g_ImportType = "1";
         Element el1 = XmlImportParams.readXMLFile(in_TagName, g_ImportType, in_FileName);
         //ArrayList m_str=XmlUtil.GetDrImportType("D:\\openjs\\openjs3.4\\openjs\\importexecl.xml");
         String mStr = XmlImportParams.getParStrValue(el1, "INSERTSTR");
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
   public static Element readXMLFile(String in_TagName, String iType,
                                     String in_FileName) throws Exception
   {
      Element ell = ParaseXMLFile(in_TagName, in_FileName);
      NodeList childfields = ell.getElementsByTagName(CHILD_TAG_NAME + iType);
      if (childfields.getLength() != 1)
      {
         throw new Exception("读取配置文件出错!没有找到节点" + CHILD_TAG_NAME + iType);
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
   private static Element ParaseXMLFile(String in_TagName, String in_FileName) throws
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
