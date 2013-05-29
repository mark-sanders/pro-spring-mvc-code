package com.apress.prospringmvc.bookstore.web.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.apress.prospringmvc.bookstore.domain.Book;

@SuppressWarnings("serial")
public class OrderForm implements Serializable {

	private final Map<Book, Integer> selectedBooks = new HashMap<Book, Integer>();
	
	private Long categoryId;
	
	private Long bookId;

	private Integer quantity;
	
	private String orderDate;
	
	private String deliveryDate;

	
	public void resetSelectedBooks() {
		selectedBooks.clear();
	}
	
	public Map<Book, Integer> getSelectedBooks() {
		return Collections.unmodifiableMap(selectedBooks);
	}
	
	
	public Long getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	
	public Long getBookId() {
		return bookId;
	}
	
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	
	
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	
	
}
