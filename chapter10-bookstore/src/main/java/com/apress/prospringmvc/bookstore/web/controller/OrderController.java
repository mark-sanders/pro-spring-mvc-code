package com.apress.prospringmvc.bookstore.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.apress.prospringmvc.bookstore.domain.Book;
import com.apress.prospringmvc.bookstore.domain.Category;
import com.apress.prospringmvc.bookstore.service.BookstoreService;
import com.apress.prospringmvc.bookstore.service.CategoryService;


@Controller
public class OrderController {
	
	@Autowired
	private BookstoreService bookstoreService;

	@Autowired
	private CategoryService categoryService;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");

	public OrderForm initializeForm() {
		OrderForm orderForm = new OrderForm();
		orderForm.setQuantity(1);
		orderForm.setOrderDate(simpleDateFormat.format(new Date()));
		return orderForm;
	}

	public Map<Long, String> initializeSelectableCategories() {
		Map<Long, String> selectableCategories = new HashMap<Long, String>();
		
		List<Category> categories = categoryService.findAll();
		for (Category category : categories) {
			selectableCategories.put(category.getId(), category.getName());
		}
		
		return selectableCategories;
	}
	
	public Map<Long, String> initializeSelectableBooks(OrderForm orderForm) {
		
		orderForm.resetSelectedBooks();
		
		Category selectedCategory = categoryService.findById(orderForm.getCategoryId());
		List<Book> booksByCategory = bookstoreService.findBooksByCategory(selectedCategory);
		
		Map<Long, String> selectableBooks = new HashMap<Long, String>();
		for (Book book : booksByCategory) {
			selectableBooks.put(book.getId(), book.getTitle());
		}
		
		return selectableBooks;
	}
}
