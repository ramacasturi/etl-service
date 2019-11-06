package com.myswiftly.etl.dao;

import java.util.List;

import com.myswiftly.etl.models.Product;

public interface ProductDAO {
	public void saveOrUpdate(Product product);

	public Product getById(int storeId, int productId);

	public List<Product> getAllByStoreId(int storeId);
}
