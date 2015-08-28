package com.Chat;

public class DataC {
	private String content;
	public static final int SEND = 1;
	public static final int RECIVER = 2;
	private int flag;

	public DataC(String content, int flag) {
		setContent(content);
		setFlag(flag);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
