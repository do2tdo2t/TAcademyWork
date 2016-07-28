package com.example.tacademy.volleylistproject;

import java.io.Serializable;

public class MyItem implements Serializable {


	private static final long serialVersionUID = 6049686051833374484L;
	private String imgName;
	private String title;
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "MyItem [imgName=" + imgName + ", title=" + title + "]";
	}
	
}
