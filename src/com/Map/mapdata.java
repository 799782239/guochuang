package com.Map;

import java.util.ArrayList;
import java.util.List;

import com.FriendData.FriendInfo;
import com.FriendData.LocalFriendInfoXMLToList;

public class mapdata {
	static LocalFriendInfoXMLToList localFriendInfoXMLToList = new LocalFriendInfoXMLToList();
	static List<FriendInfo> InfoList = localFriendInfoXMLToList.getFriendInfoList();

	public static List<String> getDataSource() {
		List<String> list = new ArrayList<String>();
		
		for (int temp = 0; temp < InfoList.size(); temp++) {
			list.add(InfoList.get(temp).getFriend_Name() + InfoList.get(temp).getFriend_Sex());
		}
		
		return list;
	}
}
