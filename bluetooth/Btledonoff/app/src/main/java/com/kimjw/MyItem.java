package com.kimjw;

import java.io.Serializable;

public class MyItem implements Serializable {

	private static final long serialVersionUID = -6703200596890457645L;
	private String address;
	private String name;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
