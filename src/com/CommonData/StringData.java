package com.CommonData;

/**
 * @author hao 数据类：存放一般消息，类似URL地址，SD卡地址
 */
public class StringData {
	private final String LoginPath = "http://172.23.191.1:8080/NoticeServer/servlet/UserLoginServlet";
	private final static String RegisterPath = "http://172.23.191.1:8080/NoticeServer/servlet/RegisterServlet";
	private final String GetNoticePath = "http://172.23.191.1:8080/NoticeServer/servlet/NoticeServlet";
	private final String XMLPath = "http://172.23.191.1:8080/NoticeServer/Notice.xml";
	private final String LocalXMLPath = "/sdcard/greenroad/notices.xml";
	// 刷新信息服务器地址
	private final String RefreshFriendInfoPath = "http://172.23.191.1:8080/NoticeServer/servlet/GetUserInfoServlet";
	// 客户端本地朋友信息XML地址
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
