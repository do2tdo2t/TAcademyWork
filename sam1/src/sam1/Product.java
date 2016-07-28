package sam1;

import java.io.Serializable;

public class Product implements Serializable {

	private static final long serialVersionUID = -6345752248485183425L;
	private String name;
	private int price;
	private String maker;
	
	public Product(String name, int price, String maker) {
		super();
		this.name = name;
		this.price = price;
		this.maker = maker;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getMaker() {
		return maker;
	}
	public void setMaker(String maker) {
		this.maker = maker;
	}
	@Override
	public String toString() {
		return "Product [name=" + name + ", price=" + price + ", maker=" + maker + "]";
	}
	
}
