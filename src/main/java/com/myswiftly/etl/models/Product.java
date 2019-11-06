package com.myswiftly.etl.models;

import java.io.Serializable;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	private int _storeId;
	private int _id;
	private String _description;
	private String _regularDisplayPrice;
	private float _regularCalculatorPrice;
	private String _promotionalDisplayPrice;
	private float _promotionalCalculatorPrice;
	private UnitOfMeasure _unitOfMeasure;
	private String _productSize;
	private float _taxRate;

	public Product(int _id, String _description, String _regularDisplayPrice, float _regularCalculatorPrice,
			String _promotionalDisplayPrice, float _promotionalCalculatorPrice, UnitOfMeasure _unitOfMeasure,
			String _productSize, float _taxRate) {
		super();
		this._id = _id;
		this._description = _description;
		this._regularDisplayPrice = _regularDisplayPrice;
		this._regularCalculatorPrice = _regularCalculatorPrice;
		this._promotionalDisplayPrice = _promotionalDisplayPrice;
		this._promotionalCalculatorPrice = _promotionalCalculatorPrice;
		this._unitOfMeasure = _unitOfMeasure;
		this._productSize = _productSize;
		this._taxRate = _taxRate;
	}

	public int getStoreId() {
		return _storeId;
	}

	public void setStoreId(int _storeId) {
		this._storeId = _storeId;
	}

	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String _description) {
		this._description = _description;
	}

	public String getRegularDisplayPrice() {
		return _regularDisplayPrice;
	}

	public void setRegularDisplayPrice(String _regularDisplayPrice) {
		this._regularDisplayPrice = _regularDisplayPrice;
	}

	public float getRegularCalculatorPrice() {
		return _regularCalculatorPrice;
	}

	public void setRegularCalculatorPrice(float _regularCalculatorPrice) {
		this._regularCalculatorPrice = _regularCalculatorPrice;
	}

	public String getPromotionalDisplayPrice() {
		return _promotionalDisplayPrice;
	}

	public void setPromotionalDisplayPrice(String _promotionalDisplayPrice) {
		this._promotionalDisplayPrice = _promotionalDisplayPrice;
	}

	public float getPromotionalCalculatorPrice() {
		return _promotionalCalculatorPrice;
	}

	public void setPromotionalCalculatorPrice(float _promotionalCalculatorPrice) {
		this._promotionalCalculatorPrice = _promotionalCalculatorPrice;
	}

	public UnitOfMeasure getUnitOfMeasure() {
		return _unitOfMeasure;
	}

	public void setUnitOfMeasure(UnitOfMeasure _unitOfMeasure) {
		this._unitOfMeasure = _unitOfMeasure;
	}

	public String getProductSize() {
		return _productSize;
	}

	public void setProductSize(String _productSize) {
		this._productSize = _productSize;
	}

	public float getTaxRate() {
		return _taxRate;
	}

	public void setTaxRate(float _taxRate) {
		this._taxRate = _taxRate;
	}

	@Override
	public String toString() {
		return "Product [_storeId=" + _storeId + ", _id=" + _id + ", _description=" + _description
				+ ", _regularDisplayPrice=" + _regularDisplayPrice + ", _regularCalculatorPrice="
				+ _regularCalculatorPrice + ", _promotionalDisplayPrice=" + _promotionalDisplayPrice
				+ ", _promotionalCalculatorPrice=" + _promotionalCalculatorPrice + ", _unitOfMeasure=" + _unitOfMeasure
				+ ", _productSize=" + _productSize + ", _taxRate=" + _taxRate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_description == null) ? 0 : _description.hashCode());
		result = prime * result + _id;
		result = prime * result + ((_productSize == null) ? 0 : _productSize.hashCode());
		result = prime * result + Float.floatToIntBits(_promotionalCalculatorPrice);
		result = prime * result + ((_promotionalDisplayPrice == null) ? 0 : _promotionalDisplayPrice.hashCode());
		result = prime * result + Float.floatToIntBits(_regularCalculatorPrice);
		result = prime * result + ((_regularDisplayPrice == null) ? 0 : _regularDisplayPrice.hashCode());
		result = prime * result + _storeId;
		result = prime * result + Float.floatToIntBits(_taxRate);
		result = prime * result + ((_unitOfMeasure == null) ? 0 : _unitOfMeasure.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (_description == null) {
			if (other._description != null)
				return false;
		} else if (!_description.equals(other._description))
			return false;
		if (_id != other._id)
			return false;
		if (_productSize == null) {
			if (other._productSize != null)
				return false;
		} else if (!_productSize.equals(other._productSize))
			return false;
		if (Float.floatToIntBits(_promotionalCalculatorPrice) != Float
				.floatToIntBits(other._promotionalCalculatorPrice))
			return false;
		if (_promotionalDisplayPrice == null) {
			if (other._promotionalDisplayPrice != null)
				return false;
		} else if (!_promotionalDisplayPrice.equals(other._promotionalDisplayPrice))
			return false;
		if (Float.floatToIntBits(_regularCalculatorPrice) != Float.floatToIntBits(other._regularCalculatorPrice))
			return false;
		if (_regularDisplayPrice == null) {
			if (other._regularDisplayPrice != null)
				return false;
		} else if (!_regularDisplayPrice.equals(other._regularDisplayPrice))
			return false;
		if (_storeId != other._storeId)
			return false;
		if (Float.floatToIntBits(_taxRate) != Float.floatToIntBits(other._taxRate))
			return false;
		if (_unitOfMeasure != other._unitOfMeasure)
			return false;
		return true;
	}
}
