package com.FriendData;

/**
 * @author hao 数据类：用于存放好友所有信息 1、好友用户名 2、好友IP地址（用于聊天连接） 3、好友横纵坐标（用于地图标点）
 *         4、好友性别（地图标点，不同性别不同颜色的点） 5、好友姓名、电话
 */
public class FriendInfo {

	
	private String friend_ID;
	private String friend_username;
	private String friend_IP;
	private Double friend_Local_longitude;
	private Double friend_Local_latitude;
	private String friend_Name;
	private String friend_Phone;
	private String friend_Sex;

	public FriendInfo() {
		// TODO Auto-generated constructor stub
	}

	
	public String getFriend_ID() {
		return friend_ID;
	}


	public void setFriend_ID(String friend_ID) {
		this.friend_ID = friend_ID;
	}


	public String getFriend_username() {
		return friend_username;
	}

	public void setFriend_username(String friend_username) {
		this.friend_username = friend_username;
	}

	public String getFriend_IP() {
		return friend_IP;
	}

	public void setFriend_IP(String friend_IP) {
		this.friend_IP = friend_IP;
	}

	public Double getFriend_Local_longitude() {
		return friend_Local_longitude;
	}

	public void setFriend_Local_longitude(Double friend_Local_longitude) {
		this.friend_Local_longitude = friend_Local_longitude;
	}

	public Double getFriend_Local_latitude() {
		return friend_Local_latitude;
	}

	public void setFriend_Local_latitude(Double friend_Local_latitude) {
		this.friend_Local_latitude = friend_Local_latitude;
	}

	public String getFriend_Name() {
		return friend_Name;
	}

	public void setFriend_Name(String friend_Name) {
		this.friend_Name = friend_Name;
	}

	public String getFriend_Phone() {
		return friend_Phone;
	}

	public void setFriend_Phone(String friend_Phone) {
		this.friend_Phone = friend_Phone;
	}

	public String getFriend_Sex() {
		return friend_Sex;
	}

	public void setFriend_Sex(String friend_Sex) {
		this.friend_Sex = friend_Sex;
	}

}
