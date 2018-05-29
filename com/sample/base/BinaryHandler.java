package com.sample.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BinaryHandler {

	public static void main(String[] args) throws XPathExpressionException {
		String binary="validate.bin";
		String xml="validate.xml";
		String outputFile=parseFile(binary,xml);
		System.out.println(outputFile);
		
	}
	public static String parseFile(String binaryFile,String xmlFile) throws XPathExpressionException {
		try {
		File input=new File(binaryFile);
		FileInputStream fis=new FileInputStream(input);
		File xml= new File(xmlFile);
		long length=input.length();
		byte[] bytes = new byte[(int) length];
		fis.read(bytes);
		fis.close();
//		XPATH
        FileInputStream fileIS = new FileInputStream(xml);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(fileIS);
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression_name = "/atfx_file/instance_data/MeaQuantity/Name/text()";
        String expression_unit="/atfx_file/instance_data/MeaQuantity/Unit/text()";
        String expression_datatype="/atfx_file/instance_data/MeaQuantity/DataType/text()";
        String unit_name="/atfx_file/instance_data/Unit/Name/text()";
        String expression_quantity="/atfx_file/instance_data/MeaQuantity/Quantity/text()";
        String external_blocksize="/atfx_file/instance_data/ExternalComponent/Blocksize/text()";
        String external_valuesperblock="/atfx_file/instance_data/ExternalComponent/ValuesPerBlock/text()";
        String external_valueoffset="/atfx_file/instance_data/ExternalComponent/ValueOffset/text()";
        String external_length="/atfx_file/instance_data/ExternalComponent/Length/text()";
        String external_startoffset="/atfx_file/instance_data/ExternalComponent/StartOffset/text()";
        String quantity="/atfx_file/instance_data/Quantity/Name/text()";
        NodeList nodeList_name = (NodeList) xPath.compile(expression_name).evaluate(xmlDocument, XPathConstants.NODESET);
        NodeList nodeList_unit = (NodeList) xPath.compile(expression_unit).evaluate(xmlDocument, XPathConstants.NODESET);
        NodeList nodeList_unit_name=(NodeList) xPath.compile(unit_name).evaluate(xmlDocument, XPathConstants.NODESET);
        NodeList nodeList_datatype = (NodeList) xPath.compile(expression_datatype).evaluate(xmlDocument, XPathConstants.NODESET);
        NodeList nodeList_external_blocksize=(NodeList) xPath.compile(external_blocksize).evaluate(xmlDocument, XPathConstants.NODESET);
        NodeList nodeList_external_valuesperblock=(NodeList) xPath.compile(external_valuesperblock).evaluate(xmlDocument, XPathConstants.NODESET);
        NodeList nodeList_external_valueoffset=(NodeList) xPath.compile(external_valueoffset).evaluate(xmlDocument, XPathConstants.NODESET);
        NodeList nodeList_external_length=(NodeList) xPath.compile(external_length).evaluate(xmlDocument, XPathConstants.NODESET);
        NodeList nodeList_external_startoffset=(NodeList) xPath.compile(external_startoffset).evaluate(xmlDocument, XPathConstants.NODESET);
        NodeList nodeList_getquantity=(NodeList) xPath.compile(quantity).evaluate(xmlDocument, XPathConstants.NODESET);
        NodeList nodeList_quantity=(NodeList) xPath.compile(expression_quantity).evaluate(xmlDocument, XPathConstants.NODESET);

        int len=nodeList_name.getLength();
		File file= new File("lab_1.csv");
		 PrintWriter pw = new PrintWriter(file);
		 StringBuilder sb = new StringBuilder();
		//Name
		 	for (int i=0;i<len;i++) {
		 		sb.append(nodeList_name.item(i).getTextContent());
		 		if(i==len-1) {
		 			sb.append('\n');
		 		}
		 		else sb.append(',');
		 	}
	    //Unit
		 	for(int i=0;i<len;i++) {
		 		int index=Integer.valueOf(nodeList_unit.item(i).getTextContent());
		 		sb.append(nodeList_unit_name.item(index-1).getTextContent());
		 		if(i==len-1) {
		 			sb.append('\n');
		 		}
		 		else sb.append(',');
		 	
		 	}
		 	
		 	
	 	//Quantity
		 	for(int i=0;i<len;i++) {
		 		int index=Integer.valueOf(nodeList_quantity.item(i).getTextContent());
		 		sb.append(nodeList_getquantity.item(index-1).getTextContent());
		 		if(i==len-1) {
		 			sb.append('\n');
		 		}
		 		else sb.append(',');
		 	
		 	}
		 	
	 	//int StartOffset
	     //int Blocksize
		 //int ValuesPerBlock
		 //int ValueOffset
	 	 //int Length
	 	 //String DataType
		 	
		 String[][] arr=new String[len][getMaxOfList(nodeList_external_length)];
		 for (int i=0;i<len;i++) {
			 int outerOffset=Integer.valueOf(nodeList_external_startoffset.item(i).getTextContent());
	 			int innerOffset=Integer.valueOf(nodeList_external_valueoffset.item(i).getTextContent());
	 			int step=Integer.valueOf(nodeList_external_blocksize.item(i).getTextContent());
	 			int valuesperblock=Integer.valueOf(nodeList_external_valuesperblock.item(i).getTextContent());
	 			int maxLength=getMaxOfList(nodeList_external_length);
	 			int itemLength=Integer.valueOf(nodeList_external_length.item(i).getTextContent());
	 			switch(nodeList_datatype.item(i).getTextContent()) {
		 			case "DT_FLOAT":
		 				for (int j=0;j<maxLength;j++) {
		 					if(itemLength>=j+1) {
			 					int iteration=j/valuesperblock;
			 					int blockOffset=j%valuesperblock;
				 				int id1=get32(bytes,iteration,outerOffset,innerOffset+blockOffset*4,step);
				 				float id_1=Float.intBitsToFloat(id1);
				 				String id=Float.toString(id_1);
				 				arr[i][j]=id;
		 					}
		 					else {
		 						arr[i][j]="";
		 					}
		 				}
		 				break;
		 			case "DT_LONG":
		 				for (int j=0;j<maxLength;j++) {
		 					if(itemLength>=j+1) {
			 					int iteration=j/valuesperblock;
			 					int blockOffset=j%valuesperblock;
			 					long id_2=(long)get32(bytes,iteration,outerOffset,innerOffset+blockOffset*4,step);
				 				String id=Long.toString(id_2);
				 				arr[i][j]=id;
		 					}
		 					else {
		 						arr[i][j]="";
		 					}
		 				}
		 
		 				break;
		 			case "DT_SHORT":
		 				for (int j=0;j<maxLength;j++) {
		 					if(itemLength>=j+1) {
			 					int iteration=j/valuesperblock;
			 					int blockOffset=j%valuesperblock;
			 					short id_3=get16(bytes,iteration,outerOffset,innerOffset+blockOffset*2,step);
				 				String id=Short.toString(id_3);
				 				arr[i][j]=id;
		 					}
		 					else {
		 						arr[i][j]="";
		 					}
		 				}
		 				break;
		 			case "DT_DOUBLE":
		 				for (int j=0;j<maxLength;j++) {
		 					if(itemLength>=j+1) {
			 					int iteration=j/valuesperblock;
			 					int blockOffset=j%valuesperblock;
			 					long id4=get64(bytes,iteration,outerOffset,innerOffset+blockOffset*8,step);
				 				double id_4=Double.longBitsToDouble(id4);
				 				String id=Double.toString(id_4);
				 				arr[i][j]=id;
		 					}
		 					else {
		 						arr[i][j]="";
		 					}
		 				}
		 				break;
	 				default:
	 					break;
		 			}
		 }
		 	
		 
		 //into csv
		 for (int i=0;i<getMaxOfList(nodeList_external_length);i++) {
			 for(int j=0;j<len;j++) {
				 sb.append(arr[j][i]);
				 if(j==len-1) {
			 			sb.append('\n');
			 		}
			 		else sb.append(',');
				 
			 }
		 }
	
		
		
		
	        pw.print(sb.toString());
	        pw.close();
	        
	        
	        
		
		}
		catch(IOException e){
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "lab_1.csv";	
		
	}

	protected static int get32(byte[] bytes, int i, int outerOffset, int innerOffset, int step) {
		int value = 0;
		value += (bytes[i * step + 3 + innerOffset + outerOffset] & 0x00FF) << 24;
		value += (bytes[i * step + 2 + innerOffset + outerOffset] & 0x00FF) << 16;
		value += (bytes[i * step + 1 + innerOffset + outerOffset] & 0x00FF) << 8;
		value += (bytes[i * step + innerOffset + outerOffset] & 0x00FF);
		return value;
	}

	protected static short get16(byte[] bytes, int i, int outerOffset, int innerOffset, int step) {
		short value = 0;
		value += (bytes[i * step + 1 + innerOffset + outerOffset] & 0xFF) << 8;
		value += (bytes[i * step + innerOffset + outerOffset] & 0xFF);
		return value;
	}

	protected static int getMaxOfList(NodeList list) {
		int length = list.getLength();
		int maxValue = 0;
		for (int i = 0; i < length; i++) {
			if (Integer.valueOf(list.item(i).getTextContent()) > maxValue) {
				maxValue = Integer.valueOf(list.item(i).getTextContent());
			}
		}
		return maxValue;
	}

}
