package org.jpf.xmls;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author 吴平福
 * @version 1.0
 */
public final class JpfXmlUtil
{

	public JpfXmlUtil()
	{
	}


	/**
	 * <p>
	 * Description:取string类型值
	 * </p>
	 * 
	 * @throws Exception
	 */
	public static String GetParStrValue(Element in_el, String in_ParName)
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
	 * <p>
	 * Description:取INT类型值
	 * </p>
	 * 
	 * @throws Exception
	 */
	public static int GetParIntValue(Element in_el, String in_ParName)
	{
		return Integer.parseInt(GetParStrValue(in_el, in_ParName));
	}

	/**
	 * <p>
	 * Description:取BOOL类型值
	 * </p>
	 * 
	 * @throws Exception
	 */
	public static boolean GetParBoolValue(Element in_el, String in_ParName)
	{
		String m_value = GetParStrValue(in_el, in_ParName);
		if (m_value.equalsIgnoreCase("0"))
		{
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Description:从XML读取参数
	 * </p>
	 * 
	 * @throws Exception
	 */
	public static Element ReadXMLFile2(String in_FileName, String in_TagName, String strSecondNode) throws Exception
	{
		Element ell = ParaseXMLFile(in_TagName, in_FileName);
		NodeList childfields = ell.getElementsByTagName(strSecondNode);
		if (childfields.getLength() != 1)
		{
			throw new Exception("读取配置文件出错!没有找到节点" + strSecondNode);
		}
		return (Element) childfields.item(0);

	}

	/**
	 * 
	 * @param in_FileName
	 * @param in_TagName
	 * @return
	 * @throws Exception
	 */
	public static String ReadXMLFile1(String in_FileName, String in_TagName) throws Exception
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(in_FileName);
		Element root = doc.getDocumentElement();

		return GetParStrValue(root, in_TagName);

	}

	public static String ReadXMLFile3(String in_FileName, String in_TagName) throws Exception
	{
		String strOut = "";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(in_FileName);
		Element root = doc.getDocumentElement();
		NodeList books = root.getChildNodes();
		if (books != null)
		{
			for (int i = 0; i < books.getLength(); i++)
			{
				Node book = books.item(i);
				if (book.getNodeType() == Node.ELEMENT_NODE)
				{
					if (book.getNodeName().equals(in_TagName))
					{
						strOut = book.getFirstChild().getNodeValue();
					}
				}
			}
		}
		return strOut;

	}

	/**
	 * @todo 一级解析
	 * @param in_TagName
	 *            String
	 * @param in_FileName
	 *            String
	 * @throws Exception
	 * @return Element
	 */
	public static Element ParaseXMLFile(String in_TagName, String in_FileName) throws
			Exception
	{
		// System.out.println(in_FileName);
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
	 * @param in_Tag
	 *            String
	 * @param in_FileName
	 *            String
	 * @throws Exception
	 * @return NodeList
	 */
	public static NodeList GetNodeList(String in_Tag, String in_FileName) throws
			Exception
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc  = db.parse(in_FileName);

		Element root = doc.getDocumentElement();
		return root.getElementsByTagName(in_Tag);
	}
   
	public static void aa(String in_FileName)
	{
        try{
        	System.out.println(in_FileName);
            //得到DOM解析器的工厂实例
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            //从DOM工厂中获得DOM解析器
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            //把要解析的xml文档读入DOM解析器
            Document doc = dbBuilder.parse(in_FileName);
            System.out.println("处理该文档的DomImplementation对象  = "+ doc.getImplementation());
            //得到文档名称为Student的元素的节点列表
            NodeList nList = doc.getElementsByTagName("Student");
            //遍历该集合，显示结合中的元素及其子元素的名字
            for(int i = 0; i< nList.getLength() ; i ++){
                Element node = (Element)nList.item(i);
                System.out.println("Name: "+ node.getElementsByTagName("Name").item(0).getFirstChild().getNodeValue());
                System.out.println("Num: "+ node.getElementsByTagName("Num").item(0).getFirstChild().getNodeValue());
                System.out.println("Classes: "+ node.getElementsByTagName("Classes").item(0).getFirstChild().getNodeValue());
                System.out.println("Address: "+ node.getElementsByTagName("Address").item(0).getFirstChild().getNodeValue());
                System.out.println("Tel: "+ node.getElementsByTagName("Tel").item(0).getFirstChild().getNodeValue());
            }
            
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
	}
	public static String GetParStrValue2(Element in_el, String in_ParName) throws Exception
	{
		NodeList nodeList = in_el.getElementsByTagName(in_ParName);
		/*
		 * System.out.println("Name: " +
		 * nodeList.item(0).getChildNodes().item(0) .getNodeValue());
		 */
		return nodeList.item(0).getChildNodes().item(0).getNodeValue();
	}

	public static int GetParIntValue2(Element in_el, String in_ParName)
	{
		try
		{
			String tmpString = GetParStrValue2(in_el, in_ParName);
			int iResult = Integer.parseInt(tmpString);
			return iResult;
		} catch (Exception ex)
		{
			// TODO: handle exception

		}
		return 0;
	}
}
