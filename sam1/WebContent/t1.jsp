<%@page import="sam1.ProductList"%><%@page import="java.util.ArrayList"%><%@page import="com.google.gson.Gson"%><%@page import="sam1.Product"%><%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%
    Product product = new Product("노트북1",2000,"samsung");
    ArrayList<Product> list = new ArrayList<>();
    list.add(new Product("노트북1",2000,"samsung1"));
    list.add(new Product("노트북2",9000,"samsung2"));
    list.add(new Product("노트북3",1000,"samsung4"));
    list.add(new Product("노트북4",2000,"samsung"));
    list.add(new Product("노트북5",4000,"samsung"));
    list.add(new Product("노트북6",2000,"samsung"));
    list.add(new Product("노트북7",4000,"samsung"));
    list.add(new Product("노트북8",2000,"samsung"));
    list.add(new Product("노트북9",8000,"samsung"));
    list.add(new Product("노트북10",2000,"samsung"));
	Gson gson = new Gson();
	ProductList productList = new ProductList();
	productList.setCount(list.size());
	productList.setStatus("success");
	productList.setList(list);
	String result = gson.toJson(productList);
	//String result = gson.toJson(list);
	//String result = gson.toJson(product);
	out.println(result);    
    %>