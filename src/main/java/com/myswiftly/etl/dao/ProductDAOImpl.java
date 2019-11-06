package com.myswiftly.etl.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.myswiftly.etl.models.Product;

@Component
public class ProductDAOImpl implements ProductDAO {

	private Map<Integer, Map<Integer, Product>> products = new ConcurrentHashMap<Integer, Map<Integer, Product>>();

	@Override
	public void saveOrUpdate(Product product) {
		int storeId = product.getStoreId();
		int productId = product.getId();
		if (!products.containsKey(storeId)) {
			products.put(storeId, new ConcurrentHashMap<Integer, Product>());
		}
		products.get(storeId).put(productId, product);
	}

	@Override
	public Product getById(int storeId, int productId) {
		Map<Integer, Product> storeProducts = products.get(storeId);
		if (storeProducts == null) {
			return null;
		}
		return storeProducts.get(productId);
	}

	@Override
	public List<Product> getAllByStoreId(int storeId) {
		Map<Integer, Product> storeProducts = products.get(storeId);
		if (storeProducts == null) {
			return new ArrayList<Product>();
		}
		return new ArrayList<Product>(storeProducts.values());
	}
}
