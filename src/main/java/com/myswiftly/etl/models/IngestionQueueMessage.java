package com.myswiftly.etl.models;

public class IngestionQueueMessage {
	private String _id;
	private int _storeId;
	private String _catalogFileUri;

	public IngestionQueueMessage(String id, int storeId, String catalogFileUri) {
		super();
		this._id = id;
		this._storeId = storeId;
		this._catalogFileUri = catalogFileUri;
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public int getStoreId() {
		return _storeId;
	}

	public void setStoreId(int storeId) {
		this._storeId = storeId;
	}

	public String getCatalogFileUri() {
		return _catalogFileUri;
	}

	public void setCatalogFileUri(String catalogFileUri) {
		this._catalogFileUri = catalogFileUri;
	}
}
