package com.FriendData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.CommonData.StringData;

/**
 * @author hao 功能类：读取从服务器读取到的input流，并且转化为本地文件
 */
public class DataFromServerToXML {

	//从服务器获取的Input流
	private InputStream input;
	//好友信息的存放表
	private List<FriendInfo> InfoList = new ArrayList<FriendInfo>();
	//获取本地XML存放地址
	private StringData mydata = new StringData();
	private String friendInfoLocalXMLPath = null;
	private int friendInfoSize = 0;
	public DataFromServerToXML(InputStream input) {
		this.input = input;
		friendInfoLocalXMLPath = mydata.getFriendInfoLocalXMLPath();
	}

	/**
	 * 将读取到的input流转化为List
	 * 
	 * @return 所有好友List
	 */
	public List<FriendInfo> ServerDataToList() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(input);
			Element element = document.getDocumentElement();

			NodeList friendInfoNodes = element.getElementsByTagName("friend");
			for (int i = 0; i < friendInfoNodes.getLength(); i++) {
				Element friendInfoElement = (Element) friendInfoNodes.item(i);
				FriendInfo friendInfo = new FriendInfo();

				NodeList childNodes = friendInfoElement.getChildNodes();

				for (int j = 0; j < childNodes.getLength(); j++) {
					if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
						if ("ID".equals(childNodes.item(j).getNodeName())) {
							friendInfo.setFriend_ID(childNodes.item(j)
									.getFirstChild().getNodeValue());
						} else if ("username".equals(childNodes.item(j)
								.getNodeName())) {
							friendInfo.setFriend_username(childNodes.item(j)
									.getFirstChild().getNodeValue());
						} else if ("IP"
								.equals(childNodes.item(j).getNodeName())) {
							friendInfo.setFriend_IP(childNodes.item(j)
									.getFirstChild().getNodeValue());
						} else if ("Local_longitude".equals(childNodes.item(j)
								.getNodeName())) {
							friendInfo.setFriend_Local_longitude(Double
									.parseDouble(childNodes.item(j)
											.getFirstChild().getNodeValue()));
						} else if ("Local_latitude".equals(childNodes.item(j)
								.getNodeName())) {
							friendInfo.setFriend_Local_latitude(Double
									.parseDouble(childNodes.item(j)
											.getFirstChild().getNodeValue()));
						} else if ("Name".equals(childNodes.item(j)
								.getNodeName())) {
							friendInfo.setFriend_Name(childNodes.item(j)
									.getFirstChild().getNodeValue());
						} else if ("Phone".equals(childNodes.item(j)
								.getNodeName())) {
							friendInfo.setFriend_Phone(childNodes.item(j)
									.getFirstChild().getNodeValue());
						} else if ("Sex".equals(childNodes.item(j)
								.getNodeName())) {
							friendInfo.setFriend_Sex(childNodes.item(j)
									.getFirstChild().getNodeValue());
						}

					}
				}
				InfoList.add(friendInfo);
			}
		} catch (Exception e) {
		}
		return InfoList;
	}
	
	public void ListDataToXML() {
		//获得好友列表大小
		friendInfoSize = InfoList.size();
		File xmlFile = new File(friendInfoLocalXMLPath);
		try {
			xmlFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(xmlFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("can't create FileOutputStream");
		}
		
		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(fileos, "UTF-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, "friends");
			
			for (int i = 0; i < friendInfoSize; i++) {
				serializer.startTag(null, "friend");
				
				serializer.startTag(null, "ID");
				serializer.text(InfoList.get(i).getFriend_ID());
				serializer.endTag(null, "ID");
				
				serializer.startTag(null, "username");
				serializer.text(InfoList.get(i).getFriend_username());
				serializer.endTag(null, "username");
				
				serializer.startTag(null, "IP");
				serializer.text(InfoList.get(i).getFriend_IP());
				serializer.endTag(null, "IP");
				
				serializer.startTag(null, "Local_longitude");
				serializer.text(InfoList.get(i).getFriend_Local_longitude().toString());
				serializer.endTag(null, "Local_longitude");
				
				serializer.startTag(null, "Local_latitude");
				serializer.text(InfoList.get(i).getFriend_Local_latitude().toString());
				serializer.endTag(null, "Local_latitude");
				
				serializer.startTag(null, "Name");
				serializer.text(InfoList.get(i).getFriend_Name());
				serializer.endTag(null, "Name");
				
				serializer.startTag(null, "Phone");
				serializer.text(InfoList.get(i).getFriend_Phone());
				serializer.endTag(null, "Phone");
				
				serializer.startTag(null, "Sex");
				serializer.text(InfoList.get(i).getFriend_Sex());
				serializer.endTag(null, "Sex");
				
				serializer.endTag(null, "friend");
			}
			
			serializer.endTag(null, "friends");
			serializer.endDocument();
			serializer.flush();
			fileos.close();
			System.out.println("创建xml文件成功");
		} catch (Exception e) {
		}
	}
}
