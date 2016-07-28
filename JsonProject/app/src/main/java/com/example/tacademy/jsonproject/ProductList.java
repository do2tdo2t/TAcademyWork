package com.example.tacademy.jsonproject;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductList implements Serializable {

	private static final long serialVersionUID = 1421827398645045936L;
	private String status ;
	private int count;
	private ArrayList<Product> list;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public ArrayList<Product> getList() {
		return list;
	}
	public void setList(ArrayList<Product> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "ProductList [status=" + status + ", count=" + count + ", list=" + list + "]";
	}
	

}
