package com.FriendData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.CommonData.StringData;

/**
 * @author hao
 *功能类：程序中可直接调用，返回在线好友信息
 */
public class LocalFriendInfoXMLToList {

	private StringData mydata = new StringData();
	private String LocalFriendInfoXMLPath = null;
	// 好友信息的存放表
	private List<FriendInfo> InfoList = new ArrayList<FriendInfo>();

	public LocalFriendInfoXMLToList() {
		// TODO Auto-generated constructor stub
	}

	public List<FriendInfo> getFriendInfoList() {
		LocalFriendInfoXMLPath = mydata.getFriendInfoLocalXMLPath();
		File xmlFile = new File(LocalFriendInfoXMLPath);
		try {
			DocumentBuilder builder = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
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
			// TODO: handle exception
		}
		return InfoList;
	}
}
