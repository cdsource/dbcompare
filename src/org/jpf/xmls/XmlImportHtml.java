package org.jpf.xmls;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.ArrayList;
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
public class XmlImportHtml
{
   public XmlImportHtml()
   {
   }
   /**
    * @todo 读取详单导入类型
    * @param in_FileName String
    * @throws Exception
    * @return ArrayList
    */
   public static ArrayList GetDrImportType(String in_FileName) throws Exception
   {
      //return GetImportType(in_FileName,"IMPORT_TXT");

      ArrayList strDrImportType = new ArrayList();

      Element ell = XmlRead.ParaseXMLFile("IMPORT_TXT", in_FileName);
      NodeList fields = ell.getChildNodes();

      for (int i = 0; i < fields.getLength(); i++)
      {
         Node m_node = fields.item(i);
         String m_str = m_node.getNodeName();
         if (m_str.startsWith(XmlConst.Import_CHILD_TAG_NAME ))
         {
            strDrImportType.add(m_node.getAttributes().getNamedItem("title").
                                getNodeValue() + ";" +
                                m_str.substring(XmlConst.Import_CHILD_TAG_NAME.length()));
         }
      }

      return strDrImportType;

   }
}
