package com.example.model;

public class test {
	private String tour;
	private int price;
	
	public test() {		
	}
	
	public test(String tour, int price) {
		this.tour = tour;
		this.price = price;
	}
	
	public String get_area() {
		return tour;
	}
	
	public void set_area(String tour) {
		this.tour = tour;
	}
	
	public int get_price() {
		return price;
	}
	
	public void set_price(int price) {
		this.price = price;
	}
}