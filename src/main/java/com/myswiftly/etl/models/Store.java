package com.myswiftly.etl.models;

import java.io.Serializable;

public class Store implements Serializable {

	private static final long serialVersionUID = 1L;

	private int _id;
	private String _name;

	public Store(int id, String name) {
		super();
		this._id = id;
		this._name = name;
	}

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		this._id = id;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

}
