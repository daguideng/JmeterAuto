package com.atguigu.gmall.common.utils.file;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * DOM  Class  Reader
 *
 */
public class ReaderXML {

    public static Map<String, String> readXML(String file) {

        Map<String, String> map = new HashMap<String, String>();


        return map;
    }


    public static void main(String[] args) {
        try {

            //1。获取DOM 解析器的工厂实例。
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //2。获得具体的DOM解析器。
            DocumentBuilder builder = factory.newDocumentBuilder();
            String file = "/Users/ellen/Documents/WorkSpace/java/santiago/resources/sales/ui/ios.xml";
            //3。获取文件
            Document document = builder.parse(new File(file));

            //4。获取根元素
            Element root = document.getDocumentElement();
            System.out.println("suite=" + root.getAttribute("suite"));
            //5。获取根节点[有多个节点]
            NodeList list = root.getElementsByTagName("parameter");

            for (int i = 0; i < list.getLength(); i++) {
                //Node lan = list.item(i);
                //System.out.println("id="+lan.getNodeType());
                System.out.println("---------------");
                Element p = (Element) list.item(i);
                System.out.println("name=" + p.getAttribute("name"));
                System.out.println("value=" + p.getAttribute("value"));

                //获取子节点集合
                NodeList clist = p.getChildNodes();
                for (int j = 0; j < clist.getLength(); j++) {
                    //获取下标
                    Node c = clist.item(j);
                    //把空格删除[获取属性名和值]
                    if (c instanceof Element) {
                        System.out.println(c.getNodeName() + "=" + c.getTextContent());
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}