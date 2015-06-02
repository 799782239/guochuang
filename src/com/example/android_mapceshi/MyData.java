package com.example.android_mapceshi;

import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;

public class MyData {

	public static List<String> getDataSource() {
		List<String> list = new ArrayList<String>();
		list.add("ÕÅÈı" +
				"100M");
		list.add("ÂíÑà" +
				"200m");
		list.add("µ­µ­" +
				"10Km");
		return list;
	}
}
