package com.atguigu.gmall.common.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.common.exception.ServiceException;


/**
 * 
 * @ClassName: XMLUtils
 * @Description: XMLUtils
 * @author shirui
 * @date 2018年6月4日 上午11:51:54
 *
 */
public class XMLUtils {

	private static Logger logger = LoggerFactory.getLogger(XMLUtils.class);
	/**
	 * 根据替换规则替换指定路径下的xml文件： Map<String,String> rules; key是需要替换的xpath,
	 * value是替换后的值
	 * 
	 * @throws
	 * @throws FileNotFoundException
	 */
	public static void replaceByXpathMap(String xmlPath, Map<String, String> rules) {

		Document document = null;
		DocumentBuilder documentBuilder = null;
		DocumentBuilderFactory factory = null;
		File file = new File(xmlPath);

		try {
			factory = DocumentBuilderFactory.newInstance();
			documentBuilder = factory.newDocumentBuilder();
			document = documentBuilder.parse(new FileInputStream(file));
		} catch (Exception e) {
		//	throw new ServiceException(RES_STATUS.INVALID_XML_FORMAT);
		}

		Element root = document.getDocumentElement();

		if (rules != null && rules.size() > 0) {
			Set<String> xpathSet = rules.keySet();
			// 替换xml文件内容
			for (String xpath : xpathSet) {
				Element sourceNode = (Element) selectByXpath(root, xpath);
				sourceNode.setTextContent(rules.get(xpath));

			}
			// 保存替换后的xml内容
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer former;
			try {
				former = transFactory.newTransformer();
				former.transform(new DOMSource(document), new StreamResult(new File(xmlPath)));
			} catch (TransformerConfigurationException e) {
			//	throw new ServiceException(RES_STATUS.SAVE_XML_FILED);
			} catch (TransformerException e) {
			//	throw new ServiceException(RES_STATUS.SAVE_XML_FILED);
			}
		}

	}

	/**
	 * 根据xpath选择指定节点下满足条件的子节点
	 */
	public static Node selectByXpath(Element root, String xpathExpress) {

		Node result = null;
		
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			result = (Node) xpath.evaluate(xpathExpress, root, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
		//	throw new ServiceException(RES_STATUS.MISSING_XML_NODE);
		}
		if (result == null) {
		//	throw new ServiceException(RES_STATUS.MISSING_XML_NODE);
		}
		return result;
	}


}
