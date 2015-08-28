package com.CommonData;

/**
 * @author hao �����ࣺ���һ����Ϣ������URL��ַ��SD����ַ
 */
public class StringData {
	private final String LoginPath = "http://172.23.191.1:8080/NoticeServer/servlet/UserLoginServlet";
	private final static String RegisterPath = "http://172.23.191.1:8080/NoticeServer/servlet/RegisterServlet";
	private final String GetNoticePath = "http://172.23.191.1:8080/NoticeServer/servlet/NoticeServlet";
	private final String XMLPath = "http://172.23.191.1:8080/NoticeServer/Notice.xml";
	private final String LocalXMLPath = "/sdcard/greenroad/notices.xml";
	// ˢ����Ϣ��������ַ
	private final String RefreshFriendInfoPath = "http://172.23.191.1:8080/NoticeServer/servlet/GetUserInfoServlet";
	// �ͻ��˱���������ϢXML��ַ
	private final String FriendInfoLocalXMLPath = "/sdcard/friend.xml";

	public String getLoginPath() {
		return LoginPath;
	}

	public static String getRegisterpath() {
		return RegisterPath;
	}

	public String getGetNoticePath() {
		return GetNoticePath;
	}

	public String getXMLPath() {
		return XMLPath;
	}

	public String getLocalXMLPath() {
		return LocalXMLPath;
	}

	public String getRefreshFriendInfoPath() {
		return RefreshFriendInfoPath;
	}

	public String getFriendInfoLocalXMLPath() {
		return FriendInfoLocalXMLPath;
	}

}
