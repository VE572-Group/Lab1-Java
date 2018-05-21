import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Demo {

    public static void main(String[] args) {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document dDoc = builder.parse("E:/test.xml");

            XPath xPath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xPath.evaluate("/Request/@name", dDoc, XPathConstants.NODE);
            System.out.println(node.getNodeValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}