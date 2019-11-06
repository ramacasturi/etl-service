package com.myswiftly.etl.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myswiftly.etl.dao.ProductDAO;
import com.myswiftly.etl.models.Product;

@Controller
public class ProductController {

	@Autowired
	private ProductDAO _productDAO;

	@RequestMapping(method = RequestMethod.GET, path = "/stores/{storeId}/products/{productId}")
	@ResponseBody
	public Product getProductById(@PathVariable int storeId, @PathVariable int productId) {
		return _productDAO.getById(storeId, productId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/stores/{storeId}/products")
	@ResponseBody
	public List<Product> getProductsByStoreId(@PathVariable int storeId) {
		return _productDAO.getAllByStoreId(storeId);
	}
}
